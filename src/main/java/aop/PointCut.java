package aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PointCut {
public String expression;
public PointCut(String expression) {
	this.expression = expression;
}
public boolean isMatch(Object targetObject,Method method) {
    return pathMatch(targetObject, method);
}
public boolean isMatch(Object targetObject) {
	return pathMatch(targetObject,null);
}
public boolean regexMatch(String regex,String input) {
	regex=regex.replace("*", ".*");
	Pattern pattern=Pattern.compile(regex);
	return pattern.matcher(input).matches();
}
public boolean pathMatch(Object targetObject,Method method) {
	String scope=expression.split(" ")[0].replace("execution(", "").trim();//解析表达式范围
	String target=expression.split("\\(")[1].split(" ")[1];//解析表达式路径
	String[]splitTarget=target.split("\\.");
	List<String> regexList=new ArrayList<String>();
	for(int i=0;i<splitTarget.length;i++) {//处理正则表达式
		if(splitTarget[i].equals("")) {
			String regex=regexList.get(regexList.size()-1);
			regex+="|"+splitTarget[++i];
			regexList.remove(regexList.size()-1);
			regexList.add(regex);
		}else {
			regexList.add(splitTarget[i]);
		}
	}
	//开始判断路径是否匹配
	String beanClass=targetObject.getClass().getName();//获取beanClass名字
	String[] splitClass=beanClass.split("\\.");
	boolean pathCheck=false;
	int index=0;
	for(;index<regexList.size()-1;index++) {
		if(regexList.get(index).equals("*")) {
			pathCheck=true;
			break;
		}
		if(regexMatch(regexList.get(index), splitClass[index])) {
			continue;
		}
		break;
	}
	pathCheck=(index==regexList.size()-1)&&(index==splitClass.length);
	if(method!=null) {
		return regexMatch(regexList.get(regexList.size()-1), method.getName())&&pathCheck;
	}else {
		return pathCheck;
	}
}
}
