import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MyDocument {
	
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    
    void createMyDocument () {
    try {
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}
