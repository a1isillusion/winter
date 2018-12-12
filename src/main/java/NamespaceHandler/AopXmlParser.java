package NamespaceHandler;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import Factory.WinterFactory;
import aop.Advice;
import aop.Advisor;
import aop.PointCut;

public class AopXmlParser implements XmlParser {

	@SuppressWarnings("unchecked")
	public void parse(Element element) throws Exception {
		List<Element> childElements = element.elements();
		for(Element childElement:childElements) {
			if(childElement.getName().equals("aopaspect")) {
				Object aopObject=Class.forName(childElement.attributeValue("class")).newInstance();
				HashMap<String, PointCut> pointCutMap=new HashMap<String, PointCut>();
				List<Element> grandchildElements=childElement.elements();
				for(Element grandchildElement:grandchildElements) {
					if(grandchildElement.getName().equals("aoppointcut")) {
						pointCutMap.put(grandchildElement.attributeValue("id"),new PointCut(grandchildElement.attributeValue("expression")));
					}else {
						Advisor advisor=new Advisor(pointCutMap.get(grandchildElement.attributeValue("pointcut-ref")), new Advice(grandchildElement.getName().replace("aop", ""), aopObject, grandchildElement.attributeValue("method")));
						WinterFactory.setSingletonBean(advisor.hashCode()+"", advisor);
					}
				}
			}
		}
		
	}

}
