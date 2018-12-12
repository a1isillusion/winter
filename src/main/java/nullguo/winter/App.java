package nullguo.winter;


import Factory.WinterFactory;
import aop.Advice;
import aop.Advisor;
import aop.PointCut;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        WinterFactory.parse("C:/Users/Administrator/eclipse-workspace/winter/src/main/java/winter.xml");
        Advisor advisor=new Advisor(new PointCut("execution(* nullguo.winter.Test.toString(..))"),new Advice("around",new AopTest(),"before"));
        WinterFactory.setSingletonBean("aopTest", advisor);
        WinterFactory.initBeans();
        System.out.println(WinterFactory.getSingletonBean("test1"));
        System.out.println(WinterFactory.getSingletonBean("test3"));
        WinterFactory.close();
    }
}
