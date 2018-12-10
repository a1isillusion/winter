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
		List<Element> childElements = element.elements();
		for(Element childelement:childElements) {
			if(childelement.getName().equals("property")) {
				beanDefinition.setConstructorInit(0);
			}
			else if(childelement.getName().equals("constructor-arg")) {
				beanDefinition.setConstructorInit(1);
			}
			beanDefinition.setAttribute(childelement.attributeValue("name"), childelement.attributeValue("value"));
		}
		WinterFactory.setBeanDefinition(beanDefinition.getBeanName(), beanDefinition);
}
}
