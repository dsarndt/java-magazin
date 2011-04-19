package de.sample.cdi.audit;

import java.util.List;

import de.sample.cdi.MoneyTransferDone;

public interface AuditService {
	
	public List<MoneyTransferDone> getMoneyTransferDoneEvents();
	
	public List<AuditEvent> getAuditViolationEvents();
	

}
