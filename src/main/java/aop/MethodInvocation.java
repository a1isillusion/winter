package aop;

import java.lang.reflect.Method;
import java.util.List;

public class MethodInvocation {
public Object targetObject;
public Method method;
public Object[] args;
public List<Advisor> advisorList;
public int size;

public MethodInvocation(Object targetObject, Method method, Object[] args, List<Advisor> advisorList) {
	this.targetObject = targetObject;
	this.method = method;
	this.args = args;
	this.advisorList = advisorList;
}

public Object proceed() throws Exception {
	if(advisorList==null||size==advisorList.size()) {
		return invokeJoinPoint();
	}else {
		Advisor advisor=advisorList.get(size++);
		if(advisor.isMatch(targetObject, method)) {
			return advisor.invoke(MethodInvocation.this);
		}
	}
	return invokeJoinPoint();
}
public Object invokeJoinPoint() throws Exception {
	return method.invoke(targetObject, args);
}
}
