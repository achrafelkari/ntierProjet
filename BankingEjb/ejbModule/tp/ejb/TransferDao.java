package tp.ejb;

import java.util.List;

import domain.model.Account;
import domain.model.Transfer;

public interface TransferDao {

	Transfer createTransfer(Account account, Account other, String date, double amount);
	List<Transfer> populate(Account account, Account other);
	Transfer createRandom(Account account, Account other);
	Transfer merge(Account a, Transfer t);
	Transfer create(Account a, Account other);
	Transfer next(Account a, Transfer t);
	Transfer prior(Account a, Transfer t);
	Transfer last(Account a);
	Transfer first(Account a);
	Transfer delete(Account a, Transfer t);
	Transfer find(Account a, int id);
	Transfer add(Account a, Transfer t);
	List<Transfer> getList(Account a);
	Transfer createTransfer(Account ownerAccount, Account otherAccount);
	void testTransfer(Account ownerAccount, Account otherAccount);
	Transfer foobar(Transfer a);

}
