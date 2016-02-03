package domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.constraints.Email;

@Entity
@NamedQueries({
		@NamedQuery(name = "allcustomers", query = "select c FROM Customer c"),
		@NamedQuery(name = "allcustomersbybank", query = "select c FROM Customer c where c.bank = :bank"),
		@NamedQuery(name = "allcustomersbybankid", query = "select c FROM Customer c where c.bank.id = :bankId")
		})

public class Customer implements Serializable {

	private static final long serialVersionUID = 7204194554910037494L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	
    @NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
    private String name;


   // @NotNull
    //@NotEmpty
	private String forName;
	
	
   // @NotNull
    //@NotEmpty
   // @Email
    private String email;

   // @NotNull
    //@Size(min = 10, max = 12)
    @Digits(fraction = 0, integer = 12)
    @Column(name = "phone_number")
    private String phoneNumber;
	
	
	private String address;
	private String zipCode;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@IndexColumn(name = "id")
	private List<Account> accounts;

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}	
	

	@ManyToOne
	@JoinColumn(name = "bank_id")
	private Bank bank;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}


	public void addAccount(Account account) {
		account.setOwner(this);
		accounts.add(account);
		if (accounts.size() > 0) {
			//System.out.println(accounts.size()+" accounts for customer " + this);
		} else
			;//System.out.println("no accounts for customer " + this);
	}

	public void removeAccount(Account account) {
		this.accounts.remove(account);
		account.setOwner(null);
	}

	public Customer(String name, String forName, String address, String zipCode) {
		this();
		this.name = name;
		this.forName = forName;
		this.address = address;
		this.zipCode = zipCode;

	}

	public Customer(String name) {
		this();
		this.name = name;
	}

	public Customer() {
		super();
		accounts = new ArrayList<Account>();
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

	public String getForName() {
		return forName;
	}

	public void setForName(String forName) {
		this.forName = forName;
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

	public String toString() {
		String result = id + " " + forName + " " + name;
		if (getCity() != null)
			result += ";" + getCity().toString();
		else
			result += ";city=null";
		return result;
	}

	public void setBank(Bank b) {
		this.bank = b;
	}

	public Bank getBank() {
		return bank;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Customer))
			return false;
		return this.id == ((Customer) other).id;
	}

	@Override
	public int hashCode() {
		return new Integer(id).hashCode();
	}
	
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
