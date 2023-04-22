/*
 * Copyright (c) 2015, Xinlong.inc and/or its affiliates. All rights reserved.
 */
package com.yida.spider4j.crawler.utils.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.yida.spider4j.crawler.utils.common.GerneralUtils;

/**
 * XML常用操作工具类
 * 
 * @since 1.0
 * @author Lanxiaowei@citic-finance.com
 * @date 2015-6-16下午3:39:10
 * 
 */
public class XMLUtils {
	private DocumentBuilder builder;

	private XPath xpath;

	private XMLUtils() {
		init();
	}

	private static class SingletonHolder {
		private static final XMLUtils INSTANCE = new XMLUtils();
	}

	public static final XMLUtils getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private void init() {
		if (builder == null) {
			DocumentBuilderFactory domfactory = DocumentBuilderFactory
					.newInstance();
			// 禁言HTML标签语法验证
			domfactory.setValidating(false);
			domfactory.setIgnoringComments(true);
			domfactory.setNamespaceAware(false);
			domfactory.setIgnoringElementContentWhitespace(true);
			try {
				builder = domfactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw new RuntimeException(
						"Create DocumentBuilder instance occur one exception.");
			}
		}

		if (xpath == null) {
			XPathFactory xpfactory = XPathFactory.newInstance();
			xpath = xpfactory.newXPath();
		}
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: document2String
	 * @Description: W3C Document对象转成XML String
	 * @param @param doc
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String document2String(Document doc) {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (TransformerException e) {
			throw new RuntimeException(
				"Transformer org.w3c.dom.document object occur one exception.");
		}
		return writer.toString();
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: parseDocument
	 * @Description: 根据XML路径解析XML文档
	 * @param path
	 * @return
	 * @return Document
	 * @throws
	 */
	public Document parseDocument(String path) {
		try {
			return builder.parse(path);
		} catch (SAXException e) {
			throw new RuntimeException(
					"The xml path is invalid or parsing xml occur exception.");
		} catch (IOException e) {
			throw new RuntimeException(
					"The xml path is invalid or parsing xml occur exception.");
		}
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: parseDocument
	 * @Description: 根据文件解析XML文档
	 * @param file
	 * @return
	 * @return Document
	 * @throws
	 */
	public Document parseDocument(File file) {
		try {
			return builder.parse(file);
		} catch (SAXException e) {
			throw new RuntimeException(
					"The input xml file is null or parsing xml occur exception.");
		} catch (IOException e) {
			throw new RuntimeException(
					"The input xml file is null or parsing xml occur exception.");
		}

	}

	/**
	 * @Author Lanxiaowei
	 * @Title: parseDocument
	 * @Description: 根据输入流解析XML文档
	 * @param is
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @return Document
	 * @throws
	 */
	public Document parseDocument(InputStream is) {
		try {
			return builder.parse(is);
		} catch (SAXException e) {
			throw new RuntimeException(
					"The input xml fileInputStream is null or parsing xml occur exception.");
		} catch (IOException e) {
			throw new RuntimeException(
					"The input xml fileInputStream is null or parsing xml occur exception.");
		}
	}

	/**
	 * @throws IOException 
	 * @throws SAXException 
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: fragment2Document
	 * @Description: 将html代码片段转换成document对象
	 * @param @param fragment
	 * @param @return
	 * @return Document
	 * @throws
	 */
	public Document fragment2Document(String fragment) throws SAXException, IOException {
		/*try {
			return builder.parse(new InputSource(new StringReader(fragment)));
		} catch (SAXException e) {
			throw new RuntimeException(
					"parse fragment to document occur SAXException,please check your fragment.");
		} catch (IOException e) {
			throw new RuntimeException(
					"parse fragment to document occur one IOException.");
		}*/
		
		return builder.parse(new InputSource(new StringReader(fragment)));
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
		XPathExpression xpexpreesion = null;
		try {
			xpexpreesion = this.xpath.compile(expression);
			return (NodeList) xpexpreesion.evaluate(node,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Compile xpath expression occur excetion,please check out your xpath expression.");
		}
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
		XPathExpression xpexpreesion = null;
		try {
			xpexpreesion = this.xpath.compile(expression);
			return (Node) xpexpreesion.evaluate(node, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Compile xpath expression occur excetion,please check out your xpath expression.");
		}
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
		XPathExpression xpexpreesion = null;
		try {
			xpexpreesion = this.xpath.compile(expression);
			return (String) xpexpreesion.evaluate(node, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(
					"Compile xpath expression occur excetion,please check out your xpath expression.");
		}
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
			Node attNode = currentItem.getAttributes().getNamedItem(
					atrributeName);
			if (null == attNode) {
				continue;
			}
			String val = attNode.getNodeValue();
			list.add(val);
		}
		return list;
	}

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException, TransformerException {

		/*
		 * String fragment = "<data><employee><name>益达</name>" +
		 * "<title>Manager</title></employee></data>";
		 * 
		 * XMLUtils util = new XMLUtils(); Document doc =
		 * util.fragment2Document(fragment); NodeList nodes =
		 * doc.getElementsByTagName("employee");
		 * 
		 * for (int i = 0; i < nodes.getLength(); i++) { Element element =
		 * (Element) nodes.item(i);
		 * 
		 * NodeList name = element.getElementsByTagName("name"); Element line =
		 * (Element) name.item(0); System.out.println("Name: " +
		 * line.getNodeName() + ":" + line.getTextContent());
		 * 
		 * NodeList title = element.getElementsByTagName("title"); line =
		 * (Element) title.item(0); System.out.println("Name: " +
		 * line.getNodeName() + ":" + line.getTextContent()); }
		 */

		String fragment = "<link rel=\"apple-touch-icon\" href=\"/pics/movie/apple-touch-icon.png\"><data><employee><name id=\"1\">益达</name><name id=\"2\">yida</name>"
				+ "<title>Manager</title></employee></data>";
		
		//HTML未闭合标签修复，如果html标签未闭合的话，Xpath查询会报错
		//fragment = TagsChecker.fix(fragment);
		
		
		XMLUtils util = new XMLUtils();
		// 先得到W3C Document对象
		Document doc = util.fragment2Document(fragment);

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
