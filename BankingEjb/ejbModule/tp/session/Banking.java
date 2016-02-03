package tp.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tp.ejb.AccountDao;
import tp.ejb.CityDao;
import tp.ejb.CustomerDao;
import domain.model.Account;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;

/**
 * Session Bean implementation class Banking
 */
@Stateful
@LocalBean
public class Banking implements BankingRemote {


	@PersistenceContext
	private EntityManager em;	
	
	@EJB
	private CityDao citydao;
	
	@EJB
	private AccountDao accountDao;
	
	@EJB
	private CustomerDao customerDao;
	
	
	
    private List<String> banks;    
    
    public Banking(){
    	banks = new ArrayList<String>();
    }
  
    @Override
    public void addBankName(String bookName) {
    	banks.add(bookName);
    }    
    @Override
    public List<String> getBankNames() {
       return banks;
    }
    
	
	private void log(String mesg){
		System.out.println("s*  "+mesg);
	}
	
	@Override
	public Bank find(Bank b) {
		return em.find(Bank.class, b.getId());
	}

	@Override
	public void add(Bank b) {
		if (find(b) == null)
			em.persist(b);
	}

	@Override
	public void populate() {
		
		City c1 = citydao.create();
		c1.setName("Nîmes");
		c1.setCountry("France");
		c1.setZipCode("30000");
		citydao.merge(c1);
		

		City c2 = new City("Montpellier", "34000", "France");
		em.persist(c2);
		City c3 = new City("Alès", "30000", "France");
		em.persist(c3);
		
		
		List<Bank> bqs = getBanks();
		if (bqs.size() > 2)
			return;

		Bank b = new Bank("Credit Arboricole", "3 rue des sapins", "75000",
				"5646456");
		em.persist(b);

		
		Customer ca=new Customer("Dupont", "Jean", "10, rue des lilas",
		"30000");
		
		ca.setCity(c1);
		em.persist(ca);
		
		List<Customer> custs=b.getCustomers();
		b.addCustomer(ca);

		b.addCustomer(new Customer("Poulain", "Amélie", "20, rue des mimosas",
				"34000"));

		em.persist(b);

		b = new Bank("Internet Bank", "10, avenue de la Californie", "13000",
				"5646456");
		b.addCustomer(new Customer("Durand", "Jacques",
				"10, rue des violettes", "32000"));

		b.addCustomer(new Customer("Harry", "Cover", "20, rue des salsifis",
				"31000"));

		em.persist(b);

		b = new Bank("Société Géniale", "1, parvis de la Défonse", "92400",
				"5646456");

		
		// should be done by the server
		em.joinTransaction();
		
		 List<Bank>  banks=getBanks();
		 for (Bank bank : banks) {
			log(bank.toString());
			List<Customer> customers =bank.getCustomers();
			for (Customer customer : customers) {
				log(customer.toString());
				 List<Account> accounts=customer.getAccounts();
				 for (Account account : accounts) { //il faudrait utiliser les DAO et ne pas taper directement dans les entity
					 try {
						 log("compte "+account.getAccountNumber()+" solde="+account.getBalance());
					} catch (Exception e) {
						 log("erreur pendant l'interrogation du compte, la banque "+bank.getName()+" vous présente ses excuses");
					}
					 
				}
			}
		 }
		
		
	}


	
	@Override
	public void test()  {
		// Initialisation de la base
		

		populate();
	    addBankName("crédit truc");
	    addBankName("crédit muche");
		
		List<String> bks =getBankNames();
		for (String bk : bks) {
			log(bk);
		}
		

		log("Liste des communes");

		List<City> cities = getCities();
		if (cities != null) {
			for (City city : cities) {
				log(city.toString());
			}
		}

		City thesecond = getCities().get(1);
		
		log("suppression de " + thesecond);
		delete(thesecond);

		// delete(getCities().get(1));

		log("Liste des communes après la suppression de la seconde");

		cities = getCities();
		if (cities != null) {
			for (City city : cities) {
				log(city.toString());
			}
		}

		List<Bank> banks = getBanks();

		for (Bank bank : banks) {
			Collection<Customer> customers_ = customerDao.getList(bank);
			if (customers_.size() > 0) {
				log("Liste des clients pour la banque " + bank.getName() + " :");
				for (Customer customer : customers_) {
					log("    client: " + customer);
					// List<Account> cnts=customer.getAccounts();

					List<Account> cnts = accountDao.getList(customer);

					int siz = cnts.size();
					for (Account account : cnts) {
						log("        compte: " + account.getAccountNumber()
								+ " solde= " + account.getBalance());
					}
				}
			} else
				log("pas de clients pour " + bank.getName());
		}

		// Recuperation de la liste des Customers
		log("");
		log("Liste des clients + banque :");
		List<Customer> customers = getCustomers();
		if (customers != null && customers.size() > 0) {
			getCustomers().get(0)
					.setCity(getCities().get(0));
			save(getCustomers().get(0));
			for (Customer customer : customers) {
				log("client: " + customer.toString() + " /banque: "
						+ customer.getBank().getName());
			}
		}

		testAccount();

		// Customer c0 = getCustomers().get(0);
		// testAccount(c0);

		// suppression d'un client

		try {
			Customer del = getCustomers().get(1);
			log("suppression du client " + del);
			delete(del);
		} catch (Exception e) {
			log("erreur pendant la suppression du client");
		}

		if (getCustomers().size() > 0) {
			log("Liste des clients après la suppression");
			customers = getCustomers();
			for (Customer Customer : customers) {
				log(Customer.getName());
			}
		}
		// suppression d'une banque

		Bank toDelete = getBanks().get(1);
		log("suppression de la banque " + toDelete);
		delete(toDelete);

		log("Liste des banques après la suppression");
		banks = getBanks();
		if (banks != null) {
			for (Bank bank : banks)
				log(bank.getName());

		}

		customers = getCustomers();
		if (customers.size() > 0) {
			log("Liste des clients après la suppression de la banque");
			if (customers != null) {
				for (Customer Customer : customers) {
					log(Customer.getName());
				}
			}
		}

	}

	
	
