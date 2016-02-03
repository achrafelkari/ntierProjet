package domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
  @NamedQuery(name = "alltransfers", query = "select t FROM Transfer t"),
  @NamedQuery(name = "alltransfersbyaccount", query = "select t FROM Transfer t where t.account = :account")
})

public class Transfer implements Serializable {
	
	private static final long serialVersionUID = 606781954564L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int id;
	private String date;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "other_account_id")
	private Account otherAccount;	
	private double amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Account getOtherAccount() {
		return otherAccount;
	}
	public void setOtherAccount(Account otherAccount) {
		this.otherAccount = otherAccount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Transfer(String date, Account account, double amount) {
		super();
		this.date = date;
		this.account = account;
		this.amount = amount;
	}
	
	public Transfer() {
		super();
	}
	
	@Override
	public String toString() {
		return "Transfer [id=" + id + ", date=" + date + ", account=" + account + ", amount=" + amount + "]";
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
		Transfer other = (Transfer) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
