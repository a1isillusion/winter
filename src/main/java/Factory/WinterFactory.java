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
	earlyBeans.clear();//全部bean加载完成,清空earlyBeans
}
public static void initBean(BeanDefinition beanDefinition) {
	try {
		if(beanDefinition.getScope().equals("singleton")) {
			Class<?> beanClass=Class.forName(beanDefinition.getClassName());
			Object bean=null;
			if(beanDefinition.getConstructorInit()==0) {//不用构造函数，反射加载实例bean
			    bean=beanClass.newInstance();
			    earlyBeans.put(beanDefinition.getBeanName(),bean);//解决循环引用,把未完成加载的bean先放到earlyBeans中
				for(String key:beanDefinition.getAttributes().keySet()) {
					Field field=beanClass.getDeclaredField(key);
					if(beanDefinition.getAttribute(key).getName().equals("value")) {//赋值value
					field.set(bean,convertValue(field.getType().getName(), beanDefinition.getAttribute(key).getValue()));	
					}
					else {//赋值ref
						if(singletonBeans.containsKey(beanDefinition.getAttribute(key).getValue())) {//从singletonBeans获取对应的实例
							field.set(bean,singletonBeans.get(beanDefinition.getAttribute(key).getValue()));	
						}
						else if(earlyBeans.containsKey(beanDefinition.getAttribute(key).getValue())) {//从earlyBeans获取对应的实例
							field.set(bean,earlyBeans.get(beanDefinition.getAttribute(key).getValue()));	
						}
						else {//singletonBeans和earlyBeans都没有对应的实例,则加载对应的的实例到两个Beans集合中，再赋值
							initBean(beanDefinitionMap.get(beanDefinition.getAttribute(key).getValue()));
							field.set(bean,earlyBeans.get(beanDefinition.getAttribute(key).getValue()));
						}			
					}
				}
			}
			else {//使用构造函数,构造函数加载实例bean
				Class<?>[] parameterTypes=new Class<?>[beanDefinition.getAttributes().size()];
				Object[] args=new Object[beanDefinition.getAttributes().size()];
				int i=0;
				for(String key:beanDefinition.getAttributes().keySet()) {
					parameterTypes[i]=Class.forName(key);
	                String[] keyArray=key.split("\\.");
	                String type=keyArray[keyArray.length-1];
	                if(beanDefinition.getAttribute(key).getName().equals("value")) {
	                args[i]=convertValue(type, beanDefinition.getAttribute(key).getValue());	
	                }
	                else {
	                	if(singletonBeans.containsKey(beanDefinition.getAttribute(key).getValue())) {//从singletonBeans获取对应的实例
	                		args[i]=singletonBeans.get(beanDefinition.getAttribute(key).getValue());	
						}
						else if(earlyBeans.containsKey(beanDefinition.getAttribute(key).getValue())) {//从earlyBeans获取对应的实例
							args[i]=earlyBeans.get(beanDefinition.getAttribute(key).getValue());	
						}
						else {//singletonBeans和earlyBeans都没有对应的实例,则加载对应的的实例到两个Beans集合中，再赋值
							initBean(beanDefinitionMap.get(beanDefinition.getAttribute(key).getValue()));
							args[i]=earlyBeans.get(beanDefinition.getAttribute(key).getValue());
						}
					}				
					i++;
				}
				Constructor<?> constructor=beanClass.getConstructor(parameterTypes);
			    bean=constructor.newInstance(args);
			    earlyBeans.put(beanDefinition.getBeanName(),bean);//解决循环引用,把未完成加载的bean先放到earlyBeans中
			}
			beanDefinition.setIsToBean(true);
			singletonBeans.put(beanDefinition.getBeanName(),bean);
		}
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
