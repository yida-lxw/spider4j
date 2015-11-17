package com.yida.spider4j.crawler.selector;

import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.w3c.dom.Node;

import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: Html
 * @Description: HTML
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月9日 上午8:57:39
 *
 */
public class Html extends HtmlNode {

	private static volatile boolean INITED = false;

	/**禁止对HTML实体中特殊字符进行转义,如&><"'等特殊字符*/
	public static boolean DISABLE_HTML_ENTITY_ESCAPE = true;
	
    private org.jsoup.nodes.Document document;
    
    private org.w3c.dom.Document w3cDocument;

	/**禁用jsoup的HTML实体转义,只针对jsoup 1.7.2*/
	private void disableJsoupHtmlEntityEscape() {
		if (DISABLE_HTML_ENTITY_ESCAPE && !INITED) {
			Entities.EscapeMode.base.getMap().clear();
			Entities.EscapeMode.extended.getMap().clear();
			INITED = true;
		}
	}
	
	public Html(String text) {
		// for jsoup
        try {
			disableJsoupHtmlEntityEscape();
            this.document = Jsoup.parse(text);
            this.elements = getElements();
        } catch (Exception e) {
            this.document = null;
            LogUtils.warn("parse document error:\n" + e.getMessage());
        }
        
        /*if(this.elements == null || this.elements.size() <= 0) {
        	this.w3cDocument = xpathUtils.fragmentToDocument(text);
        	this.nodes = getNodes();
        }*/
        
        this.w3cDocument = xpathUtils.fragmentToDocument(text);
    	this.nodes = getNodes();
    }

    public Html(String text,ExpressionType type) {
    	/*if(ExpressionType.XPATH.equals(type) || ExpressionType.REGEX.equals(type)) {
    		// for xpath
    		this.w3cDocument = xpathUtils.fragmentToDocument(text);
        	this.nodes = getNodes();
    	} else if(ExpressionType.JSOUP.equals(type)) {
    		// for jsoup
            try {
    			disableJsoupHtmlEntityEscape();
                this.document = Jsoup.parse(text);
                this.elements = getElements();
            } catch (Exception e) {
                this.document = null;
                LogUtils.warn("parse document error:\n" + e.getMessage());
            }
    	}*/
    	
    	this.w3cDocument = xpathUtils.fragmentToDocument(text);
    	this.nodes = getNodes();
    	
    	try {
			disableJsoupHtmlEntityEscape();
            this.document = Jsoup.parse(text);
            this.elements = getElements();
        } catch (Exception e) {
            this.document = null;
            LogUtils.warn("parse document error:\n" + e.getMessage());
        }
    }
    
    public Html(org.jsoup.nodes.Document document){
    	this.document = document;
    }
    
    public Html(org.w3c.dom.Document w3cDocument){
    	this.w3cDocument = w3cDocument;
    }
    
    public static Html create(String text,ExpressionType type) {
        return new Html(text,type);
    }

    @Override
    protected List<Element> getElements() {
        return Collections.<Element>singletonList(getDocument());
    }
    
    @Override
    protected List<Node> getNodes() {
    	return Collections.<Node>singletonList(getW3cDocument());
    }

    /**
     * @param selector
     * @return
     */
    public String selectDocument(Selector selector) {
        if (selector instanceof ElementSelector) {
            ElementSelector elementSelector = (ElementSelector) selector;
            return elementSelector.select(getDocument());
        } 
        if (selector instanceof NodeSelector) {
        	NodeSelector nodeSelector = (NodeSelector) selector;
            return nodeSelector.select(getW3cDocument());
        } 
        if (selector instanceof PlainTextSelector) {
        	PlainTextSelector plainTextSelector = (PlainTextSelector) selector;
            return plainTextSelector.select(getFirstSourceText());
        } 
        return null;
    }

    public List<String> selectDocumentForList(Selector selector) {
    	if (selector instanceof ElementSelector) {
            ElementSelector elementSelector = (ElementSelector) selector;
            return elementSelector.selectList(getDocument());
        } 
        if (selector instanceof NodeSelector) {
        	NodeSelector nodeSelector = (NodeSelector) selector;
            return nodeSelector.selectList(getW3cDocument());
        } 
        if (selector instanceof PlainTextSelector) {
        	PlainTextSelector plainTextSelector = (PlainTextSelector) selector;
            return plainTextSelector.selectList(getFirstSourceText());
        } 
        return null;
    }

	public org.jsoup.nodes.Document getDocument() {
		return document;
	}

	public void setDocument(org.jsoup.nodes.Document document) {
		this.document = document;
	}

	public org.w3c.dom.Document getW3cDocument() {
		return w3cDocument;
	}

	public void setW3cDocument(org.w3c.dom.Document w3cDocument) {
		this.w3cDocument = w3cDocument;
	}
}
