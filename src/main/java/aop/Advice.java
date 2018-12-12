package aop;

import java.lang.reflect.Method;

public class Advice {
public String type;
public Object adviceObject;
public String methodName;

public Advice(String type, Object adviceObject, String methodName) {
	this.type = type;
	this.adviceObject = adviceObject;
	this.methodName = methodName;
}
public Object invoke(MethodInvocation invocation) throws Exception {
	if(type.equals("before")) {
		return beforeInvoke(invocation);
	}
	if(type.equals("after")) {
		return afterInvoke(invocation);
	}
	if(type.equals("around")) {
		return aroundInvoke(invocation);
	}
	return null;
}
public Object beforeInvoke(MethodInvocation invocation) throws Exception {
	reflectMethodInvoke();
	return invocation.proceed();
}
public Object afterInvoke(MethodInvocation invocation) throws Exception {
	Object result=invocation.proceed();
	reflectMethodInvoke();
	return result;
}
public Object aroundInvoke(MethodInvocation invocation) throws Exception {
	reflectMethodInvoke();
	Object result=invocation.proceed();
	reflectMethodInvoke();
	return result;
}
public void reflectMethodInvoke() throws Exception {
	Method method=adviceObject.getClass().getDeclaredMethod(methodName);
	method.invoke(adviceObject,new Object[] {});
}
}
