package com.tasnetwork.calibration.energymeter.util;

import org.springframework.context.ApplicationContext;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtil implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;		
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}
	
}
