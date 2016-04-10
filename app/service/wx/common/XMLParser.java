package service.wx.common;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  Util.getStringStream(xmlString);
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;

    }
    
    public static <T> T getObjectFromXML(String xmlString, Class<T> c) {
    	
    	Method[] methods = c.getDeclaredMethods();
    	//key: 小写method
    	Map<String, Method> methodMap = new HashMap<String, Method>();
    	for (Method method : methods) {
    		if(method.getName().startsWith("set")) {
    			methodMap.put(method.getName().substring(3, method.getName().length()).toLowerCase(), method);
    		}
    	}
    	T t = null; 
    	//这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
    	try {
    		t = (T) c.newInstance();
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	InputStream is =  Util.getStringStream(xmlString);
	    	Document document = builder.parse(is);
	    	
	    	//获取到document里面的全部结点
	    	NodeList allNodes = document.getFirstChild().getChildNodes();
	    	Node node;
	    	int i=0;
	    	while (i < allNodes.getLength()) {
	    		node = allNodes.item(i);
	    		if(node instanceof Element && methodMap.get(node.getNodeName().toLowerCase()) != null){
					methodMap.get(node.getNodeName().toLowerCase()).invoke(t, node.getTextContent());
	    		}
	    		i++;
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return t;
    	
    }


}
