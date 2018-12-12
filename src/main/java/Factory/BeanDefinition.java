package Factory;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.dom4j.Attribute;

public class BeanDefinition {
public String beanName;
public String className;
public HashMap<String, Attribute> attributes;
public String scope;
public int constructorInit;
public boolean isRef;
public boolean isToBean;
public boolean isProcessor;
public boolean isAdvisor;
public String initMethod;
public String destroyMethod;
public BeanDefinition() {
	this.attributes=new LinkedHashMap<String, Attribute>();
}
public String getBeanName() {
	return beanName;
}
public void setBeanName(String beanName) {
	this.beanName = beanName;
}
public String getClassName() {
	return className;
}
public void setClassName(String className) {
	this.className = className;
}
public HashMap<String, Attribute> getAttributes() {
	return attributes;
}
public void setAttributes(HashMap<String, Attribute> attributes) {
	this.attributes = attributes;
}
public String getScope() {
	return scope;
}
public void setScope(String scope) {
	this.scope = scope;
}
public int getConstructorInit() {
	return constructorInit;
}
public void setConstructorInit(int constructorInit) {
	this.constructorInit = constructorInit;
}
public boolean isRef() {
	return isRef;
}
public void setRef(boolean isRef) {
	this.isRef = isRef;
}
public boolean getIsToBean() {
	return isToBean;
}
public void setIsToBean(boolean isToBean) {
	this.isToBean = isToBean;
}
public boolean getIsProcessor() {
	return isProcessor;
}
public void setIsProcessor(boolean isProcessor) {
	this.isProcessor = isProcessor;
}

public boolean getIsAdvisor() {
	return isAdvisor;
}
public void setIsAdvisor(boolean isAdvisor) {
	this.isAdvisor = isAdvisor;
}
public String getInitMethod() {
	return initMethod;
}
public void setInitMethod(String initMethod) {
	this.initMethod = initMethod;
}
public String getDestroyMethod() {
	return destroyMethod;
}
public void setDestroyMethod(String destroyMethod) {
	this.destroyMethod = destroyMethod;
}
public void setAttribute(String key,Attribute value) {
	attributes.put(key, value);
}
public Attribute getAttribute(String key) {
	return attributes.get(key);
}
@Override
public String toString() {
	return "BeanDefinition [beanName=" + beanName + ", className=" + className + ", attributes=" + attributes
			+ ", scope=" + scope + ", constructorInit=" + constructorInit + ", initMethod=" + initMethod
			+ ", destroyMethod=" + destroyMethod + "]";
}

}
