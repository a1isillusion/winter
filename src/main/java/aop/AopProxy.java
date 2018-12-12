package aop;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class AopProxy implements MethodInterceptor {
private Object targetObject;
private List<Advisor> advisorList;
public AopProxy(Object targetObject, List<Advisor> advisorList) {
	this.targetObject = targetObject;
	this.advisorList = advisorList;
}
public Object getInstance() {
	Enhancer enhancer=new Enhancer();
	enhancer.setSuperclass(targetObject.getClass());
	enhancer.setCallback(AopProxy.this);
	return enhancer.create();
}

public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
	return new MethodInvocation(targetObject, method, args, advisorList).proceed();
}

}
