package de.sample.cdi.debtlimit;

import de.sample.cdi.entities.Account;
import de.sample.cdi.entities.Money;

public interface DebtLimitService {
	
	public boolean isExceeded(Account debitAccount, Money amount);

}
