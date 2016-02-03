
package domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries( {
	@NamedQuery(name = "allcities", query = "select c FROM City c"),
	@NamedQuery(name = "allcitiesbyid", query = "select c FROM City c where c.id = :cityId"),
	@NamedQuery(name = "allcitiesbyzipcode", query = "select c FROM City c where c.zipCode = :zipcode")
})


public class City implements Serializable{
	private static final long serialVersionUID = -5494809610279467399L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String zipCode;
	private String country;
	
	
	public City() {
	
	}
	
	@Override
	public String toString(){
		return id+";"+zipCode+";"+name+";"+country;
	}
	
	public City(String name, String country, String zipCode) {
		this.name = name;
		this.country = country;
		this.zipCode = zipCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof City))
			return false;
		City other =(City)obj;
		return other.id==this.id;
	}

	@Override
	public int hashCode() {
		return id;
	}	
}
