package tp.ejb;

import java.util.List;

import javax.ejb.Remote;

import domain.model.City;


@Remote
public interface CityDao {
	public List<City> getList();
	public City add(City c);
	public City find(int id);
	public City find(String zipCode);
	public City find(City c);
	public City delete(City c);
	public City first();
	public City last();
	public City prior(City c);
	public City next(City c);
	public City create();
	public City merge(City c);
	public Boolean isSame(City a, City b);
	public List<City> populate();
}
