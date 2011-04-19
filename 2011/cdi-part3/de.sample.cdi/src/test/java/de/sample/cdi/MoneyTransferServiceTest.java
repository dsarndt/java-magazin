package de.sample.cdi;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.cdi.beans.BeansDescriptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.sample.cdi.audit.AuditEvent;
import de.sample.cdi.audit.AuditService;
import de.sample.cdi.debtlimit.DebtLimitExceeded;
import de.sample.cdi.debtlimit.DebtLimitExceededEvent;
import de.sample.cdi.debtlimit.DebtLimitVerification;
import de.sample.cdi.entities.Account;
import de.sample.cdi.entities.Money;
import de.sample.cdi.entities.Transfer;
import de.sample.cdi.logging.LoggerFactory;
import de.sample.cdi.performance.Performance;
import de.sample.cdi.performance.PerformanceInterceptor;
import de.sample.cdi.tx.Transactional;
import de.sample.cdi.tx.TransactionalInterceptor;

@RunWith(Arquillian.class)
public class MoneyTransferServiceTest {

	@Deployment(testable = true)
	public static JavaArchive createTestArchive() {
		return ShrinkWrap
				//
				.create(JavaArchive.class, "moneytransfer.jar")
				//
				.addClasses(MoneyTransferService.class,//
						MoneyTransferServiceBean.class,//
						PerformanceInterceptor.class, //
						Performance.class,//
						TransactionalInterceptor.class,//
						Transactional.class,//
						LoggerFactory.class,//
						MoneyTransferDone.class)
				.addPackage("de.sample.cdi.entities")//				
				.addPackage("de.sample.cdi.debtlimit")//		
				.addPackage("de.sample.cdi.audit")//
				.addAsResource("log4j.properties")
				.addAsManifestResource(
						new StringAsset(Descriptors
								.create(BeansDescriptor.class)
								.decorator(DebtLimitVerification.class)
								.interceptor(PerformanceInterceptor.class)
								.interceptor(TransactionalInterceptor.class)
								.exportAsString()),
						ArchivePaths.create("beans.xml"))//
				.addAsManifestResource("persistence.xml",
						ArchivePaths.create("persistence.xml"));

	}

	@Inject
	MoneyTransferService moneyTransferService;

	@PersistenceContext
	EntityManager entityManager;

	@Resource
	UserTransaction tx;
	
	@Inject
	AuditService auditService;

	@Before
	public void init() throws Exception {
		
		assertNotNull(moneyTransferService);
		assertNotNull(tx);
		assertNotNull(entityManager);

		initAccount("111111", new Money(1000));
		initAccount("222222", new Money(500));

	}

	private void initAccount(String accountNumber, Money balance)
			throws Exception {
		tx.begin();
		Account account = new Account(accountNumber);
		account.withdraw(balance);
		entityManager.merge(account);
		tx.commit();
	}

	@Test
	public void shouldTransfer() throws Exception {

		Account from = entityManager.find(Account.class, "111111");
		assertEquals(new Money(1000), from.getBalance());

		Account to = entityManager.find(Account.class, "222222");
		assertEquals(new Money(500), to.getBalance());

		moneyTransferService.transfer(from, to, new Money(200));

		List<Transfer> history = moneyTransferService.getHistory(from);
		Transfer transfer = history.get(0);
		assertEquals(new Money(800), transfer.getFrom().getBalance());
		assertEquals(new Money(700), transfer.getTo().getBalance());
		
		MoneyTransferDone moneyTransferDone = auditService.getMoneyTransferDoneEvents().get(0);
		
		assertEquals("111111", moneyTransferDone.getAccount().getNumber());
		assertEquals(new Money(200), moneyTransferDone.getTransfer().getAmount());
		
		

	}
	
	@Test
	public void shouldDebtLimitExceeded() {
		
		Account from = entityManager.find(Account.class, "111111");
		assertEquals(new Money(1000), from.getBalance());

		Account to = entityManager.find(Account.class, "222222");
		assertEquals(new Money(500), to.getBalance());
		
		try {
			moneyTransferService.transfer(from, to, new Money(2000));
			fail(DebtLimitExceeded.class.getName() + " not thrown");
		} catch (DebtLimitExceeded dle) {
			assertEquals(new Money(2000), dle.getAmount());
			AuditEvent auditEvent = auditService.getAuditViolationEvents().get(0);
			assertTrue(auditEvent instanceof DebtLimitExceededEvent);
		}
		
	}

}
