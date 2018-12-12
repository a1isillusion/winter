package aop;

import java.lang.reflect.Method;

public class PointCut {
public String expression;
public boolean isMatch(Object targetObject,Method method) {
	return true;
}
public boolean isMatch(Object targetObject) {
	return true;
}
}
