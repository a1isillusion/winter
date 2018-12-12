package aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Factory.WinterFactory;
import lifecycle.BeanPostProcessor;

public class AutoProxyCreater implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) {
		if(!isInfrastructure(bean)) {
			HashMap<String, Object>singletonBeans=WinterFactory.getSingletonBeans();
			List<Advisor> advisorList=new ArrayList<Advisor>();
			for(String key:singletonBeans.keySet()) {
				Object singletonBean=singletonBeans.get(key);
				if(singletonBean instanceof Advisor&&((Advisor)singletonBean).isMatch(bean)) {
					advisorList.add((Advisor)singletonBean);
				}
			}
			if(advisorList.size()!=0) {
				return new AopProxy(bean, advisorList).getInstance();
			}
		}
		return bean;
	}
	public boolean isInfrastructure(Object bean) {
		if(bean instanceof Advisor) {
			return true;
		}else {
			return false;
		}
	}

}
