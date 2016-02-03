package tp.ejb.test;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.model.Account;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;
import tp.ejb.CityDao;

//import org.hibernate.collection.internal.PersistentBag;



/**
 * Bean sans etat assurant le dialogue cote serveur avec les beans entites .
 * 
 * @author pfister
 */
@Stateless
@LocalBean
public class TestFacade implements TestFacadeRemote {

	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;	
	
	@EJB
	CityDao citydao;
	
	
	private void log(String mesg){
		System.out.println("s*  "+mesg);
	}
	
	
	public Bank find(Bank b) {
		return em.find(Bank.class, b.getId());
	}

	public void add(Bank b) {
		if (find(b) == null)
			em.persist(b);
	}

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

		Bank b = new Bank("Credit Arbor", "3 rue des sapins", "75000",
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

	public void delete(Customer c) {
		c = em.find(c.getClass(), c.getId());
		c.getBank().getCustomers().remove(c);
		em.remove(c);
	}

	public void delete(City c) {
		c = em.find(c.getClass(), c.getId());
		em.remove(c);
	}

	public List<City> getCities() {
		return em.createNamedQuery("allcities").getResultList();
	}

	public void save(Customer customer) {
		em.merge(customer);
		em.joinTransaction();	
	}

}