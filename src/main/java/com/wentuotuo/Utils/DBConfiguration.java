package com.wentuotuo.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DBConfiguration {
	public static Logger logger = Logger.getLogger(DBConfiguration.class);

	private String driver;
	private String url;
	private String user;
	private String password;
	private String path = "etc/dbconfig.xml";

	/**
	 * 默认数据库构造方法
	 * <li>读取默认路径etc/dbconfig.xml，解析XML获取其中数据库链接信息
	 */
	public DBConfiguration() {
		parseXML();
	}

	/**
	 * 读取xmlPath路径下的配置文件，解析并获取数据库链接信息
	 * 
	 * @param xmlPath
	 *            数据库配置文件路径
	 */
	public DBConfiguration(String xmlPath) {
		path = xmlPath;
		parseXML();
	}

	/**
	 * 解析xml
	 */
	private void parseXML() {
		SAXReader reader = new SAXReader();// 创建SAXReader对象
		try {
			Document doc = reader.read(new File(path));// 通过read方法读取一个文件
														// 转换成Document对象
			Element node = doc.getRootElement();// 获取根节点元素对象
			// listNodes(node);// 遍历所有的元素节点

			// Element element = node.element("driver");
			driver = node.element("driver").getStringValue();

			url = node.element("url").getStringValue();

			user = node.element("user").getStringValue();

			password = node.element("password").getStringValue();
		} catch (DocumentException e) {
			logger.error("读取数据库配置文件出错");
			e.printStackTrace();
		}
	}

	/**
	 * 遍历当前节点元素下面的所有(元素的)子节点
	 * 
	 * @param node
	 */
	public void listNodes(Element node) {
		System.out.println("当前节点的名称：：" + node.getName());
		// 获取当前节点的所有属性节点
		List<Attribute> list = node.attributes();
		// 遍历属性节点
		for (Attribute attr : list) {
			System.out.println(attr.getText() + "-----" + attr.getName() + "---" + attr.getValue());
		}

		if (!(node.getTextTrim().equals(""))) {
			System.out.println("文本内容：：：：" + node.getText());
		}

		// 当前节点下面子节点迭代器
		Iterator<Element> it = node.elementIterator();
		// 遍历
		while (it.hasNext()) {
			// 获取某个子节点对象
			Element e = it.next();
			// 递归对子节点进行遍历
			listNodes(e);
		}
	}

	/**
	 * 获取数据库连接对象
	 * 
	 * @return Connection对象
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.error("获取数据库链接出错");
			e.printStackTrace();
		}

		return conn;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPath() {
		return path;
	}
}
