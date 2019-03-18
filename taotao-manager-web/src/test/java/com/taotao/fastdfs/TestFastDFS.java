package com.taotao.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.utils.FastDFSClient;




public class TestFastDFS {
	
	@Test
	public void uploadFile() throws Exception{
		//1.添加jar包
		//2.创建配置文件，创建tracker服务器的地址
		
		//3.加载配置文件
		ClientGlobal.init("D:/JavaTaoTao/taotao-manager-web/src/main/resources/resource/client.conf");
		//4.创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//5.使用TrackerClient创获得trackerserver对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//6.创建一个StorageServer的引用null就可以
		StorageServer storageServer = null;
		//7.创建一个StrogeClient对象，trackerserver、StorageServer两个参数
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		//8.使用StrogeClient对象上传文件
		String[] strings = storageClient.upload_file("C:/Users/Administrator/Desktop/aaa.jpg", "jpg",null);
		for (String string : strings) {
			System.out.println(string);
		}
		
	}
	@Test
	public void testFastDfsClient() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("D:/JavaTaoTao/taotao-manager-web/src/main/resources/resource/client.conf");
		String string = fastDFSClient.uploadFile("C:/Users/Administrator/Desktop/新建文件夹/u=4001595143,477774509&fm=27&gp=0.jpg");
		System.out.println(string);
	}
}
