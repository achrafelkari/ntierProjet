package tp.ejb.test;


import java.util.List;

import javax.ejb.Remote;

import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;




@Remote
public interface TestFacadeRemote {
	   List<Customer> getCustomers(); 
	   List<Bank> getBanks();
	   List<City> getCities();
	   void delete(Bank b);
	   void delete(Customer c);
	   void delete(City c);
	   void populate();
	   void save(Customer customer);
}
