package de.sample.cdi.debtlimit;

import java.util.List;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import de.sample.cdi.MoneyTransferService;
import de.sample.cdi.audit.AuditEvent;
import de.sample.cdi.audit.AuditViolation;
import de.sample.cdi.entities.Account;
import de.sample.cdi.entities.Money;
import de.sample.cdi.entities.Transfer;

@Decorator
public abstract class DebtLimitVerification implements MoneyTransferService {
	
	@Inject
	DebtLimitService debtLimitService;
	
	@Inject
	@Delegate
	@Any
	MoneyTransferService delegate;
	
	@Inject
	Event<AuditEvent> auditEventProducer;

	@SuppressWarnings("serial")
	@Override
	public void transfer(Account from, Account to, Money amount) {
		if(debtLimitService.isExceeded(from, amount)) {
			auditEventProducer
				.select(new AnnotationLiteral<AuditViolation>() {})
				.fire(new DebtLimitExceededEvent(from));
			
			throw new DebtLimitExceeded(from, amount);
		}
		delegate.transfer(from, to, amount);
	}

	@Override
	public List<Transfer> getHistory(Account accout) {
		return delegate.getHistory(accout);
	}

}
