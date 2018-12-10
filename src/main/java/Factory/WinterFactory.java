package Factory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import NamespaceHandler.WinterNamespaceHandler;

public class WinterFactory {
public static HashMap<String,Object> earlyBeans=new HashMap<String, Object>();
public static HashMap<String,Object> singletonBeans=new HashMap<String, Object>();
public static HashMap<String,BeanDefinition> beanDefinitionMap=new HashMap<String, BeanDefinition>();
static {
	WinterNamespaceHandler.init();
}
@SuppressWarnings("unchecked")
public static void parse(String path) {
	 SAXReader reader=new SAXReader();
	try {
		Document document = reader.read(new File(path)); 
		Element root=document.getRootElement();
		List<Element> childElements = root.elements();
		for(Element element:childElements) {
			WinterNamespaceHandler.getParserMap().get(element.getName()).parse(element);
		}
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public static void initBeans() {
	for(String key:beanDefinitionMap.keySet()) {
		if(!beanDefinitionMap.get(key).getIsToBean()) {
			initBean(beanDefinitionMap.get(key));
		}
	}
	earlyBeans.clear();
}
public static void initBean(BeanDefinition beanDefinition) {
	try {
		Class<?> beanClass=Class.forName(beanDefinition.getClassName());
		Object bean=null;
		if(beanDefinition.getConstructorInit()==0) {
		    bean=beanClass.newInstance();
			for(String key:beanDefinition.getAttributes().keySet()) {
				Field field=beanClass.getDeclaredField(key);
				field.set(bean,convertValue(field.getType().getName(), beanDefinition.getAttribute(key).getValue()));
			}
		}
		else {
			Class<?>[] parameterTypes=new Class<?>[beanDefinition.getAttributes().size()];
			Object[] args=new Object[beanDefinition.getAttributes().size()];
			int i=0;
			for(String key:beanDefinition.getAttributes().keySet()) {
				parameterTypes[i]=Class.forName(key);
                String[] keyArray=key.split("\\.");
                String type=keyArray[keyArray.length-1];
				args[i]=convertValue(type, beanDefinition.getAttribute(key).getValue());				
				i++;
			}
			Constructor<?> constructor=beanClass.getConstructor(parameterTypes);
		    bean=constructor.newInstance(args);
		}
		beanDefinition.setIsToBean(true);
		singletonBeans.put(beanDefinition.getBeanName(),bean);
		
	}catch (Exception e) {
		e.printStackTrace();
	}
}
public static Object convertValue(String type,String value) {//转换方法,待修改
	if(type.equals("int")||type.equals("Integer")) {
		return Integer.parseInt(value);
	}
	return value;
}
public static HashMap<String, Object> getEarlyBeans() {
	return earlyBeans;
}
public static void setEarlyBeans(HashMap<String, Object> earlyBeans) {
	WinterFactory.earlyBeans = earlyBeans;
}
public static HashMap<String, Object> getSingletonBeans() {
	return singletonBeans;
}
public static void setSingletonBeans(HashMap<String, Object> singletonBeans) {
	WinterFactory.singletonBeans = singletonBeans;
}
public static HashMap<String, BeanDefinition> getBeanDefinitionMap() {
	return beanDefinitionMap;
}
public static void setBeanDefinitionMap(HashMap<String, BeanDefinition> beanDefinitionMap) {
	WinterFactory.beanDefinitionMap = beanDefinitionMap;
}
public static void setEarlyBean(String key,Object value) {
	WinterFactory.earlyBeans.put(key, value);
}
public static Object getEarlyBean(String key) {
	return WinterFactory.earlyBeans.get(key);
}
public static void setSingletonBean(String key,Object value) {
	WinterFactory.singletonBeans.put(key, value);
}
public static Object getSingletonBean(String key) {
	return WinterFactory.singletonBeans.get(key);
}
public static void setBeanDefinition(String key,BeanDefinition value) {
	WinterFactory.beanDefinitionMap.put(key, value);
}
public static BeanDefinition getBeanDefinition(String key) {
	return WinterFactory.beanDefinitionMap.get(key);
}
}
