package com.mbp.eng.framework.db.jdbc.pool.type1;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author ChenPei
 * 操作配置文件类 读  写 修改 删除等操作
 * 操作多(这个'多'包括不同的数据库和同一种数据库有多个数据库)数据 配置文件xml
 */
public class ParseDSConfig {
    private static final Logger logger = LoggerFactory.getLogger(ParseDSConfig.class);

    /**
     * 构造函数
     */
    public ParseDSConfig() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 读取xml配置文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public Vector readConfigInfo(String path) {
        File file = new File(path);
        String filePath = "";
        if (!file.exists()) {
            //FilePath = this.getClass().getResource("").getPath().substring(1) + path;
            filePath = this.getClass().getResource("/").getPath() + path;
        } else {
            try {
                filePath = file.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //String rpath = this.getClass().getResource("").getPath().substring(1) + path;
        String rpath = filePath;
        Vector vector = null;
        FileInputStream fileInputStream = null;
        try {
            // 读取路径文件
            fileInputStream = new FileInputStream(rpath);
            vector = new Vector();
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(fileInputStream);
            Element element = document.getRootElement();
            List list = element.getChildren();
            Element element1 = null;
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                element1 = (Element) iterator.next();
                DSConfigBean dsConfigBean = new DSConfigBean();
                dsConfigBean.setType(element1.getChild("type").getText());
                dsConfigBean.setName(element1.getChild("name").getText());
                //System.out.println(dsConfigBean.getName());
                dsConfigBean.setDriver(element1.getChild("driver").getText());
                dsConfigBean.setUrl(element1.getChild("url").getText());
                dsConfigBean.setUsername(element1.getChild("username").getText());
                dsConfigBean.setPassword(element1.getChild("password").getText());
                dsConfigBean.setMaxconn(Integer.parseInt(element1.getChild("maxconn").getText()));
                vector.add(dsConfigBean);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return vector;
    }

    /**
     * 修改配置文件 没时间写 过段时间再贴上去 其实一样的
     */
    public void modifyConfigInfo(String path, DSConfigBean dsb) throws Exception {
        String rpath = this.getClass().getResource("").getPath().substring(1) + path;
        //读出
        FileInputStream fileInputStream = null;
        //写入
        FileOutputStream fileOutputStream = null;
    }

    /**
     * 增加配置文件
     */
    public void addConfigInfo(String path, DSConfigBean dsConfigBean) {
        String rpath = this.getClass().getResource("").getPath().substring(1) + path;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 读取xml流
            fileInputStream = new FileInputStream(rpath);

            SAXBuilder saxBuilder = new SAXBuilder();

            //得到xml
            Document document = saxBuilder.build(fileInputStream);
            Element element = document.getRootElement();
            //得到xml子树
            List list = element.getChildren();

            //创建新连接池
            Element element1 = new Element("pool");

            //设置连接池类型
            Element element2 = new Element("type");
            element2.setText(dsConfigBean.getType());
            element1.addContent(element2);

            //设置连接池名字
            Element element3 = new Element("name");
            element3.setText(dsConfigBean.getName());
            element1.addContent(element3);

            //设置连接池驱动
            Element element4 = new Element("driver");
            element4.addContent(dsConfigBean.getDriver());
            element1.addContent(element4);

            //设置连接池url
            Element element5 = new Element("url");
            element5.setText(dsConfigBean.getUrl());
            element1.addContent(element5);

            //设置连接池用户名
            Element poolusername = new Element("username");
            poolusername.setText(dsConfigBean.getUsername());
            element1.addContent(poolusername);

            //设置连接池密码
            Element element6 = new Element("password");
            element6.setText(dsConfigBean.getPassword());
            element1.addContent(element6);

            //设置连接池最大连接
            Element element7 = new Element("maxconn");
            element7.setText(String.valueOf(dsConfigBean.getMaxconn()));
            element1.addContent(element7);
            //将child添加到root
            list.add(element1);
            Format format = Format.getPrettyFormat();
            format.setIndent("");
            format.setEncoding("utf-8");
            XMLOutputter xmlOutputter = new XMLOutputter(format);
            fileOutputStream = new FileOutputStream(rpath);
            xmlOutputter.output(document, fileOutputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }/* finally {
        }*/
    }

    /**
     * 删除配置文件
     */
    public void delConfigInfo(String path, String name) {
        String rpath = this.getClass().getResource("").getPath().substring(1) + path;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //读取路径文件
            fileInputStream = new FileInputStream(rpath);
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(fileInputStream);
            Element element = document.getRootElement();
            List list = element.getChildren();
            Element element1 = null;
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                element1 = (Element) iterator.next();
                if (element1.getChild("name").getText().equals(name)) {
                    list.remove(element1);
                    break;
                }
            }
            Format format = Format.getPrettyFormat();
            format.setIndent("");
            format.setEncoding("utf-8");
            XMLOutputter xmlOutputter = new XMLOutputter(format);
            fileOutputStream = new FileOutputStream(rpath);
            xmlOutputter.output(document, fileOutputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        ParseDSConfig parseDSConfig = new ParseDSConfig();
        String path = "data-source.xml";
        parseDSConfig.readConfigInfo(path);
        // pd.delConfigInfo(path, "tj012006");
        DSConfigBean dsConfigBean = new DSConfigBean();
        dsConfigBean.setType("hive");
        dsConfigBean.setName("yyy004");
        dsConfigBean.setDriver("org.apache.hive.jdbc.HiveDriver");
        dsConfigBean.setUrl("jdbc:hive2://10.71.5.3:10000/default");
        dsConfigBean.setUsername("ocdp");
        dsConfigBean.setPassword("ocdp");
        dsConfigBean.setMaxconn(1000);
        parseDSConfig.addConfigInfo(path, dsConfigBean);
        parseDSConfig.delConfigInfo(path, "yyy001");
    }
}