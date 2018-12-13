package Factory;

import java.io.File;

public class ApplicationContext {
//待完善,目前只作为启动类，应该增加更多功能
public static void init() {//不指定winter配置文件,默认配置文件为根路径的winter.xml
	File f = new File(ApplicationContext.class.getResource("/").getPath());
	 File [] fileList=f.listFiles();
	 for(File file:fileList) {
		 if(file.getName().equals("winter.xml")) {
			  WinterFactory.parse(file.getPath());
			  WinterFactory.initBeans();
		 }
	 }
}
public static void init(String path) {//指定winter配置文件
	 WinterFactory.parse(path);
     WinterFactory.initBeans();
}
public static Object getBean(String key) {//从工厂获取单例bean
	return WinterFactory.getSingletonBean(key);
}
}
