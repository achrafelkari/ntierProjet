package beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import tp.ejb.AccountDaoBean;
import tp.ejb.BankDaoBean;
import tp.ejb.TransferDaoBean;
import domain.model.Account;
import domain.model.Bank;
import domain.model.Transfer;



@ManagedBean(name = "transferWeb")
@SessionScoped
public class TransferWeb implements Serializable {

	private static final long serialVersionUID = 541L;
	
	private int compteId;
	private int bankId;
	private Transfer transfer; 
	private String typeTransfer; 
	private String typeRetrait;
	private List<Transfer> allTransfer;
	private Account account;
	
	private double amount;
	
	private Bank currentBank;
	private Account currentAccount;
	
	@EJB
	private TransferDaoBean transferBean;
	@EJB
	private AccountDaoBean accountBean;
	@EJB 
	private BankDaoBean bankBean;
	
	@Resource
    private UserTransaction userTransaction;
	
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void transferer(){
		
		try{
		transfer = new Transfer(); // je cr��e le transfer 
		transfer.setAmount(amount); // set amount 
		Account compteBaque = new Account(); // je cr��e un compte pour la banque, car la banque n'a pas de compte (dans ma concption ! )
		compteBaque.setAccountNumber(currentBank.getNumeroCompteBaque());//
		compteBaque.setBalance(currentBank.getBanque() + currentBank.getCaisse());
		transfer.setAccount(currentAccount); // le premier compte sera celui du client ! 
		transfer.setOtherAccount(compteBaque); // le deuxi�me compte sera celui de la banque ! 
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
		Date date = new Date(); 
		transfer.setDate(dateFormat.format(date)); // je set la date 
		int debit = 1; // debit egal � 1 si d�bit et -1 si cr�dit ! 
		if("credit".equals(typeRetrait)) 
			debit = -1;
		if("cheque".equals(typeTransfer)){ // si on fait un virement ou une op�ration par cheque ! 
			double currentSolde = currentBank.getBanque(); // je prend le solde de la BANQUE ! 
			currentBank.setBanque(currentSolde + amount*debit);  // soit je d�bite soit je cr�dite sir le compte du client ! 
			currentAccount.setBalance(currentAccount.getBalance() -amount*debit); // on modifi la balance du compte du client ! 
		}else{// si on fait une op�ration par �sp�ce 
			double currentSolde = currentBank.getCaisse(); // alors on doit travailler avec la caisse et non la banque ! 
			currentBank.setCaisse(currentSolde + amount*debit); 
			currentAccount.setBalance(currentAccount.getBalance() - amount*debit);
		}
		currentAccount.addTransfer(transfer);
		bankBean.merge(currentBank);
		accountBean.merge(currentAccount);
		currentBank = bankBean.find(currentBank.getId());
		currentAccount = accountBean.find(currentAccount.getId());
		amount = 0 ; // r�initialisation de la variable amout  !
		}catch(Exception ex){/// ici si on catch une exception, on fait un rollback (retour en arri�re ! )
			 try {
				userTransaction.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e) {
				// si on a un probl�me de s�cirit� on est cuit XD ! 
				e.printStackTrace();
			}
		}
        MBUtils.redirect("transfer.xhtml?id=" + compteId + "&idBank="+bankId);
	}
	
	public String getTypeRetrait() {
		return typeRetrait;
	}

	public List<Transfer> getAllTransfer() {
		// je cr�er le compte du client ! 
		
		account = accountBean.find(compteId);
		
		allTransfer =new ArrayList<Transfer>();
				// transferBean.getList(account);
		return allTransfer;
	}
	
	

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public Bank getCurrentBank() {
		return currentBank;
	}

	public void setCurrentBank(Bank currentBank) {
		this.currentBank = currentBank;
	}

	public Account getCurrentAccount() { /// la premiere qu'on va faire 
		Account a = accountBean.find(compteId);
		Bank  b  = bankBean.find(bankId);
				
		currentBank =		b;
		currentAccount = a;
		
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

	public void setAllTransfer(List<Transfer> allTransfer) {
		this.allTransfer = allTransfer;
	}

	public void setTypeRetrait(String typeRetrait) {
		this.typeRetrait = typeRetrait;
	}

	public String getTypeTransfer() {
		return typeTransfer;
	}

	public void setTypeTransfer(String typeTransfer) {
		this.typeTransfer = typeTransfer;
	}

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public int getCompteId() {
		return compteId;
	}

	public void setCompteId(int compteId) {
		this.compteId = compteId;
	} 
	
	
	/**
	 * create the session before the Facelet (/JSP) page is printed-----
	 */
	@PostConstruct
	void initialiseSession() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
	}
}
