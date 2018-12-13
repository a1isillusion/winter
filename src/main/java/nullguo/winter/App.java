package nullguo.winter;


import Factory.ApplicationContext;
import Factory.WinterFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )throws Exception
    {
        ApplicationContext.init();
        System.out.println(ApplicationContext.getBean("test1"));
        System.out.println(ApplicationContext.getBean("test3"));
        WinterFactory.close();
    }
}
