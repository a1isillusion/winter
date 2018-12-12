package aop;

import java.lang.reflect.Method;

public class Advisor {
public PointCut pointCut;
public Advice advice;

public Advisor(PointCut pointCut, Advice advice) {
	this.pointCut = pointCut;
	this.advice = advice;
}
public boolean isMatch(Object targetObject,Method method) {
	return pointCut.isMatch(targetObject, method);
}
public boolean isMatch(Object targetObject) {
	return pointCut.isMatch(targetObject);
}
public Object invoke(MethodInvocation invocation) throws Exception {
	return advice.invoke(invocation);
}
}
