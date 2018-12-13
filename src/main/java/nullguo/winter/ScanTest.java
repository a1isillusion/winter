package nullguo.winter;

import NamespaceHandler.ComponentScanXmlParser;
import annotation.Autowired;
import annotation.Component;
import annotation.Resourced;
@Component
public class ScanTest extends ComponentScanXmlParser{
@Autowired
public Test test;

@Override
public String toString() {
	return "ScanTest [test=" + test.a+test.b + "]";
}

}
