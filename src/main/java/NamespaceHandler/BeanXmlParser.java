package NamespaceHandler;

import java.util.List;

import org.dom4j.Element;

import Factory.BeanDefinition;
import Factory.WinterFactory;

public class BeanXmlParser implements XmlParser {
@SuppressWarnings("unchecked")
public void parse(Element element) {
		BeanDefinition beanDefinition=new BeanDefinition();
		beanDefinition.setBeanName(element.attributeValue("name"));
		beanDefinition.setClassName(element.attributeValue("class"));
		beanDefinition.setScope(element.attributeValue("scope")!=null?element.attributeValue("scope"):"singleton");
		beanDefinition.setInitMethod(element.attributeValue("init-method"));
		beanDefinition.setDestroyMethod(element.attributeValue("destroy-method"));
		List<Element> childElements = element.elements();
		for(Element childelement:childElements) {
			if(childelement.getName().equals("property")) {
				beanDefinition.setConstructorInit(0);
				if( childelement.attribute("ref")!=null) {
					beanDefinition.setAttribute(childelement.attributeValue("name"), childelement.attribute("ref"));
				}else {
					beanDefinition.setAttribute(childelement.attributeValue("name"), childelement.attribute("value"));
				}	
			}
			else if(childelement.getName().equals("constructor-arg")) {
				beanDefinition.setConstructorInit(1);
				if( childelement.attribute("ref")!=null) {
					beanDefinition.setAttribute(childelement.attributeValue("type"), childelement.attribute("ref"));
				}else {
					beanDefinition.setAttribute(childelement.attributeValue("type"), childelement.attribute("value"));
				}	
			}
			
		}
		WinterFactory.setBeanDefinition(beanDefinition.getBeanName(), beanDefinition);
}
}
