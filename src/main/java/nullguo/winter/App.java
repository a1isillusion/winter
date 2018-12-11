package nullguo.winter;

import com.alibaba.fastjson.JSON;

import Factory.BeanDefinition;
import Factory.WinterFactory;

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
        WinterFactory.initBeans();
        System.out.println(WinterFactory.getSingletonBean("test1"));
        System.out.println(WinterFactory.getSingletonBean("test3"));
        WinterFactory.close();
    }
}
