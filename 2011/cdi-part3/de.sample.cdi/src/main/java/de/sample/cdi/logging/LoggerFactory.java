package de.sample.cdi.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.log4j.Logger;

public class LoggerFactory {
	
	private static final String FQCN = LoggerFactory.class.getName();
	
	@Produces
	public Logger getLogger(InjectionPoint injectionPoint) {
		System.out.println(FQCN + " produces Logger for " + injectionPoint);
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
	
	

}
