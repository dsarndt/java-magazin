package de.sample.cdi;

import de.sample.cdi.audit.AuditEvent;
import de.sample.cdi.entities.Transfer;

public class MoneyTransferDone extends AuditEvent {

	private Transfer transfer;

	public MoneyTransferDone(Transfer transfer) {
		super(transfer.getFrom());
		this.transfer = transfer;
	}

	public Transfer getTransfer() {
		return transfer;
	}

}
