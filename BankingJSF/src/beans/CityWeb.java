package beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tp.ejb.CityDaoBean;
import domain.model.City;

@ManagedBean(name = "cityWeb")
@SessionScoped
public class CityWeb implements Serializable {

	private static final long serialVersionUID = 1641645687L;


	private Map<String, String> cityMap;
	private Map<String, String> revCityMap;

	@EJB
	private CityDaoBean cityDao;
	
	public Map<String, String> getCities(){
		if (cityMap == null){
			cityMap = new HashMap();
			revCityMap = new HashMap();
			List<City> cities = cityDao.getList();
			for (City city : cities) {
				cityMap.put(city.getName(), Integer.toString(city.getId()));
				revCityMap.put(Integer.toString(city.getId()), city.getName());
			}		
		}
		return cityMap;
	}
	
	




	




}
