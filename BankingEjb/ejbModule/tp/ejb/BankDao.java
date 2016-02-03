package tp.ejb;

import java.util.List;

import javax.ejb.Remote;

import domain.model.Bank;

@Remote
public interface BankDao {
	public List<Bank> getList();
	public Bank add(Bank b);
	public Bank find(int id);
	public Bank delete(Bank b);
	public Bank first();
	public Bank last();
	public Bank prior(Bank b);
	public Bank next(Bank b);
	public Bank clone(Bank b);
	public Bank create();
	public Bank merge(Bank b);
	public Bank foobar(Bank b);
	public Bank createRandom();
	public Boolean isSame(Bank a, Bank b);
	public List<Bank> populate();
}
