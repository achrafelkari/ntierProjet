package tp.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.mapping.Array;

import tp.ejb.utils.Paging;
import domain.model.Account;
import domain.model.Transfer;

@Stateless
@LocalBean
public class TransferDaoBean implements TransferDao, Serializable {
	
	private static final long serialVersionUID = 2233690057693275189L;

	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;
	

	@EJB
	private AccountDao accountDao;

	public TransferDaoBean() {
		super();
		System.out.println("creating TransferDaoBean");
	}
	@Override
	public Transfer createTransfer(Account account, Account other, String date,double amount) {
		Transfer t = create(account,other);
	    t.setAmount(amount);
	    t.setDate(date);
	    account.setBalance(account.getBalance() + amount);
		other.setBalance(other.getBalance() - amount);  //a revoir, agréger l'historique
		em.merge(account);
		em.merge(other);
	    em.persist(t);
		return t;	
	}
	
	@Override
	public void testTransfer(Account ownerAccount, Account otherAccount) {
	//	account = accountDao.getList(account.getOwner()).get(0);
		Transfer t = createTransfer(ownerAccount, otherAccount);
		t.setAmount(100);
		System.out.println("transfer created " + t.getAccount()+" amount="+t.getAmount());	
	}

	
	
	@Override
	public Transfer createTransfer(Account ownerAccount, Account otherAccount) {
		Transfer t = create(ownerAccount, otherAccount);
		t.setDate(new Date().toString());
		t.setAmount(0.0);
		em.persist(t);
		return t;
	}

	@Override
	public List<Transfer> getList(Account a) {
		return em.createNamedQuery("alltransfersbyaccount").setParameter("account", a).getResultList();
	}

	@Override
	public Transfer add(Account a, Transfer t) {
		a.addTransfer(t);
		if (find(a,t.getId()) == null) {
			em.merge(t);
			em.merge(a);
			return t;
		} else
			return null;
	}

	@Override
	public Transfer find(Account a,int id) {
		for (Transfer t : getList(a)) {
			if (t.getId() == id)
				return t;
		}
		return null;
	}

	@Override
	public Transfer delete(Account a, Transfer t) {
		List<Transfer> l = getList(a);
		Transfer result = Paging.prior(l, t);
		if (result == null)
			result = Paging.next(l, t);
		a.removeTransfer(t);
		em.merge(t);
		em.merge(a);
		return result;
	}

	@Override
	public Transfer first(Account a) {
		List<Transfer> l = getList(a);
		if (l.size() > 0)
			return l.iterator().next();
		else
			return null;
	}

	@Override
	public Transfer last(Account a) {
		List<Transfer> l = getList(a);
		if (l.size() > 0)
			return l.get(l.size() - 1);
		else
			return null;
	}
	
	@Override
	public Transfer prior(Account a, Transfer t) {
		return Paging.prior(getList(a), t);
	}

	@Override
	public Transfer next(Account a, Transfer t) {
		return Paging.next(getList(a), t);
	}

	@Override
	public Transfer create(Account a,Account other) {
		Transfer transfer = new Transfer();
		transfer.setAccount(a);
		transfer.setOtherAccount(other);
		transfer.setAmount(0.0);
		transfer.setDate(new Date().toString());
	    a.addTransfer(transfer);
	    em.persist(transfer);
		em.merge(transfer);
		em.merge(a);
		return transfer;
	}

	@Override	
	public Transfer merge(Account a, Transfer t) {
		if (find(a,t.getId()) != null) {
			em.merge(a);
			return t;
		} else
			return null;
	}
	@Override
	public Transfer foobar(Transfer a) {
		boolean tb = true;
		return a;
	}
	public static Random randomGenerator = new Random();
	@Override
	public Transfer createRandom(Account account, Account other) {
		Transfer transfer = create(account, other);
		Date d = new Date();
		d.setMonth(randomGenerator.nextInt(11)+1);
		transfer.setDate(d.toString()); //
		transfer.setAmount(randomGenerator.nextInt(1000));
		em.persist(transfer);
		return transfer;
	}
	@Override
	public List<Transfer> populate(Account account, Account other) {
		/*for (int i =0; i<randomGenerator.nextInt(10);i++)
		   createRandom(account,other);
		return new ArrayList<Transfer>(account.getTransfers());*/
		return new ArrayList<Transfer>();
	}
	
}
