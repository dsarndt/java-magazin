package de.sample.cdi.debtlimit;

import de.sample.cdi.entities.Account;
import de.sample.cdi.entities.Money;

public class DebtLimitServiceBean implements DebtLimitService {

	@Override
	public boolean isExceeded(Account debitAccount, Money amount) {
		return debitAccount.getBalance().getAmount() < amount.getAmount();
	}

}
