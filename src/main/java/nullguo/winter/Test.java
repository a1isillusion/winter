package nullguo.winter;

import Factory.ApplicationContext;
import Factory.WinterFactory;
import lifecycle.ApplicationContextAware;
import lifecycle.BeanFactoryAware;
import lifecycle.BeanNameAware;
import lifecycle.DisposableBean;
import lifecycle.InitializingBean;

public class Test implements BeanNameAware,BeanFactoryAware,ApplicationContextAware,InitializingBean,DisposableBean {
public String a;
public int b;
public Test c;
public Test() {
	
}
public Test(String a,Integer b) {
	this.a=a;
	this.b=b;
}
@Override
public String toString() {
	return "Test [a=" + a + ", b=" + b + ", c=" + c.a+c.b + "]";
}
public void destroy() {
	System.out.println("destroy");
	
}
public void afterPropertiesSet() {
	System.out.println("afterPropertiesSet");
	
}
public void setApplicationContext(ApplicationContext applicationContext) {
	System.out.println("setApplicationContext");
	
}
public void setBeanFactroy(WinterFactory factory) {
	System.out.println("setBeanFactroy");
	
}
public void setBeanName(String beanName) {
	System.out.println("setBeanName");
	
}


}
