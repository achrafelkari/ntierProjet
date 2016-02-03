package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import tp.ejb.CityDaoBean;
import tp.ejb.CustomerDaoBean;
import components.CustomerView;
import domain.model.Account;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;

@ManagedBean(name = "customerWeb")
@SessionScoped
public class CustomerWeb implements Serializable {

	private static final long serialVersionUID = 1641687L;

	@EJB
	private CustomerDaoBean customerDao;

	@EJB
	private CityDaoBean cityDao;
	private Bank banque;
	
	private Account compte;
	

	@ManagedProperty(value = "#{bankWeb}")
	private BankWeb bankWeb;

	public BankWeb getBankWeb() {
		return bankWeb;
	}

	public void setBankWeb(BankWeb bankWeb) {
		this.bankWeb = bankWeb;
	}

	public Account getCompte() {
		return compte;
	}

	public void setCompte(Account compte) {
		this.compte = compte;
	}

	@ManagedProperty(value = "#{customerView}")
	private CustomerView customerView;

	public CustomerView getCustomerView() {
		return customerView;
	}

	public void setCustomerView(CustomerView customerView) {
		this.customerView = customerView;
	}

	private Map<String, String> cityMap;
	private int bankId;
	
	private Customer currentCustomer;
	private City currentCustomerCity;

	
	
	public void setCurrentCustomerCity(City currentCustomerCity) {
		this.currentCustomerCity = currentCustomerCity;
	}

	public Bank getBanque() {
		return banque;
	}

	public void setBanque(Bank banque) {
		this.banque = banque;
	}

	protected Object getBackingBean(String name) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
     	ValueExpression expression = app.getExpressionFactory()
				.createValueExpression(context.getELContext(), name,
						Object.class);
		Object result = null;
		try {
			result = expression.getValue(context.getELContext());
		} catch (ELException e) {
			System.out.println("error " + e.toString());
		}
		return result;
	}

	public void menuValueChanged(ValueChangeEvent vce) {
		String oldValue = vce.getOldValue().toString(), newValue = vce
				.getNewValue().toString();
		System.out.println("Value changed from "
				+ (oldValue.isEmpty() ? "null" : oldValue) + " to " + newValue);
		HtmlSelectOneMenu cityMenu = (HtmlSelectOneMenu) vce.getComponent();
		cityMenu = customerView.getCitymenu();
		System.out.println("citymenu=");
		List<UIComponent> childs = cityMenu.getChildren();
		for (UIComponent uiComponent : childs) {
			if (uiComponent instanceof UISelectItems) {
				UISelectItems uISelectItems = (UISelectItems) uiComponent;
				Map<String, Object> atts = (Map<String, Object>) uISelectItems
						.getValue();
				Set<String> keys = atts.keySet();
				for (String key : keys) {
					System.out.println("     " + key + "=" + atts.get(key));
				}
			}
		}
		HtmlInputText name = customerView.getName();
		System.out.println("name=" + name.getValue());
	}

	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	public void modify() {
		customerDao.merge(currentCustomer);
		//MBUtils.redirect("customers.xhtml?id=" + bankId);
	}

	public void next() {
		setCurrentCustomer(customerDao.next(getCurrentCustomer()));
	}

	public void prior() {
		setCurrentCustomer(customerDao.prior(getCurrentCustomer()));
	}

	public void accounts(int customerId) {
		MBUtils.redirect("accounts.xhtml?id=" + customerId);
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {  // ceci est la premiére fonction que la page customers va utilisé ! 
		
		Bank b = new Bank(); // on créée la baque 
		b.setId(bankId); // on leur donne notre id  // 
		bankWeb.setCurrentBank(b); // je cherche dans la base de données 
		this.bankId = bankId; // je redonne l'id en cas de modification ! 
		banque  = bankWeb.getCurrentBank(); // j'initie la banque avec la banque courante ! 
		getAllCustomers(); // je créer la liste des clients 
		
	}
	
	
	

	public Customer getCurrentCustomer() {
		if (currentCustomer == null
				|| !currentCustomer.getBank().equals(bankWeb.getCurrentBank()))
			getAllCustomers();
		return currentCustomer;
	}

	public List<Customer> getAllCustomers() { 
		
		//Bank b = bankWeb.getCurrentBank();
		List<Customer> customers = customerDao.getList(banque);
		int siz = customers.size();
		
		if (siz>0 && (currentCustomer == null || !currentCustomer.getBank().equals(banque))){
			currentCustomer = customers.get(0);
			currentCustomerCity = currentCustomer.getCity();
			
			List<Account> comptes = currentCustomer.getAccounts();
			
			if(comptes.size()!=0)
				compte = comptes.get(0);
			else
				compte = new Account();
			//bankId = bankWeb.getCurrentBank().getId();
		}
		return customers;
	}
	
	public String getSize(){
		return ""+ currentCustomer.getAccounts().size();
	}

	public String getRowstyle(Customer cust) {
		if (cust.equals(currentCustomer))
			return "list-row-even";
		else
			return "list-row-odd";
	}

	public String getCurrentCustomerCity() {
		if (currentCustomer==null)
			return "";
		City city = currentCustomer.getCity();
		if (city != null)
			return Integer.toString(currentCustomer.getCity().getId());
		else
			return "";
	}

	public void setCurrentCustomerCity(String sid) {
		if (currentCustomer==null)
			return;
		if (sid != null && !sid.isEmpty()) {
			City city = cityDao.find(Integer.parseInt(sid));
			getCurrentCustomer().setCity(city);
		}
	}

	public Map<String, String> getCities() {
		if (cityMap == null) {
			cityMap = new HashMap();
			List<City> cities = cityDao.getList();
			cityMap.put("no city", "");
			for (City city : cities) 
				cityMap.put(city.getName(), Integer.toString(city.getId()));
		}
		return cityMap;
	}

}
