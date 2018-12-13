package NamespaceHandler;

import java.util.List;

import org.dom4j.Element;

import Factory.BeanDefinition;
import Factory.WinterFactory;
import annotation.Component;
import annotation.Controller;
import annotation.Service;
import util.ClassUtil;

public class ComponentScanXmlParser implements XmlParser {

	public void parse(Element element) throws Exception {
		String basePackage=element.attributeValue("base-package");
		System.out.println("base-package:"+basePackage);
		List<Class<?>> classList=ClassUtil.getAllClassByPackageName(basePackage);
		for(Class<?> clazz:classList) {
			if(clazz.isAnnotationPresent(Component.class)||clazz.isAnnotationPresent(Controller.class)||clazz.isAnnotationPresent(Service.class)) {
				System.out.println(clazz.getName());
				BeanDefinition beanDefinition=new BeanDefinition();
				beanDefinition.setBeanName(clazz.getName());
				beanDefinition.setClassName(clazz.getName());
				beanDefinition.setScope("prototype");
				beanDefinition.setConstructorInit(2);
				WinterFactory.setBeanDefinition(clazz.getName(), beanDefinition);
			}
		}
		
	}

}
