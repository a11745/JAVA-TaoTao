package com.taotao.freemaker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreeMaker {
	
	@Test
	public void testFreemaker() throws Exception{
		//1.创建一个模板文件
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3.设置模板所在路径
		configuration.setDirectoryForTemplateLoading(new File("D:/JavaTaoTao/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
		//4.蛇者模板的字符集
		configuration.setDefaultEncoding("utf-8");
		//5.使用Configuration对象家在一个模板文件，需要指定模板文件的文件名
		///Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		//6.创建一个数据集，可以是pojo也可以是map，推荐使用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker");
		Student student = new Student(1,"小米",11,"北京");
		data.put("student", student);
		//7.创建一个Writer对象，指定输出文件的路径及文件名
		Writer writer = new FileWriter(new File("D:/东西/html/student.html"));
		//8.使用模板对象的process方法输出文件
		template.process(data, writer);
		//9.关闭流
		writer.close();
		
		
	}
}
