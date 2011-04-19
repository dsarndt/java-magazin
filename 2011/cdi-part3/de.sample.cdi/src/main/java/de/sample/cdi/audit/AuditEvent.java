package de.sample.cdi.audit;

import de.sample.cdi.entities.Account;

public class AuditEvent {
	
	private Account account;
	
	public AuditEvent(Account account) {
		this.account = account;
	}
	
	public Account getAccount() {
		return account;
	}

}
