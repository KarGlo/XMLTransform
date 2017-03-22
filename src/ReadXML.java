import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
	   

		try {

			File file = new File("C://Users//glowackk//Documents//page.xml");

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document docIn = dBuilder.parse(file);

			System.out.println("Root element :" + docIn.getDocumentElement().getNodeName());

			
		
			
//		Element rootElement = doc.createElement("page");
//		doc.appendChild(rootElement);

			if (docIn.hasChildNodes()) {

				analyse(docIn.getChildNodes());

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
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
		

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);
			

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				if (tempNode.getNodeName() == "text") {
					
					//add value to element and add to tree
					text.appendChild(docOut.createTextNode(tempNode.getTextContent()));
					rootElement.appendChild(text);

					// get node name and value
					System.out.println("\nParent Node Name =" + rootElement.getNodeName() + " [OPEN]");
					System.out.println("Parent Node Value =" + rootElement.getNodeValue());
					System.out.println("Children number =" + rootElement.getChildNodes().getLength());
					System.out.println("Children children number =" + rootElement.getFirstChild().getChildNodes().getLength());
					System.out.println("Node child value =" + rootElement.getFirstChild().getLastChild().getNodeName());
					System.out.println("Node child value =" + rootElement.getFirstChild().getLastChild().getNodeValue());


					if (tempNode.hasAttributes()) {
						

						// get attributes names and values
						NamedNodeMap nodeMap = tempNode.getAttributes();
						
						//iterate all attributes in element
						for (int i = 0; i < nodeMap.getLength(); i++) {

							Node node = nodeMap.item(i);
							
							//// make sure font attribute.
							if(tempNode.getNodeName() == "font")
								
								if (fontSize != Integer.parseInt(node.getNodeValue()) && fontSize > 0)
								{
									newElement = true;
								}
							fontSize =  Integer.parseInt(node.getNodeValue());
							
						}
					}
					
					
				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					analyse(tempNode.getChildNodes());

				}

//				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

			}

		}
		
	}
	
}