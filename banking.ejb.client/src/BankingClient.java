import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import tp.ejb.AccountDao;
import tp.ejb.CustomerDao;
import tp.ejb.test.TestFacadeRemote;
import tp.session.BankingRemote;
import domain.model.Account;
import domain.model.Bank;
import domain.model.City;
import domain.model.Customer;

public class BankingClient {

	
	/** Cette classe et pour les tests **/

	//private static final String APP_ = "banking-ejb";

	private TestFacadeRemote facadeBean;
	private AccountDao accountDao;
	private CustomerDao customerDao;
	private BankingRemote bankingSatefullBean_;

	private void log(String mesg) {
		System.out.println("  " + mesg);
	}

	private void testAccount(Customer c) {

		Account acnt0 = accountDao.createAccount(c, 100, 10);

		log("account created " + acnt0.getAccountNumber());

		Account acnt1 = accountDao.createAccount(c, 0, 0);
		log("account created " + acnt1.getAccountNumber());

		accountDao.creditAccount(acnt0, 1000);
		accountDao.creditAccount(acnt1, 10000);
		log("balance of " + acnt0.getAccountNumber() + " is "
				+ acnt0.getBalance());
		log("balance of " + acnt1.getAccountNumber() + " is "
				+ acnt1.getBalance());
		double totransfer = 500;
		log("transfer of " + totransfer + " from " + acnt0.getAccountNumber()
				+ " to " + acnt1.getAccountNumber());
		try {
			accountDao.transferAccount(acnt0, acnt1, totransfer);
			log("balance of " + acnt0.getAccountNumber() + " is "
					+ acnt0.getBalance());
			log("balance of " + acnt1.getAccountNumber() + " is "
					+ acnt1.getBalance());

		} catch (Exception e) {
			log("rolled back transfer of " + totransfer + " from "
					+ acnt0.getAccountNumber() + " to "
					+ acnt1.getAccountNumber() + " " + e.getMessage());
		}
	}

	public void test() throws Exception {
		// Initialisation de la base

		facadeBean.populate();
		bankingSatefullBean_.addBankName("crédit truc");
		bankingSatefullBean_.addBankName("crédit muche");

		List<String> bks = bankingSatefullBean_.getBankNames();
		for (String bk : bks) {
			log(bk);
		}

		log("Liste des communes");

		List<City> cities = facadeBean.getCities();
		if (cities != null) {
			for (City city : cities) {
				log(city.toString());
			}
		}

		City thesecond = facadeBean.getCities().get(1);

		// log("suppression de " + thesecond);
		// facadeBean.delete(thesecond);

		// facadeBean.delete(facadeBean.getCities().get(1));

		log("Liste des communes après la suppression de la seconde");

		cities = facadeBean.getCities();
		if (cities != null) {
			for (City city : cities) {
				log(city.toString());
			}
		}

		List<Bank> banks = facadeBean.getBanks();

		for (Bank bank : banks) {
			Collection<Customer> customers_ = customerDao.getList(bank);
			if (customers_.size() > 0) {
				log("Liste des clients pour la banque " + bank.getName() + " :");
				for (Customer customer : customers_) {
					log("    client: " + customer);
					// List<Account> cnts=customer.getAccounts();

					List<Account> cnts = accountDao.getList(customer);

					int siz = cnts.size();
					for (Account account : cnts) {
						log("        compte: " + account.getAccountNumber()
								+ " solde= " + account.getBalance());
					}
				}
			} else
				log("pas de clients pour " + bank.getName());
		}

		// Recuperation de la liste des Customers
		log("");
		log("Liste des clients + banque :");
		List<Customer> customers = facadeBean.getCustomers();
		if (customers != null && customers.size() > 0) {
			facadeBean.getCustomers().get(0)
					.setCity(facadeBean.getCities().get(0));
			facadeBean.save(facadeBean.getCustomers().get(0));
			for (Customer customer : customers) {
				log("client: " + customer.toString() + " /banque: "
						+ customer.getBank().getName());
			}
		}

		testAccount();

		// Customer c0 = facadeBean.getCustomers().get(0);
		// testAccount(c0);

		// suppression d'un client

		try {
			Customer del = facadeBean.getCustomers().get(1);
			log("suppression du client " + del);
			facadeBean.delete(del);
		} catch (Exception e) {
			log("erreur pendant la suppression du client");
		}

		if (facadeBean.getCustomers().size() > 0) {
			log("Liste des clients après la suppression");
			customers = facadeBean.getCustomers();
			for (Customer Customer : customers) {
				log(Customer.getName());
			}
		}
		// suppression d'une banque

		Bank toDelete = facadeBean.getBanks().get(1);
		log("suppression de la banque " + toDelete);
		facadeBean.delete(toDelete);

		log("Liste des banques après la suppression");
		banks = facadeBean.getBanks();
		if (banks != null) {
			for (Bank bank : banks)
				log(bank.getName());

		}

		customers = facadeBean.getCustomers();
		if (customers.size() > 0) {
			log("Liste des clients après la suppression de la banque");
			if (customers != null) {
				for (Customer Customer : customers) {
					log(Customer.getName());
				}
			}
		}

	}

	private void testAccount() {
		if (facadeBean.getCustomers().size() > 0) {
			Customer c0 = facadeBean.getCustomers().get(0);
			accountDao.testAccount(c0);
		}

	}

	public boolean createAllDao() {
		Properties jndiProps = new Properties();
		jndiProps.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jboss.naming.remote.client.InitialContextFactory");
		
		jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8080"); //wildfly
		//jndiProps.put(Context.PROVIDER_URL, "remote://localhost:4447"); //AS7.1

	
		jndiProps.put(Context.SECURITY_PRINCIPAL, "testUser");
		jndiProps.put(Context.SECURITY_CREDENTIALS, "test");
		// jndiProps.put(Context.SECURITY_CREDENTIALS, "test---0");
		jndiProps.put("jboss.naming.client.ejb.context", true);

		try {
			Context context = new InitialContext(jndiProps);

			facadeBean = (TestFacadeRemote) context
					.lookup("//BankingEjb/TestFacade!tp.ejb.test.TestFacadeRemote");

			accountDao = (AccountDao) context
					.lookup("//BankingEjb/AccountDaoBean!tp.ejb.AccountDao");
			customerDao = (CustomerDao) context
					.lookup("//BankingEjb/CustomerDaoBean!tp.ejb.CustomerDao");

			bankingSatefullBean_ = (BankingRemote) context
					.lookup("//BankingEjb/Banking!tp.session.BankingRemote");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

	
	public static void main(String[] args) {
		BankingClient tb = new BankingClient();
		try {
			if (tb.createAllDao())
				tb.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see java.lang.Object#Object()
	 */
	public BankingClient() {
		super();
	}

}