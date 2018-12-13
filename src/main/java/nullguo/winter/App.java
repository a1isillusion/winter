package nullguo.winter;


import Factory.ApplicationContext;
import Factory.WinterFactory;
import NamespaceHandler.AopXmlParser;
import NamespaceHandler.ComponentScanXmlParser;
import NamespaceHandler.XmlParser;
import aop.Advice;
import aop.Advisor;
import aop.PointCut;
import util.ClassUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )throws Exception
    {
        System.out.println( "Hello World!" );
        ApplicationContext.init();
        System.out.println(WinterFactory.getSingletonBean("test1"));
        System.out.println(WinterFactory.getSingletonBean("nullguo.winter.ScanTest"));
        System.out.println(ClassUtil.checkType(ComponentScanXmlParser.class, AopXmlParser.class));
        WinterFactory.close();
    }
}
