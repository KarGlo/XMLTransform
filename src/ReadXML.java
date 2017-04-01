import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXML {
	
	private static DocumentBuilder docBuilder;
	private static Document docOut;
	static Element rootElement;
    
    static Document createDocumentWithRoot () {
    
    try {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    docBuilder = docFactory.newDocumentBuilder();
    docOut = docBuilder.newDocument();
    
	rootElement = docOut.createElement("testDocument");
    docOut.appendChild(rootElement);
				
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return docOut;
    }

	
	public static void main(String[] args) throws ParserConfigurationException, TransformerConfigurationException {
		
		createDocumentWithRoot();
		
	      System.setProperty("java.net.useSystemProxies", "true");
	   

		try {
			

			File file = new File("page.xml");
			
			URL url = new URL("http://docs.sejmometr.pl/xml/348138.xml");			
			InputStream stream = url.openStream();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
     		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder dBuilder = dbf.newDocumentBuilder();			


			Document docIn = dBuilder.parse(stream);

			System.out.println("Root element :" + docIn.getDocumentElement().getNodeName());

			
		
			
//		Element rootElement = doc.createElement("page");
//		doc.appendChild(rootElement);

			if (docIn.hasChildNodes()) {

				analyse(docIn.getChildNodes());

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(docOut);
		StreamResult result = new StreamResult(new File("file.xml"));
		
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("File saved!");
		
	}

	private static void analyse(NodeList nodeList) {

		boolean newElement = false;
		int fontSize = 0;
		
		Element text = docOut.createElement("text");
		
		StringBuilder tekst = new StringBuilder();
		

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);
			String tekstTempString = tempNode.getTextContent();
			
			String[] regExCollection = { "(©Kancelaria Sejmu.*)", 
					"(^s.*\\d.*)"};
			
			boolean valueContent = true;
			
			for (int i = 0; i < regExCollection.length; i++)
			{
			
			Pattern MY_PATTERN = Pattern.compile(regExCollection[i]);
								
				if (tekstTempString != null){
				Matcher m = MY_PATTERN.matcher(tekstTempString);
				if (m.find())
					{
					valueContent = false;
					}
				
				}
			}
//			System.out.println(tekstTempString);
			

			// make sure it's element node with name text and has attibutes
				if (tempNode.getNodeType() == Node.ELEMENT_NODE && tempNode.getNodeName() == "text" && tempNode.hasAttributes() && valueContent == true) {
						
						// get attributes names and values
						NamedNodeMap nodeMap = tempNode.getAttributes();
						
						//iterate all attributes in element
						for (int i = 0; i < nodeMap.getLength(); i++) {

							Node node = nodeMap.item(i);
							
							//// make sure font attribute.
							if(node.getNodeName() == "font") {
								
								Attr attr = docOut.createAttribute("font");
								attr.setValue((node.getNodeValue()));
								//text.setAttributeNode(attr);
								
								
								if (fontSize != Integer.parseInt(node.getNodeValue()))
								{
									
									tekst.append(tempNode.getTextContent());
									text = docOut.createElement("text");
									
								}
							
								text.appendChild(docOut.createTextNode(tempNode.getTextContent()));
								rootElement.appendChild(text);
							
							
							fontSize =  Integer.parseInt(node.getNodeValue());
							}
							

							
						}
					
						
					//add value to element and add to tree
//					text.appendChild(docOut.createTextNode(tempNode.getTextContent()));
//					rootElement.appendChild(text);
//
//					// get node name and value
//					System.out.println("\nParent Node Name =" + rootElement.getNodeName() + " [OPEN]");
//					System.out.println("Parent Node Value =" + rootElement.getNodeValue());
//					System.out.println("Children number =" + rootElement.getChildNodes().getLength());
//					System.out.println("Children children number =" + rootElement.getFirstChild().getChildNodes().getLength());
//					System.out.println("Node child value =" + rootElement.getFirstChild().getLastChild().getNodeName());
//					System.out.println("Node child value =" + rootElement.getFirstChild().getLastChild().getNodeValue());



					
					
				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					analyse(tempNode.getChildNodes());

				}

//				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

		

		}
		
	}
	
}