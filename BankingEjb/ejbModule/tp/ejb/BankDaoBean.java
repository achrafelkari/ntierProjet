package tp.ejb;

import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import domain.model.Account;
import domain.model.Bank;
import domain.model.Customer;
import tp.ejb.utils.Paging;
import tp.ejb.utils.RandomString;

@Stateless
@LocalBean
public  class BankDaoBean implements BankDao {

	private static final long serialVersionUID = -5443611226894567253L;

	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;
	
	
	@EJB
	CustomerDao  customerDao;
	
	@EJB
	AccountDao accountDao;
	
	@EJB
	CityDao  cityDao;
	
	public Bank find(Bank b) {
		return em.find(Bank.class, b.getId());
	}

	public BankDaoBean() {
		super();
		System.out.println("creating BankDaoBean");
	}


	public Bank add(Bank b) {
		if (find(b) == null) {
			em.persist(b);
			return b;
		} else
			return null;
	}

	public Bank clone(Bank b) {
		Bank newb = new Bank(b.getName(), b.getAddress(), b.getZipCode(), b
				.getPhone());
		em.persist(newb);
		return newb;
	}

	public Bank create() {
		Bank newb = new Bank("noname");
		em.persist(newb);
		return newb;
	}

	/*
	public Bank save(Bank b) {
		em.merge(b);
		return b;
	}
	*/
	private Random rand = new Random();

	public Bank createRandom() {
		Bank b = new Bank();
		b.setName("Banque " + RandomString.generateStringAlpha(10));
		b.setAddress(rand.nextInt(100) + " rue "
				+ RandomString.generateStringAlpha(10));
		b.setPhone(rand.nextInt(100) + "." + rand.nextInt(100) + "."
				+ rand.nextInt(100) + "." + rand.nextInt(100) + "."
				+ rand.nextInt(100));
		b.setZipCode(Integer.toString(30000 + rand.nextInt(1000)));
		em.persist(b);
		return b;
	}

	public Bank delete(Bank b) {
		b = find(b); //pour avoir un bean rattaché
		Bank pr = prior(b);
		if (pr == null)
			pr = next(b);
		//TODO vérifier si c'est nécessaire de supprimer les customers d'une banque
		/*
		Collection<Customer> bcusts= b.getCustomers(); //est-ce nécessaire ??
		for (Customer customer : bcusts) {
			em.remove(customer);
		}	*/
		em.remove(b);
		return pr;
	}

	public Bank find(int id) {
		return em.find(Bank.class, id);
	}

	public Bank first() {
		List<Bank> bks = getList();
		return bks.get(0);
	}

	public Bank next(Bank b) {
		return Paging.next(getList(),b);
	}

	public Bank prior(Bank b) {
		return Paging.prior(getList(),b);
	}

	@SuppressWarnings("unchecked")
	public List<Bank> getList() {
		return em.createNamedQuery("allbanks").getResultList();
	}

	public Bank last() {
		List<Bank> bks = getList();
		return bks.get(bks.size() - 1);
	}



	public Bank foobar(Bank b) {
		// TODO Auto-generated method stub
		return b;
	}

	public Bank merge(Bank b) {
		if (find(b) != null) {
			em.merge(b);
			return b;
		} else
			return null;
	}

	public List<Bank> populate() {
		
		cityDao.populate();
		
		Bank b = create();
		b.setName("Crédit Arboricole");
		b.setPhone("123456");
		b.setAddress( "3, rue des sapins");
		b.setZipCode("75000");
		b.setCaisse((int)(Math.random()*10000));
		b.setBanque((int)(Math.random()*10000));
		em.merge(b);
		customerDao.populate(b, 0);
		
		b = create();
		b.setName("Internet Bank");
		b.setPhone("7890123");
		b.setAddress( "10, avenue de la Californie");
		b.setZipCode("13000");
		b.setCaisse((int)(Math.random()*10000));
		b.setBanque((int)(Math.random()*10000));
		em.merge(b);
		customerDao.populate(b, 1);
	

		b = create();
		b.setName("Société Géniale");
		b.setPhone("45698741");
		b.setAddress( "1, parvis de la Défonse");
		b.setZipCode("92400");
		b.setCaisse((int)(Math.random()*10000));
		b.setBanque((int)(Math.random()*10000));
		em.merge(b);


		for (int i = 0; i < 10; i++) {
			b = createRandom();
			b.setCaisse((int)(Math.random()*10000));
			b.setBanque((int)(Math.random()*10000));
			em.merge(b);
			
		}
		return getList();
	}

	public Boolean isSame(Bank a, Bank b) {
		return a!=null && b!=null && a.getId() == b.getId();
	}





}
