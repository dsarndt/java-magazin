package de.sample.cdi;

import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.sample.cdi.entities.Account;
import de.sample.cdi.entities.Money;
import de.sample.cdi.entities.Transfer;
import de.sample.cdi.performance.Performance;
import de.sample.cdi.tx.Transactional;

@Performance
public class MoneyTransferServiceBean implements MoneyTransferService {

	@PersistenceContext
	private EntityManager entityManager;

	 @Inject
	 @Any
	 private Event<MoneyTransferDone> moneyTransferDoneEventProducer;

	@Transactional
	public void transfer(Account from, Account to, Money amount) {

		from.deposit(amount);
		to.withdraw(amount);
		Transfer transfer = new Transfer(from, to, amount);
		this.entityManager.merge(to);
		this.entityManager.merge(from);
		this.entityManager.persist(transfer);
		this.moneyTransferDoneEventProducer.fire(new MoneyTransferDone(transfer));

	}

	@Override
	public List<Transfer> getHistory(Account account) {
		return entityManager.createNamedQuery("getTransfers", Transfer.class)//
				.setParameter("accountNumber", account.getNumber())//
				.getResultList();

	}

}
