package tp.ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;





import javax.persistence.PersistenceContext;

import domain.model.City;
import tp.ejb.utils.Paging;

@Stateless
@LocalBean
public class CityDaoBean implements CityDao {


	@PersistenceContext(unitName = "BankingPU")
	private EntityManager em;


	public CityDaoBean() {
		super();
		System.out.println("creating CityDaoBean");
	}

	public City add(City c) {
		if (find(c) == null) {
			em.persist(c);
			return c;
		} else
			return c;
	}

	public City create() {
		City c = new City();
		c.setName("noname");
		em.persist(c);
		return c;
	}

	public City delete(City c) {
		City result = null;
		c = find(c); //pour avoir un bean rattaché
		if (c!= null) {
			result = prior(c);
			if (result == null)
				result = next(c);
			em.remove(c);
		}
		return result;
	}

	public City find(int id) {
		return em.find(City.class, id);
	}

	public City find(City c) {
		return em.find(City.class, c.getId());
	}

	/**
	 * retourne la première City correspondant au code postal passé en paramètre
	 */
	public City find(String zipcode) {
		return (City) (em.createNamedQuery("allcitiesbyzipcode")
				.setParameter("zipcode", zipcode).getResultList().get(0));
	}

	public City first() {
		return getList().get(0);
	}

	@SuppressWarnings("unchecked")
	public List<City> getList() {
		return em.createNamedQuery("allcities").getResultList();
	}

	public City last() {
		List<City> cities = getList();
		return cities.get(cities.size() - 1);
	}

	public City merge(City c) {
		return em.merge(c);
	}

	public City next(City c) {
		return Paging.next(getList(), c);
	}

	
	
	public List<City> populate() {
		City c1 = new City("Nimes", "30000", "France");
		add(c1);
		City c2 = new City("Montpellier", "34000", "France");
		add(c2);
		City c3 = new City("Ales", "30100", "France");
		add(c3);
		City c4 = new City("Caen", "14000", "France");
		add(c4);
		City c5 = new City("Marseille", "13000", "France");
		add(c5);
		City c6 = new City("Belfort", "90000", "France");
		add(c6);
		City c7 = new City("Strasbourg", "67000", "France");
		add(c7);
		City c8 = new City("Paris", "75000", "France");
		add(c8);
		return getList();
	}

	public City prior(City c) {
		return Paging.prior(getList(), c);
	}

	public Boolean isSame(City a, City b) {
		return a.getId() == b.getId();
	}

}
