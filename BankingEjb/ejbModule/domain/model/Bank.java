package domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQuery(name = "allbanks", query = "select b FROM Bank b")
public class Bank implements Serializable {

	private static final long serialVersionUID = 3498685794448352925L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String numeroCompteBaque;
	private String name;
	private String address;
	private String phone;
	private String zipCode;
	
	
	// J'ajoute les deux comptes de la banque : "Caisse" et "banque"
		
	
	private double caisse;
	
	private double banque;
	
	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@OneToMany(mappedBy = "bank", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Customer> customers;// = new ArrayList<Customer>();

     

	public void addCustomer(Customer customer) {
		customers.add(customer);
		customer.setBank(this);
	}

	public void addCustomer(final String name) {
		Customer nc= new Customer(name);
		customers.add(nc);
		nc.setBank(this);
	}

	public String toString() {
		String result = "";
		result = id + " " + name + " " + address + " " + zipCode + "\n";
		for (Iterator<Customer> i = customers.iterator(); i.hasNext();) {
			Customer cust = i.next();
			result += cust.toString() + "\n";

			result += "------------\n";
		}
		return result;
	}

	public Bank() {
		super();
		customers = new ArrayList<Customer>();
		numeroCompteBaque =  "NUMBANQUE" + id;
	}

	public Bank(String name) {
		this();
		this.name = name;
	}

	public Bank(String name, String adddress, String zipCode, String phone) {
		this();
		this.name = name;
		this.address = adddress;
		this.zipCode = zipCode;
		this.phone = phone;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Bank))
			return false;
		return ((Bank)other).id == id;
	}

	@Override
	public int hashCode() {
		return new Integer(id).hashCode();
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void removeCustomer(Customer c) {
		customers.remove(c);
	}

	public double getCaisse() {
		return caisse;
	}

	public void setCaisse(double caisse) {
		this.caisse = caisse;
	}

	public double getBanque() {
		return banque;
	}

	public void setBanque(double banque) {
		this.banque = banque;
	}

	public String getNumeroCompteBaque() {
		return numeroCompteBaque;
	}

	public void setNumeroCompteBaque(String numeroCompteBaque) {
		this.numeroCompteBaque = numeroCompteBaque;
	}
	
	

}
