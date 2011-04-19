package de.sample.cdi.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;

import de.sample.cdi.MoneyTransferDone;

@ApplicationScoped
public class AuditServiceBean implements AuditService {
	
	private List<MoneyTransferDone> moneyTransferDoneEvents;
	
	private List<AuditEvent> auditViolationEvents;
	
	@PostConstruct
	protected void init() {
		moneyTransferDoneEvents = Collections.synchronizedList(new ArrayList<MoneyTransferDone>());
		auditViolationEvents = Collections.synchronizedList(new ArrayList<AuditEvent>());
	}
	
	
	@SuppressWarnings("unused")
	private void onAuditEventViolations(@Observes @AuditViolation AuditEvent event) {
		System.out.println("recorded " + event.getClass().getName());
		auditViolationEvents.add(event);
	}
	
	@Override
	public List<AuditEvent> getAuditViolationEvents() {
		return auditViolationEvents;
	}
	
	
	
	@Override
	public List<MoneyTransferDone> getMoneyTransferDoneEvents() {
		return moneyTransferDoneEvents;
	}

	@SuppressWarnings("unused")
	private void onAuditEvent(@Observes(during=TransactionPhase.AFTER_SUCCESS ) MoneyTransferDone moneyTransferDone) {
		moneyTransferDoneEvents.add(moneyTransferDone);
	}	
	
}
