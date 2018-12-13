package Factory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import NamespaceHandler.WinterNamespaceHandler;
import annotation.Autowired;
import annotation.Resourced;
import lifecycle.ApplicationContextAware;
import lifecycle.BeanFactoryAware;
import lifecycle.BeanNameAware;
import lifecycle.BeanPostProcessor;
import lifecycle.DisposableBean;
import lifecycle.InitializingBean;
import util.ClassUtil;

public class WinterFactory {//winter核心工厂类
public static ArrayList<BeanPostProcessor>processorList=new ArrayList<BeanPostProcessor>();
public static HashMap<String,Object> earlyBeans=new HashMap<String, Object>();
public static HashMap<String,Object> singletonBeans=new HashMap<String, Object>();
public static HashMap<String,BeanDefinition> beanDefinitionMap=new HashMap<String, BeanDefinition>();
private static final WinterFactory instance;
static {
	instance=new WinterFactory();
	WinterNamespaceHandler.init();
}
@SuppressWarnings("unchecked")
public static void parse(String path) {//解析配置文件,不同的element使用不同的XmlParser解析,最后都会解析为Beandefinition
	 SAXReader reader=new SAXReader();
	try {
		Document document = reader.read(new File(path)); 
		Element root=document.getRootElement();
		List<Element> childElements = root.elements();
		for(Element element:childElements) {
			WinterNamespaceHandler.getParserMap().get(element.getName()).parse(element);
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public static void initBeans() {
	for(String key:beanDefinitionMap.keySet()) {//先把Advisor的bean加载
		if(!beanDefinitionMap.get(key).getIsToBean()&&beanDefinitionMap.get(key).getIsAdvisor()) {
			initBean(beanDefinitionMap.get(key));
		}
	}
	for(String key:beanDefinitionMap.keySet()) {//再把BeanPostProcessor的bean加载
		if(!beanDefinitionMap.get(key).getIsToBean()&&beanDefinitionMap.get(key).getIsProcessor()) {
			initBean(beanDefinitionMap.get(key));
		}
	}
	for(String key:beanDefinitionMap.keySet()) {//最后加载其他bean
		if(!beanDefinitionMap.get(key).getIsToBean()) {
			initBean(beanDefinitionMap.get(key));
		}
	}
	earlyBeans.clear();//全部bean加载完成,清空earlyBeans
}
public static void initBean(BeanDefinition beanDefinition) {
	try {
		if(beanDefinition.getScope().equals("prototype")&&beanDefinition.getIsToBean()==false) {
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
			else if(beanDefinition.getConstructorInit()==1) {//使用构造函数,构造函数加载实例bean
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
			else if(beanDefinition.getConstructorInit()==2) {//使用扫描方式注入bean
				bean=Class.forName(beanDefinition.className).newInstance();
				earlyBeans.put(beanDefinition.getBeanName(),bean);//解决循环引用,把未完成加载的bean先放到earlyBeans中
				for(Field field:beanClass.getDeclaredFields()) {
					if(field.isAnnotationPresent(Resourced.class)) {//若有Resourced注解
						String name=field.getAnnotation(Resourced.class).name();
						Object arg=null;
	                	if(singletonBeans.containsKey(name)) {//从singletonBeans获取对应的实例
	                		arg=singletonBeans.get(name);	
						}
						else if(earlyBeans.containsKey(name)) {//从earlyBeans获取对应的实例
							arg=earlyBeans.get(name);	
						}
						else {//singletonBeans和earlyBeans都没有对应的实例,则加载对应的的实例到两个Beans集合中，再赋值
							initBean(beanDefinitionMap.get(name));
							arg=earlyBeans.get(name);
						}
	                	field.set(bean,arg);//反射设置值
					}else if(field.isAnnotationPresent(Autowired.class)){//若有Autowired注解
						Object matchBean=null;
						for(String key:earlyBeans.keySet()) {
							Object earlyBean=earlyBeans.get(key);
							Class<?> targetType=field.getType();
							if(ClassUtil.checkType(targetType, earlyBean.getClass())) {
								matchBean=earlyBean;
								field.set(bean,matchBean);//反射设置值
								break;
							}
						}
						if(matchBean==null) {
							for(String key:beanDefinitionMap.keySet()) {
								BeanDefinition definition=beanDefinitionMap.get(key);
								if(!definition.equals(beanDefinition)&&ClassUtil.checkType(field.getType(), Class.forName(definition.getClassName()))) {
									initBean(definition);
									field.set(bean, earlyBeans.get(definition.getBeanName()));//反射设置值
									break;
								}
							}	
						}
					}
				}
			}
			bean=handleBeanAfterInit(beanDefinition, bean);//bean实例已经加载完成，继续完成bean的生命周期
			beanDefinition.setIsToBean(true);
			singletonBeans.put(beanDefinition.getBeanName(),bean);
			if(beanDefinition.getIsProcessor()) {
				processorList.add((BeanPostProcessor)bean);
			}
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
public static Object handleBeanAfterInit(BeanDefinition beanDefinition,Object bean) throws Exception {//声明周期方法
	if(bean instanceof BeanNameAware) {
		Method setBeanName=bean.getClass().getMethod("setBeanName",new Class<?>[]{String.class});
		setBeanName.invoke(bean, new Object[]{beanDefinition.getBeanName()});
	}
	if(bean instanceof BeanFactoryAware) {
		Method setBeanFactory=bean.getClass().getMethod("setBeanFactroy",new Class<?>[]{WinterFactory.class});
		setBeanFactory.invoke(bean, new Object[]{getInstance()});
	}
	if(bean instanceof ApplicationContextAware) {
		Method setApplicationContext=bean.getClass().getMethod("setApplicationContext",new Class<?>[]{ApplicationContext.class});
		setApplicationContext.invoke(bean, new Object[]{new ApplicationContext()});
	}
	if(!bean.getClass().isAssignableFrom(BeanPostProcessor.class)) {
		for(BeanPostProcessor processor:processorList) {
		Method postProcessBeforeInitialization=processor.getClass().getMethod("postProcessBeforeInitialization",new Class<?>[]{Object.class,String.class});
		bean=postProcessBeforeInitialization.invoke(processor, new Object[]{bean,beanDefinition.getBeanName()});
	    }
	}
	if(bean instanceof InitializingBean) {
		Method afterPropertiesSet=bean.getClass().getMethod("afterPropertiesSet",new Class<?>[]{});
		afterPropertiesSet.invoke(bean, new Object[]{});
	}
	if(beanDefinition.getInitMethod()!=null) {
		Method initMethod=bean.getClass().getMethod(beanDefinition.getInitMethod(),new Class<?>[]{});
		initMethod.invoke(bean, new Object[]{});
	}
	if(!bean.getClass().isAssignableFrom(BeanPostProcessor.class)) {
	for(BeanPostProcessor processor:processorList) {
		Method postProcessAfterInitialization=processor.getClass().getMethod("postProcessAfterInitialization",new Class<?>[]{Object.class,String.class});
		bean=postProcessAfterInitialization.invoke(processor, new Object[]{bean,beanDefinition.getBeanName()});
	    }
	}
	return bean;
}
public static void close() {//关闭factory,调用destroy-method和destroy方法
	try {
	for(String key:beanDefinitionMap.keySet()) {
		Object bean=singletonBeans.get(key);
		BeanDefinition beanDefinition=beanDefinitionMap.get(key);
		if(bean instanceof DisposableBean) {
			Method destroy = bean.getClass().getMethod("destroy",new Class<?>[]{});
			destroy.invoke(bean, new Object[]{});
		}
		if(beanDefinition.destroyMethod!=null) {
			Method destoryMethod=bean.getClass().getMethod(beanDefinition.getDestroyMethod(),new Class<?>[]{});
			destoryMethod.invoke(bean, new Object[]{});
		}
	}
	beanDefinitionMap.clear();
	singletonBeans.clear();
	processorList.clear();
	} catch (Exception e) {
		e.printStackTrace();
	}
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
public static WinterFactory getInstance() {
	return instance;
}
}
