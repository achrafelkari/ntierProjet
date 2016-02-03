package tp.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import domain.model.Account;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;
import tp.ejb.utils.Paging;
import tp.ejb.utils.RandomString;

@Stateless
@LocalBean
public class CustomerDaoBean implements CustomerDao, Serializable {

	private static final long serialVersionUID = -5443611226894567253L;

	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;

	
	@EJB
	private CityDao  cityDao;
	
	
	public CustomerDaoBean() {
		super();
		System.out.println("creating CustomerDaoBean");
	}

	public Customer find(Customer c) {
		return em.find(Customer.class, c.getId());
	}

	public Customer add(Customer c) {
		if (find(c) == null) {
			em.persist(c);
			return c;
		} else
			return null;
	}

	private Customer copyCustomer(Customer c) {
		Customer newc = create(c.getBank());
		if (c.getAddress() != null)
			newc.setAddress(c.getAddress());
		if (c.getName() != null)
			newc.setName(c.getName());
		if (c.getForName() != null)
			newc.setForName(c.getForName());
		// if (b.getZipCode() != 0)
		newc.setZipCode(c.getZipCode());
		// copier aussi les comptes
		return newc;
	}

	public Customer clone(Customer c) {
		Customer newc = copyCustomer(c);
		return newc;
	}

	public Customer clone(Bank b, Customer c) {
		Customer newc = copyCustomer(c);
		return newc;
	}

	public Customer create() {
		Customer newc = new Customer("noname");
		em.persist(newc);
		return newc;
	}

	public Customer create(Bank b) {
		Customer newc = new Customer("noname");
		b.addCustomer(newc);
		em.persist(newc);
		return newc;
	}
	
	

	public Customer first() {
		List<Customer> bks = getList();
		return bks.get(0);
	}

	public Customer first(Bank b) {
		List<Customer> bks = getList(b);
		if (bks != null && bks.size() > 0)
			return bks.get(0);
		else
			return null;
	}

	private Random rand = new Random();

	public Customer makeRandom() {
		Customer c = new Customer();
		c.setName("Client " + RandomString.generateStringAlpha(10));
		c.setForName("Toto");
		c.setAddress(rand.nextInt(100) + " rue "
				+ RandomString.generateStringAlpha(10));
		c.setZipCode(Integer.toString(30000 + rand.nextInt(1000)));
		return c;
	}

	public Customer createRandom() {
		Customer c = makeRandom();
		em.persist(c);
		return c;
	}

	public Customer createRandom(Bank b) {
		Customer c = makeRandom();
		b.addCustomer(c);
		em.persist(c);
		return c;
	}

	public Customer delete(Customer c) {
		c = find(c.getId()); // on ne peut pas supprimer une entité
								// détachée
		Customer pr = prior(c);
		if (pr == null)
			pr = next(c);
		if (c.getBank() != null)
			c.getBank().removeCustomer(c);
		em.remove(c);
		return pr;
	}

	public Customer delete(Bank b, Customer c) {
		b = em.find(Bank.class, b.getId());
		c = find(c.getId()); // on ne peut pas supprimer une entité
								// détachée
		Customer pr = prior(b, c);
		if (pr == null)
			pr = next(b, c);

		c = find(c.getId());
		b.removeCustomer(c);
		em.remove(c);
		return pr;
	}

	public Customer find(int id) {
		return em.find(Customer.class, id);
	}

	public Customer find(Bank b, int id) {
		return em.find(Customer.class, id);
	}

	public Customer next(Customer c) {
		return Paging.next(getList(), c);
	}

	public Customer prior(Customer c) {
		return Paging.prior(getList(), c);
	}

	public Customer next(Bank b, Customer c) {
		return Paging.next(getList(b), c);
	}

	public Customer prior(Bank b, Customer c) {
		return Paging.prior(getList(b), c);
	}

	@SuppressWarnings("unchecked")
	public List<Customer> getList() {
		List<Customer> result = em.createNamedQuery("allcustomers")
				.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Customer> getList(Bank b) {
		List<Customer> result = em.createNamedQuery("allcustomersbybank")
				.setParameter("bank", b).getResultList();
		return result;
	}

	public Customer findById(int id) {
		return em.find(Customer.class, id);
	}

	public Customer findByEmail(String email) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
		Root<Customer> customer = criteria.from(Customer.class);
		criteria.select(customer).where(cb.equal(customer.get("email"), email));
		return em.createQuery(criteria).getSingleResult();
	}

	public List<Customer> findAllOrderedByName() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
		Root<Customer> customer = criteria.from(Customer.class);
		criteria.select(customer).orderBy(cb.asc(customer.get("name")));
		return em.createQuery(criteria).getResultList();
	}

	public Customer foobar(Customer c) {
		return c;
	}

	public Customer foobar(Bank b, Customer c) {
		return c;
	}

	public Customer merge(Customer c) {
		if (find(c) != null) {
			em.merge(c);
			return c;
		} else
			return null;
	}

	public Customer merge(Bank b, Customer c) {
		if (!c.getBank().equals(b))
			throw new RuntimeException("not the right bank!");
		return merge(c);
	}

	public Customer last(Bank b) {
		List<Customer> csts = getList(b);
		return csts.get(csts.size() - 1);
	}

	public Customer last() {
		List<Customer> csts = getList();
		return csts.get(csts.size() - 1);
	}

	public List<Customer> populate(Bank b, int caze) { // créer des obet pour les tests 

		List<City> cities = cityDao.getList();
		if (caze == 0) {
			Customer c = create(b);
			c.setName("Geek");
			c.setEmail("tot@gmail.com");
			c.setForName("Toto");
			c.setAddress("10, avenue de la Californie");
			c.setZipCode("13000");
			City ct = cities.get(0);
			c.setCity(ct);
			
			Account compte = new Account();
			compte.setAccountNumber("SVGHB5112DD");
			compte.setBalance(125451);
			compte.setId(574845);
			compte.setOwner(c);
			c.addAccount(compte);
			em.merge(c);
			em.merge(compte);

			System.out.println(c);

			c = create(b);
			c.setName("Foobar");
			c.setForName("Titi");
			c.setEmail("Foobar.TITI@homail.Com");
			c.setAddress("11, avenue du Nevada");
			c.setZipCode("14000");
			ct = cities.get(1);
			c.setCity(ct);
			
		/*	compte = new Account();
			compte.setId(6551);
			compte.setAccountNumber("VHBN65410FBH");
			compte.setBalance(2011500);
			compte.setOwner(c);
			c.addAccount(compte);
			*/
			
			
			
			em.merge(c);
			em.merge(compte);
			em.merge(b);

			System.out.println(c);
			
		} else if (caze == 1) {
			Customer c = create(b);
			c.setName("Taylor");
			c.setForName("Rich");
			c.setEmail("taylor@mines-ales.org");
			c.setAddress("10, avenue du Pérou");
			c.setZipCode("67000");
			City ct = cities.get(0);
			c.setCity(ct);

			em.merge(c);

			System.out.println(c);

			c = create(b);
			c.setName("Cover");
			c.setForName("Harry");
			c.setEmail("Harry");
			c.setAddress("11, Malecon Avenue");
			c.setZipCode("90000");
			 ct = cities.get(1);
			c.setCity(ct);
			em.merge(c);

			em.merge(b);

			System.out.println(c);
		
		}
		
		System.out.println("populating customers (case="+caze+") end ");

		return new ArrayList<Customer>(b.getCustomers());
	}


}
