package tp.ejb;

import java.util.List;

import javax.ejb.Remote;

import domain.model.Bank;
import domain.model.Customer;

@Remote
public interface CustomerDao {
	public List<Customer> getList(Bank b);
	public List<Customer> getList();
	public Customer add(Customer c);
	public Customer find(int id);
	public Customer delete(Customer c);
	public Customer clone(Customer c);
	public Customer clone(Bank b,Customer c);
	public Customer first(Bank b);
	public Customer last(Bank b);
	public Customer prior(Bank b,Customer c);
	public Customer next(Bank b,Customer c);
	public Customer create(Bank b);
	public Customer merge(Customer c);
	public Customer foobar(Bank b,Customer c);
	public Customer createRandom(Bank b);
	public Customer find(Bank b, int id);
	public Customer delete(Bank b, Customer c);
	public Customer merge(Bank b, Customer c);
	public List<Customer> populate(Bank b, int caze);
}
