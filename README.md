# winter
winter：一个模仿spring的轻量级框架,目前已经实现Spring的IOC,AOP功能，支持xml配置和根路径扫描注解配置。

使用方法：请参考winter.xml和nullguo.winter包中的Test类进行配置，参考App类的main方法进行初始化。

配置完成后，该如何初始化？

1.调用  ApplicationContext.init();(可以在自己指定winter.xml的路径,如Application.init(path);若不指定，默认winter.xml在根路径。)

2.调用ApplicationContext.getBean()获得单例bean。

3.Winter mvc功能待实现~
