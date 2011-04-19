package de.sample.cdi.tx;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

@Interceptor
@Transactional
public class TransactionalInterceptor {
	
	@Resource
	UserTransaction tx;
	
	@AroundInvoke
	public Object doTransaction(InvocationContext context) throws Exception {
				
		Object result = null;
		
		boolean isActiveTransaction = tx.getStatus() == Status.STATUS_ACTIVE;
		try {
			if (!isActiveTransaction) {
				tx.begin();
			}
			result = context.proceed();
			if (!isActiveTransaction) {
				tx.commit();
			}	

		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
		return result;
	}
	
}
