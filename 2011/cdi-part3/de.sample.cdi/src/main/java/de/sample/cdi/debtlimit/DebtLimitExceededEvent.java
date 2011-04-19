package de.sample.cdi.debtlimit;

import de.sample.cdi.audit.AuditEvent;
import de.sample.cdi.audit.AuditViolation;
import de.sample.cdi.entities.Account;

@AuditViolation
public class DebtLimitExceededEvent extends AuditEvent {

	public DebtLimitExceededEvent(Account account) {
		super(account);
	}

}
