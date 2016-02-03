package beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import tp.ejb.BankDaoBean;
import tp.ejb.CityDaoBean;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;

@ManagedBean(name = "bankWeb")
@SessionScoped
public class BankWeb implements Serializable {
	private static final long serialVersionUID = 1654321856L;
	
	@EJB
	private BankDaoBean bankDao;
	private List<Bank> banks;
	private Bank currentBank;

	
	@EJB
	private CityDaoBean cityDao;
	
	
	public String modify() {
		bankDao.merge(currentBank);
		return "banks";
	}
	public String next() {
		setCurrentBank(bankDao.next(getCurrentBank()));
		return "banks";
	}
	public String prior() {
		setCurrentBank(bankDao.prior(getCurrentBank()));
		return "banks";
	}
	public void customers(int bankId) {
		MBUtils.redirect("customers.xhtml?id=" + bankId);
	}
	public Bank getCurrentBank() {
		if (currentBank == null)
			getAllBanks();
		return currentBank;
	}
	public void setCurrentBank(Bank bank) {
		this.currentBank = bankDao.find(bank);
		//System.out.println("currentBank=" + this.currentBank.getName());
	}
	public boolean isCurrentBank(Bank bank) {
		return bank.equals(currentBank);
	}
	private void validate(Bank b) {
		if (b.getName().toLowerCase().contains("bernard madoff"))
			throw new RuntimeException("the name is forbidden!");
	}
	public List<Bank> getAllBanks() {
		banks = bankDao.getList();
		if (banks.isEmpty()) {
			bankDao.populate();
			banks = bankDao.getList();
		}
		if (!banks.isEmpty() && currentBank == null)
			currentBank = banks.get(0);
		return banks;
	}
	private Map<String, String> cityMap;	
	public Map<String, String> getCities(){
		if (cityMap == null){
			cityMap = new HashMap();
			List<City> cities = cityDao.getList();
			cityMap.put("no city", "");
			for (City city : cities) 
				cityMap.put(city.getName(), Integer.toString(city.getId()));
		}
		return cityMap;
	}
	public String getCurrentBankCity() {
		City city = getCurrentBank().getCity();
		if (city != null)
			return Integer.toString(getCurrentBank().getCity().getId());
		else
			return "";
	}

	public void setCurrentBankCity(String sid) {
		if (sid != null && !sid.isEmpty()) {
			City city = cityDao.find(Integer.parseInt(sid));
			getCurrentBank().setCity(city);
		}
	}
	/**
	 * create the session before the Facelet (/JSP) page is printed-----
	 */
	@PostConstruct
	void initialiseSession() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}
}

/*
 * JSF 2 does enhance the h:commandButton by allowing the command to invoke an
 * associated action by passing a parameters for it. The main attribute of the
 * h:commandButton is an action attribute that accepts a method-binding
 * expression for a backing bean action (method) to be invoked when the user has
 * clicked on the button. The method-binding expression has the following roles
 * to be accepted as a proper action. The defined method (action) in the backing
 * bean should have a public access modifier. The defined method (action) in the
 * backing bean should have a String as returned type. (This string token will
 * be consumed by the JavaServer Faces Navigation Handler). Starting with JSF
 * 2.0, the defined method can accept a parameter values in the method
 * signature, this feature is useful for providing parameters to the actions of
 * buttons and links. When a method reference is evaluated, the parameters are
 * evaluated and passed to the method. The h:commandButton and h:commandLink are
 * the primary components for navigating within a JSF application, when a button
 * or link is clicked (activated), a POST request sends the form data back to
 * the server and the JSF framework lifecycle has started. The main difference
 * between h:commandButton and h:commandLink that the latter provides a
 * predefined code at the onclick JavaScript attribute
 */
