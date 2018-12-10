package NamespaceHandler;

import java.util.HashMap;

public class WinterNamespaceHandler {
public static HashMap<String,XmlParser> parserMap=new HashMap<String, XmlParser>();
public static void init() {
	parserMap.put("bean", new BeanXmlParser());
}
public static HashMap<String, XmlParser> getParserMap() {
	return parserMap;
}
public static void setParserMap(HashMap<String, XmlParser> parserMap) {
	WinterNamespaceHandler.parserMap = parserMap;
}

}