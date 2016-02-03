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

import domain.model.Account;
import domain.model.Customer;
import tp.ejb.utils.Paging;

@Stateless
@LocalBean
public class AccountDaoBean implements AccountDao, Serializable {
	
	private static final long serialVersionUID = 2233690057693275189L;

	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;
	

	@EJB
	CustomerDao customerDao;

	public AccountDaoBean() {
		super();
		System.out.println("creating AccountDaoBean");
	}
	
	public void testAccount(Customer customer) {
		customer = customerDao.getList().get(0);
		Account acnt0 = createAccount(customer ,100, 10);
		System.out.println("account created " + acnt0.getAccountNumber());
		Account acnt1 = createAccount(customer, 0, 0);
		System.out.println("account created " + acnt1.getAccountNumber());
		creditAccount(acnt0, 1000);
		creditAccount(acnt1, 10000);
		System.out.println("balance of " + acnt0.getAccountNumber() + " is "
				+ acnt0.getBalance());
		System.out.println("balance of " + acnt1.getAccountNumber() + " is "
				+ acnt1.getBalance());
		double totransfer = 500;
		System.out.println("transfer of " + totransfer + " from "
				+ acnt0.getAccountNumber() + " to " + acnt1.getAccountNumber());
		try {
			transferAccount(acnt0, acnt1, totransfer);
			System.out.println("balance of " + acnt0.getAccountNumber()
					+ " is " + acnt0.getBalance());
			System.out.println("balance of " + acnt1.getAccountNumber()
					+ " is " + acnt1.getBalance());

		} catch (Exception e) {
			System.out.println("rolled back transfer of " + totransfer + " from "
					+ acnt0.getAccountNumber() + " to "
					+ acnt1.getAccountNumber() + " "+ e.getMessage());
		}
	}

	public List<Account> getList(Customer c) {
		return em.createNamedQuery("allaccountsbycustomer").setParameter("owner", c).getResultList();
	}

	public Account add(Customer c, Account a) {
		c.addAccount(a);
		if (find(c,a.getId()) == null) {
			em.merge(a);
			em.merge(c);
			return a;
		} else
			return null;
	}

	public Account find(Customer c,int id) {
		for (Account a : getList(c)) {
			if (a.getId() == id)
				return a;
		}
		return null;
	}

	public Account find(int id) { // je cherche par l'id du compte!
		Account account = em.find(Account.class, id);
		return account;
	}
	
	public Account delete(Customer c,Account a) {
		List<Account> l = getList(c);
		Account result = Paging.prior(l, a);
		if (result == null)
			result = Paging.next(l, a);
		c.removeAccount(a);
		em.merge(a);
		em.merge(c);
		return result;
	}

	public Account first(Customer c) {
		List<Account> l = getList(c);
		if (l.size() > 0)
			return l.iterator().next();
		else
			return null;
	}

	public Account last(Customer c) {
		List<Account> l = getList(c);
		if (l.size() > 0)
			return l.get(l.size() - 1);
		else
			return null;
	}
	
	public Account prior(Customer c, Account a) {
		return Paging.prior(getList(c), a);
	}

	public Account next(Customer c, Account a) {
		return Paging.next(getList(c), a);
	}

	public Account clone(Account a) {
		Account account = create(a.getOwner());
		account.setBalance(a.getBalance());
		account.setOverdraftLimit(a.getOverdraftLimit());
		account.setOverdraftPenalty(a.getOverdraftPenalty());
		account.setInterestRate(a.getInterestRate());
		em.persist(account);
		return account;
	}

	public Account create(Customer c) {
		Account account = new Account();
		account.setOwner(c);
		account.setBalance(0);
		account.setOverdraftLimit(0);
		account.setOverdraftPenalty(0);
		account.setInterestRate(0);
	    c.addAccount(account);
	    em.persist(account);
		account.setAccountNumber(c.getId() + "-" + account.getId());
		em.merge(account);
		em.merge(c);
		return account;
	}

	public Account merge(Customer c, Account a) {
		if (find(c,a.getId()) != null) {
			em.merge(a);
			return a;
		} else
			return null;
	}
	public Account merge(Account a) {
		if (find(a.getId()) != null) {
			em.merge(a);
			return a;
		} else
			return null;
	}
	
	
	public Account foobar(Account a) {
		boolean tb = true;
		return a;
	}
	
	public static Random randomGenerator = new Random();
	
	
	public Account createRandom(Customer c) {
		Account account = create(c);
		account.setBalance(randomGenerator.nextInt(1000));
		account.setOverdraftLimit(randomGenerator.nextInt(100));
		account.setOverdraftPenalty(randomGenerator.nextInt(10));
		account.setInterestRate(randomGenerator.nextInt(10));
		em.persist(account);
		return account;
	}

	
	public List<Account> populate(Customer c) {
		for (int i =0; i<randomGenerator.nextInt(10);i++)
		   createRandom(c);
		return new ArrayList<Account>(c.getAccounts());
	}

	
	public Account createAccount(Customer c, double overdraftLimit,
			double overdraftPenalty) {
		Account account = create(c);
		account.setOverdraftLimit(overdraftLimit);
		account.setOverdraftPenalty(overdraftPenalty);
		em.persist(account);
		return account;
	}
	
	public Account createAccount(Customer c, double overdraftLimit,
			double overdraftPenalty,double solde) {

		Account account = create(c);
		account.setSolde(solde);
		account.setOverdraftLimit(overdraftLimit);
		account.setOverdraftPenalty(overdraftPenalty);
		em.persist(account);
		return account;
		
	}

	
	public void creditAccount(Account account, double amount) {
		if (amount <= 0)
			throw new RuntimeException("credit is null or negative");
		account.setBalance(account.getBalance() + amount);
		em.merge(account);
	}

	
	public void transferAccount(Account from, Account to, double amount) {
		if (amount <= 0)
			throw new RuntimeException("amount is null or negative");
		if (from.getBalance() - amount < -from.getOverdraftLimit())
			throw new RuntimeException("overdraft limit exceeded !");
		from.setBalance(from.getBalance() - amount);
		to.setBalance(to.getBalance() + amount);
		em.merge(from);
		em.merge(to);
	}

	
	public void withdrawAccount(Account account, double amount) {
		if (amount <= 0)
			throw new RuntimeException("debit is null or negative");
		if (account.getBalance() - amount < -account.getOverdraftLimit())
			throw new RuntimeException("overdraft limit exceeded !");
		account.setBalance(account.getBalance() - amount);
		em.merge(account);
	}

}
