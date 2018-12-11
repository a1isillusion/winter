package aop;

import lifecycle.BeanPostProcessor;

public class AutoProxyCreater implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		System.out.println(beanName+"  before");
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) {
		System.out.println(beanName+"  after");
		return bean;
	}

}
