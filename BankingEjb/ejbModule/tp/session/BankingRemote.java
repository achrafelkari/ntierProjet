package tp.session;

import java.util.List;

import javax.ejb.Remote;

import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;

@Remote
public interface BankingRemote {
	void addBankName(String bookName);
	List<String> getBankNames();
	List<City> getCities();
	Bank find(Bank b);
	void add(Bank b);
	void populate();
	void delete(Bank b);
	void delete(Customer c);
	void delete(City c);
	void save(Customer customer);
	void test();
}
