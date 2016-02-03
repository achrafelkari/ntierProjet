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

import domain.model.Account;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;
import tp.ejb.AccountDaoBean;
import tp.ejb.CustomerDaoBean;
import tp.ejb.TransferDaoBean;


@ManagedBean(name = "accountWeb")
@SessionScoped
public class AccountWeb implements Serializable{
    
	private static final long serialVersionUID = 165432181L;
	
	@EJB
	private AccountDaoBean accountDao ; 
	private List<Account> accounts;
	private Account currentAccount;
	private Customer currentCustomer;
	private int customerID;
	
	
	@EJB
	private TransferDaoBean transferDao;
	
	@EJB
	private CustomerDaoBean customerDao;
	
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	public Customer getCurrentCustomer() {
		return currentCustomer;
	}
	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}
	public String next() {
	     setCurrentAccount(accountDao.next(currentCustomer,currentAccount));
		return "accounts";
	}
	public String prior() {
		setCurrentAccount(accountDao.prior(currentCustomer,currentAccount));
		return "banks";
	}
	
	
	public void customers(int bankId) {
		MBUtils.redirect("customers.xhtml?id=" + bankId);
	}
	
	public Account getCurrentAccount() {
		Customer c = new Customer();
		c.setId(customerID);
		currentCustomer = customerDao.find(c);
		
		if (currentAccount == null)
			getAllAccount();
		return currentAccount;
	}
	
	public void setCurrentAccount(Customer costomer , int id) {
		this.currentAccount = accountDao.find(costomer,id);
		//System.out.println("currentBank=" + this.currentBank.getName());
	}
	
	public boolean isCurrentAccount(Account account) {
		return account.equals(currentAccount);
	}
	
	private void validate(Bank b) {
		if (b.getName().toLowerCase().contains("bernard madoff"))
			throw new RuntimeException("the name is forbidden!");
	}
	
	
	public List<Account> getAllAccount() {
	
		accounts = accountDao.getList(currentCustomer);
		
		if (accounts.isEmpty()) {
			accountDao.populate(currentCustomer);
			accounts = accountDao.getList(currentCustomer);
		}
		
		if (!accounts.isEmpty() && currentAccount == null)
			currentAccount = accounts.get(0);
		
		return accounts;
		
	}
	
	private Map<String, String> cityMap;
	
	
	/*public Map<String, String> getCities(){
		if (cityMap == null){
			cityMap = new HashMap();
			List<City> cities = cityDao.getList();
			cityMap.put("no city", "");
			for (City city : cities) 
				cityMap.put(city.getName(), Integer.toString(city.getId()));
		}
		return cityMap;
	}
	*/
	
	/*public String getCurrentBankCity() {
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
	*/
	/**
	 * create the session before the Facelet (/JSP) page is printed-----
	 */
	@PostConstruct
	void initialiseSession() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}
	
	
	
	
	
	

}
