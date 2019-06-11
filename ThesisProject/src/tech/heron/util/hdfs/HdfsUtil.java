package tech.heron.util.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class HdfsUtil {

	private FileSystem fs;


	public static void upload(String inputPath , String hdfsPath) throws IllegalArgumentException, IOException, InterruptedException, URISyntaxException{
		FileSystem fs = FileSystem.get(new URI("hdfs://ns1/"),new Configuration(),"heron");
		FileInputStream is = new FileInputStream(new File(inputPath));
		FSDataOutputStream os = fs.create(new Path("hdfs://ns1:9000"+hdfsPath));
		IOUtils.copy(is, os);
	}
	
	public static void uploadThesisTxt(String inputPath , String hdfsPath , String ThesisName) throws IOException, InterruptedException, URISyntaxException{
		FileSystem fs = FileSystem.get(new URI("hdfs://ns1/"),new Configuration(),"heron");
		FileInputStream is = new FileInputStream(new File(inputPath));
		FSDataOutputStream os = fs.create(new Path("hdfs://ns1:9000"+hdfsPath + "/" + ThesisName + ".txt"));
		IOUtils.copy(is, os);
	}
	
	public static String uploadThesis(String inputPath,String author,String articleName) throws IOException, InterruptedException, URISyntaxException{
		
		FileSystem fs = FileSystem.get(new URI("hdfs://ns1/"),new Configuration(),"heron");
		
		String hdfsPath="/Thesis/"+author+"_"+articleName + System.currentTimeMillis() + "/" ;
		FileInputStream is = new FileInputStream(inputPath);
		Path dst = new Path(hdfsPath + articleName + ".pdf");
		fs.mkdirs(new Path(hdfsPath));
		
		FSDataOutputStream os = fs.create(dst);
		
		System.out.print(hdfsPath);
		IOUtils.copy(is, os);
		return hdfsPath;
		/////////////////////////////////////////////////
//		Configuration conf = new Configuration();
//		
//		conf.set("fs.defaultFS", "hdfs://hadoop1:9000/");
//		
//		String hdfsPath="hdfs://hadoop1:9000/Thesis/"+author+"_"+articleName;
//		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop1:9000/"),conf,"heron");
//		Path dst = new Path(hdfsPath);
//		
//		System.out.print(hdfsPath + "===" + inputPath);
//		
//		FSDataOutputStream os = fs.create(dst);
//		
//		FileInputStream is = new FileInputStream(inputPath);
//		
//		IOUtils.copy(is, os);
	}
	
	@Test
	public void download() throws IllegalArgumentException, IOException{
		fs.copyToLocalFile(false , new Path("hdfs://ns1:9000/log_raw.txt"), new Path("D:/hdp-test/testresult") , true);
	}
	
	
	public void split(){
		
		String line = "13480253104	u_flow = 3 ## d_flow = 180 ## s_flow = 183";
		String[] b = StringUtils.split(line, ' ');
		System.out.print(b);
		
	}
	
	
	public void MilisToDate(){
		long time3 = 45215712;  
        
        Date date2 = new Date();  
        date2.setTime(time3);  
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(date2));

	}
	
	@Before
	public void init() throws Exception{
		
		Configuration conf = new Configuration();
		
		conf.set("fs.defaultFS", "hdfs://hadoop1:9000/");
		
		fs = FileSystem.get(new URI("hdfs://hadoop1:9000/"),conf,"heron");
		
		
		
		
	}
	
	
	
	/**
	 * �ϴ��ļ����Ƚϵײ��д��
	 * 
	 * @throws Exception
	 */
	@Test
	public void upload() throws Exception {
//		System.setProperty("hadoop.home.dir", "E:\\hadoop-2.6.5");
//		Configuration conf = new Configuration();
//		conf.set("fs.defaultFS", "hdfs://hadoop1:9000/");
		
//		FileSystem fs = FileSystem.get(conf);
		
		Path dst = new Path("hdfs://hadoop1:9000/aa/adb用法大全.pdf");
		
		FSDataOutputStream os = fs.create(dst);
		
		FileInputStream is = new FileInputStream("E:\\学习资料\\adb用法大全.pdf");
		
		IOUtils.copy(is, os);
		

	}
	

}
