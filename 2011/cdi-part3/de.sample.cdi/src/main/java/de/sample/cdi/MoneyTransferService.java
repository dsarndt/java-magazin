package de.sample.cdi;

import java.util.List;

import de.sample.cdi.entities.Account;
import de.sample.cdi.entities.Money;
import de.sample.cdi.entities.Transfer;

public interface MoneyTransferService {
	
	public void transfer(Account from, Account to, Money amount);
	
	public List<Transfer> getHistory(Account accout);

}