	private void testAccount() {
		if (getCustomers().size() > 0) {
			Customer c0 = getCustomers().get(0);
			accountDao.testAccount(c0);
		}

	}


	private void testAccount(Customer c) {

		Account acnt0 = accountDao.createAccount(c, 100, 10);

		log("account created " + acnt0.getAccountNumber());

		Account acnt1 = accountDao.createAccount(c, 0, 0);
		log("account created " + acnt1.getAccountNumber());

		accountDao.creditAccount(acnt0, 1000);
		accountDao.creditAccount(acnt1, 10000);
		log("balance of " + acnt0.getAccountNumber() + " is "
				+ acnt0.getBalance());
		log("balance of " + acnt1.getAccountNumber() + " is "
				+ acnt1.getBalance());
		double totransfer = 500;
		log("transfer of " + totransfer + " from " + acnt0.getAccountNumber()
				+ " to " + acnt1.getAccountNumber());
		try {
			accountDao.transferAccount(acnt0, acnt1, totransfer);
			log("balance of " + acnt0.getAccountNumber() + " is "
					+ acnt0.getBalance());
			log("balance of " + acnt1.getAccountNumber() + " is "
					+ acnt1.getBalance());

		} catch (Exception e) {
			log("rolled back transfer of " + totransfer + " from "
					+ acnt0.getAccountNumber() + " to "
					+ acnt1.getAccountNumber() + " " + e.getMessage());
		}
	}


	/**
	 * @return la liste des Banks
	 */
	@SuppressWarnings("unchecked")
	public List<Bank> getBanks() {
		return em.createNamedQuery("allbanks").getResultList();
	}

	/**
	 * @return la liste des Customers
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> getCustomers() {
		return em.createNamedQuery("allcustomers").getResultList();
	}

	@Override
	public void delete(Bank b) {
		// TODO vérifier si c'est nécessaire de supprimer les customers d'une
		// banque

		b = em.find(Bank.class, b.getId());
		/*
		 * Collection<Customer> bcusts = b.getCustomers(); //est-ce nécessaire ??
		 * for (Customer customer : bcusts) { em.remove(customer); }
		 */
		em.remove(b);
	}

	@Override
	public void delete(Customer c) {
		c = em.find(c.getClass(), c.getId());
		c.getBank().getCustomers().remove(c);
		em.remove(c);
	}

	@Override
	public void delete(City c) {
		c = em.find(c.getClass(), c.getId());
		em.remove(c);
	}

	@Override
	public List<City> getCities() {
		return em.createNamedQuery("allcities").getResultList();
	}

	@Override
	public void save(Customer customer) {
		em.merge(customer);
		em.joinTransaction();
	}

	

    
    
}
