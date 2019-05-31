package kp.files;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reading the XML files.
 *
 */
public interface XmlFiles {

	/**
	 * Reads XML file.
	 * 
	 */
	public static void readXmlFile() {

		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(Constants.EXAMPLE_PATH.toString());
			System.out.printf("Document URI[%s]%n", document.getDocumentURI());

			final Element element = document.getDocumentElement();
			System.out.printf("Root element: name[%s]%n", element.getNodeName());
			final Node childNode = element.getChildNodes().item(1);
			System.out.printf("Child node: name[%s]%n", childNode.getNodeName());
			final NodeList nodeList = document.getElementsByTagName("third");
			System.out.printf("Node with tag 'third': name[%s]%n", nodeList.item(0).getNodeName());

			final XPathFactory xPathFactory = XPathFactory.newInstance();
			final XPath xpath = xPathFactory.newXPath();
			final XPathExpression xPathExpr = xpath.compile("/first/second/third/fourth/text()");
			final Node node = xPathExpr.evaluateExpression(document, Node.class);
			System.out.printf("Node from xpath expression: text[%s]%n", node.getTextContent());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("▼▼▼ XML fragment ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼");
		final Predicate<String> predicate = Predicate.not(line -> line.contains("second>"));
		try (Stream<String> linesStream = Files.lines(Constants.EXAMPLE_PATH)) {
			// show only xml file fragment
			linesStream/*-*/
					.dropWhile(predicate)/*-*/
					.skip(1)/*-*/
					.takeWhile(predicate)/*-*/
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
		System.out.println("- ".repeat(50));
	}
}
