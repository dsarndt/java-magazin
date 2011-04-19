package de.sample.cdi.performance;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

@Interceptor
@Performance
public class PerformanceInterceptor {

	@Inject
	private Logger logger;

	@AroundInvoke
	public Object doPerformanceMeasurement(InvocationContext context)
			throws Exception {
		
		System.out.println("logger >>> " + logger);
		System.out.println("logger isDebugEnabled" + logger.isDebugEnabled());

		Object result = null;
		long start = System.currentTimeMillis();
		result = context.proceed();
		if (logger.isDebugEnabled()) {
			logger.debug(context.getMethod() + " done in "
					+ (System.currentTimeMillis() - start) + " ms");
		}
		return result;

	}

}
