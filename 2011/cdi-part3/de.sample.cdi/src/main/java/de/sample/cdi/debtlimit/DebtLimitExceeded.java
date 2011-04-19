package de.sample.cdi.debtlimit;

import de.sample.cdi.entities.Account;
import de.sample.cdi.entities.Money;

public class DebtLimitExceeded extends RuntimeException {
	

	private static final long serialVersionUID = 4193355516300068042L;
	
	private Account debitAccount;
	
	private Money amount;

	public DebtLimitExceeded(Account debitAccount, Money amount) {
		this.debitAccount = debitAccount;
		this.amount = amount;
	}
	
	public Account getDebitAccount() {
		return debitAccount;
	}
	
	public Money getAmount() {
		return amount;
	}

}
