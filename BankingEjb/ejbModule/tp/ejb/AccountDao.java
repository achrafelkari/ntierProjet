package tp.ejb;

import java.util.List;

import javax.ejb.Remote;

import domain.model.Account;
import domain.model.Customer;

@Remote
public interface AccountDao {
	
	List<Account> getList(Customer c);
	Account add(Customer c,Account a);
	Account find(Customer c,int id);
	Account delete(Customer c,Account a);
	Account first(Customer c);
	Account last(Customer c);
	Account prior(Customer c,Account a);
	Account next(Customer c,Account a);
	Account clone(Account b);
	Account create(Customer c);
	Account merge(Customer c, Account a);
	Account foobar(Account a);
	Account createRandom(Customer c);
	List<Account> populate(Customer c);
	
	Account createAccount(Customer c, double overdraftLimit, double overdraftPenalty);
	void creditAccount(Account account, double amount);
	void withdrawAccount(Account account, double amount);
	void transferAccount(Account from, Account to, double amount);
	Account find(int id) ;
    void testAccount(Customer customer);
    
    
    /// Le code ajouté 
    
    Account createAccount(Customer c, double overdraftLimit, double overdraftPenalty,double solde);
	
}
