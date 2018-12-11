package nullguo.winter;

public class Test {
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


}
