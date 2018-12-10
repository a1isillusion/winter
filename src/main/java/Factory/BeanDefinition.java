package Factory;

import java.util.HashMap;

public class BeanDefinition {
public String beanName;
public String className;
public HashMap<String, Object> attributes;
public int scope;
public int constructorInit;
public String initMethod;
public String destroyMethod;
public BeanDefinition() {
	this.attributes=new HashMap<String, Object>();
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
public HashMap<String, Object> getAttributes() {
	return attributes;
}
public void setAttributes(HashMap<String, Object> attributes) {
	this.attributes = attributes;
}
public int getScope() {
	return scope;
}
public void setScope(int scope) {
	this.scope = scope;
}
public int getConstructorInit() {
	return constructorInit;
}
public void setConstructorInit(int constructorInit) {
	this.constructorInit = constructorInit;
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
public void setAttribute(String key,Object value) {
	attributes.put(key, value);
}
public Object getAttribute(String key) {
	return attributes.get(key);
}
@Override
public String toString() {
	return "BeanDefinition [beanName=" + beanName + ", className=" + className + ", attributes=" + attributes
			+ ", scope=" + scope + ", constructorInit=" + constructorInit + ", initMethod=" + initMethod
			+ ", destroyMethod=" + destroyMethod + "]";
}

}
