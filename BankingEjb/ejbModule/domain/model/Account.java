package domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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

import org.hibernate.annotations.IndexColumn;

@Entity
@NamedQueries({
  @NamedQuery(name = "allaccounts", query = "select a FROM Account a"),
  @NamedQuery(name = "allaccountsbycustomer", query = "select a FROM Account a where a.owner = :owner")
})
public class Account implements Serializable {
	
	private static final long serialVersionUID = 6067819559697622964L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String accountNumber;
	private double solde;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Customer owner;
	
	private double balance;
	private double interestRate;
	private double overdraftPenalty;
	private double overdraftLimit;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Customer getOwner() {
		return owner;
	}
	public void setOwner(Customer owner) {
		this.owner = owner;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public double getOverdraftPenalty() {
		return overdraftPenalty;
	}
	public void setOverdraftPenalty(double overdraftPenalty) {
		this.overdraftPenalty = overdraftPenalty;
	}
	public double getOverdraftLimit() {
		return overdraftLimit;
	}
	public void setOverdraftLimit(double overdraftLimit) {
		this.overdraftLimit = overdraftLimit;
	}
	public Account(String accountNumber, Customer owner,
			double balance, double interestRate, double overdraftPenalty,
			double overdraftLimit,double solde) {
		super();
		this.accountNumber = accountNumber;
		this.owner = owner;
		this.balance = balance;
		this.interestRate = interestRate;
		this.overdraftPenalty = overdraftPenalty;
		this.overdraftLimit = overdraftLimit;
		this.solde = solde;
	}
	
	public Account() {
		super();
	}
	
	
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@IndexColumn(name = "id")
	private List<Transfer> transfers;

	
	
	public List<Transfer> getTransfers() {
		return transfers;
	}
	
	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}
	
	public void addTransfer(Transfer t) {
		transfers.add(t);	
	}
	
	public void removeTransfer(Transfer t) {
		transfers.remove(t);	
	}

	
	
	public double getSolde() {
		return solde;
	}
	public void setSolde(double solde) {
		this.solde = solde;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", accountNumber=" + accountNumber
				+ ", owner=" + owner + ", balance=" + balance
				+ ", interestRate=" + interestRate + ", overdraftPenalty="
				+ overdraftPenalty + ", overdraftLimit=" + overdraftLimit + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
	
}
