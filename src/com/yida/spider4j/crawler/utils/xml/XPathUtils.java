package com.yida.spider4j.crawler.utils.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.yida.spider4j.crawler.utils.common.GerneralUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: XPathUtils
 * @Description: XPath操作工具类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月17日 下午9:41:40
 *
 */
public class XPathUtils {
	private XPath xpath;
	private Transformer transformer;
	private XMLReader xmlReader;

	private static final String DEFAULT_ATTR_NAME = "text";
	
	private XPathUtils() {
		init();
	}

	private static class SingletonHolder {
		private static final XPathUtils INSTANCE = new XPathUtils();
	}

	public static final XPathUtils getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private void init() {
		XPathFactory xpathFac = XPathFactory.newInstance();
		if(this.xpath == null) {
			this.xpath = xpathFac.newXPath();
		}

		//this.xpath = xpathFac.newXPath();
		//initReader();
	}
	
	/*public void initReader() {
		try {
	    	this.xmlReader = new Parser();
		    this.xmlReader.setFeature(Parser.namespacesFeature, false);
		    this.xmlReader.setFeature(Parser.namespacePrefixesFeature, false);
		    this.xmlReader.setFeature(Parser.validationFeature, false);
		    
			this.transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (SAXNotRecognizedException e) {
			e.printStackTrace();
		} catch (SAXNotSupportedException e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: parseDocument
	 * @Description: 根据XML文件的硬盘路径构建Document
	 * @param @param path  XML文件的硬盘路径，如C:/xxxx/a.xml
	 * @param @return
	 * @return Document
	 * @throws
	 */
	public Document parseDocument(String path) {
		InputStream input = null;
		DOMResult result = null;
		try {
			input = new FileInputStream(path);
			result = new DOMResult();
		    transformer.transform(new SAXSource(this.xmlReader, new InputSource(input)), result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	    return (Document)result.getNode();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: parseDocument
	 * @Description: 根据网络上的一个XML文件的URL链接构建Document
	 * @param @param url xml文件的URL链接,比如http://www.xxxxx/aaaa.xml
	 * @param @return
	 * @return Document
	 * @throws
	 */
	public Document parseDocument(URL url) {
	    InputStream input = null;
		DOMResult result = null;
		try {
			input = url.openStream();
			result = new DOMResult();
		    transformer.transform(new SAXSource(this.xmlReader, new InputSource(input)), result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return (Document)result.getNode();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: parseDocument
	 * @Description: 根据XML输入流构建Document
	 * @param @param is
	 * @param @return
	 * @return Document
	 * @throws
	 */
	public Document parseDocument(InputStream is) {
		DOMResult result = new DOMResult();
	    try {
			transformer.transform(new SAXSource(this.xmlReader, new InputSource(is)), result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	    return (Document)result.getNode();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: parseDocument
	 * @Description: 根据IO Reader构建Document
	 * @param @param is
	 * @param @return
	 * @return Document
	 * @throws
	 */
	public Document parseDocument(Reader reader) {
		DOMResult result = new DOMResult();
	    try {
			transformer.transform(new SAXSource(this.xmlReader, new InputSource(reader)), result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	    return (Document)result.getNode();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: fragment2Document
	 * @Description: 根据XML代码片段构建Document
	 * @param @param xmlString
	 * @param @return
	 * @return Document
	 * @throws
	 */
	/*public Document fragment2Document(String xmlString) {
		StringReader stringReader = new StringReader(xmlString);
		DOMResult result = new DOMResult();
	    try {
	    	
	    	this.transformer = TransformerFactory.newInstance().newTransformer();
			this.transformer.transform(new SAXSource(this.xmlReader, new InputSource(stringReader)), result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	    return (Document)result.getNode();
	}*/
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: fragmentToDocument
	 * @Description: 根据XML代码片段构建Document[基于HTMLCleaner实现]
	 * @param @param xmlString
	 * @param @return
	 * @return Document
	 * @throws
	 */
	public Document fragmentToDocument(String xmlString) {
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(xmlString);
	    DomSerializer ser = new DomSerializer(cleaner.getProperties());
	    try {
			return ser.createDOM(node);
		} catch (ParserConfigurationException e) {
			return null;
		}
	}
	
	/**
	 * @Author Lanxiaowei
	 * @Title: selectNodes
	 * @Description: 通过xpath获取节点列表
	 * @param node
	 * @param expression
	 * @return
	 * @throws XPathExpressionException
	 * @return NodeList
	 * @throws
	 */
	public NodeList selectNodes(Node node, String expression) {
		/*XPathExpression xpexpreesion = null;
		try {
			xpexpreesion = this.xpath.compile(expression);
			return (NodeList) xpexpreesion.evaluate(node,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Compile xpath expression occur excetion,please check out your xpath expression.");
		}*/
		try {
			return (NodeList) this.xpath.evaluate(expression,node,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectNodeList
	 * @Description: 通过xpath获取节点列表
	 * @param @param node
	 * @param @param expression
	 * @param @return
	 * @return List<Node>
	 * @throws
	 */
	public List<Node> selectNodeList(Node node, String expression) {
		NodeList nodeList = selectNodes(node, expression);
		if(null == nodeList || nodeList.getLength() == 0) {
			return null;
		}
		List<Node> list = new ArrayList<Node>();
		for(int i=0; i < nodeList.getLength(); i++) {
			list.add(nodeList.item(i));
		}
		return list;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: selectSingleNode
	 * @Description: 通过xpath获取单个节点
	 * @param node
	 * @param expression
	 * @return
	 * @return Node
	 * @throws
	 */
	public Node selectSingleNode(Node node, String expression) {
		/*XPathExpression xpexpreesion = null;
		try {
			xpexpreesion = this.xpath.compile(expression);
			return (Node) xpexpreesion.evaluate(node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Compile xpath expression occur excetion,please check out your xpath expression.");
		}*/
		try {
			return (Node) this.xpath.evaluate(expression,node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: getNodeText
	 * @Description: 根据xpath获取节点的文本值(只返回匹配的第一个节点的文本值)
	 * @param node
	 * @param expression
	 * @return
	 * @return String
	 * @throws
	 */
	public String getNodeText(Node node, String expression) {
		/*XPathExpression xpexpreesion = null;
		try {
			xpexpreesion = this.xpath.compile(expression);
			return (String) xpexpreesion.evaluate(node, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Compile xpath expression occur excetion,please check out your xpath expression.");
		}*/
		
		try {
			return (String) this.xpath.evaluate(expression,node, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getMultiNodeText
	 * @Description: 根据xpath获取节点的文本值(若xpath表达式匹配到多个节点,则会提取所有匹配到节点的文本值)
	 * @param @param node
	 * @param @param expression
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> getMultiNodeText(Node node, String expression) {
		NodeList nodeList = selectNodes(node, expression);
		if (null == nodeList || nodeList.getLength() <= 0) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			String text = n.getTextContent();
			if(text == null) {
				continue;
			}	
			list.add(text);
		}
		return list;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getNodeAttributeValue
	 * @Description: 根据xpath获取节点的属性值(若xpath表达式匹配到多个节点,则只会提取匹配到的第一个节点的属性值)
	 * @param @param node
	 * @param @param expression
	 * @param @param atrributeName
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getNodeAttributeValue(Node node,
			String expression, String atrributeName) {
		Node matchNode = selectSingleNode(node, expression);
		if (null == matchNode) {
			return null;
		}
		if(StringUtils.isEmpty(atrributeName) || DEFAULT_ATTR_NAME.equalsIgnoreCase(atrributeName)) {
			return matchNode.getTextContent();
		}
		Node attNode = matchNode.getAttributes().getNamedItem(
				atrributeName);
		if (null == attNode) {
			return null;
		}
		return attNode.getNodeValue();
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getMultiNodeAttributeValue
	 * @Description: 根据xpath获取节点的属性值(若xpath表达式匹配到多个节点,则会提取所有匹配到节点的属性值)
	 * @param @param node
	 * @param @param expression Xpath表达式,如div\span[@class]
	 * @param @param atrributeName 属性名称
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> getMultiNodeAttributeValue(Node node,
			String expression, String atrributeName) {
		NodeList nodeList = selectNodes(node, expression);
		if (null == nodeList || nodeList.getLength() == 0) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentItem = nodeList.item(i);
			if(StringUtils.isEmpty(atrributeName) || DEFAULT_ATTR_NAME.equalsIgnoreCase(atrributeName)) {
				String text = currentItem.getTextContent();
				list.add(text);
			} else {
				Node attNode = currentItem.getAttributes().getNamedItem(
						atrributeName);
				if (null == attNode) {
					continue;
				}
				String val = attNode.getNodeValue();
				list.add(val);
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		String fragment = "<link rel=\"apple-touch-icon\" href=\"/pics/movie/apple-touch-icon.png\"><data><employee><name id=\"1\">益达</name><name id=\"2\">yida</name>"
				+ "<title>Manager</title></employee></data>";
		
		
		XPathUtils util = XPathUtils.getInstance();
		// 先得到W3C Document对象
		Document doc = util.fragmentToDocument(fragment);

		List<String> strList = util.getMultiNodeText(doc,
				"//employee/name/attribute::id");
		String s = GerneralUtils.joinCollection(strList);
		System.out.println(s);

		strList = util.getMultiNodeAttributeValue(doc, "//employee/name[@id]",
				"id");
		s = GerneralUtils.joinCollection(strList);
		System.out.println(s);
	}
}
