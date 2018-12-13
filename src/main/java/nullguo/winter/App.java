package nullguo.winter;


import Factory.ApplicationContext;
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
        ApplicationContext.init();
        System.out.println(WinterFactory.getSingletonBean("test1"));
        System.out.println(WinterFactory.getSingletonBean("test3"));
        WinterFactory.close();
    }
}
