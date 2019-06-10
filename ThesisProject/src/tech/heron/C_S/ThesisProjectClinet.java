package tech.heron.C_S;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import tech.heron.exceptions.*;
import tech.heron.interfaces.Client;
import tech.heron.util.hdfs.*;


public class ThesisProjectClinet {
	
	static ThesisProjectInterface proxy;
	
	private String[] input_params_opts = {"-upload"};
	
//	private static String[] input_params_upload = {"--filepath","--fileName","--keywords","--author"}; 
	
	public static void main(String[] args) {
		//判断是什么操作，将操作后的参数解析出来
		System.out.print(args[0]);
		try {
			Map<String, String> paramsOfOpt = InputParamManagerImpl.getInstance().getInputParamsOfOpt(args);
			switch (args[0]) {
			case "-upload":
				System.out.print("dddddddd");
				uploadThesisPdf(paramsOfOpt);
				break;
				
			default:
				break;
			}
		} catch (ThesisNullInputParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	private static void uploadThesisPdf(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		InputParamManagerImpl.getInstance().checkParmas(paramsOfOpt , Client.input_params_upload);
		//读取输入参数中的filepath，没有filepath也要报错
		if(!paramsOfOpt.keySet().contains("--filepath") || !new File(paramsOfOpt.get("--filepath")).exists() || !paramsOfOpt.get("--filepath").endsWith("pdf")){
			//TODO 报错提示输入的params没有path或者path错误
		}
		//TODO 服务端检查输入参数
		try {
			boolean Thesis_exist = getProxy().Service_check_upload_params(paramsOfOpt.get("--fileName"));
			//如果库中已有同名文章，询问客户是否还要继续上传
			if(!Thesis_exist){
				Scanner sc = new Scanner(System.in); 
		         System.out.println("有同名文章，是否继续上传，输入y继续，其它任意键结束"); 
		         String dicision = sc.nextLine(); 
		         if(!"y".equals(dicision)){
		        	 return;
		         }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//读取filepath中的pdf文件，使用hdfs的api上传到hdfs集群中,并在hbase中写入相应信息，这两者要有一致性
		try {
//			System.out.print("开始传输文件");
//			System.out.print(paramsOfOpt);
			HdfsUtil.uploadThesis(paramsOfOpt.get(Client.input_params_upload[0]), InputParamManagerImpl.getInstance().getFirAuthor(paramsOfOpt), paramsOfOpt.get("--fileName"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



//	private static boolean checkParmas(Map<String, String> paramsOfOpt, String[] params) {
//		// TODO Auto-generated method stub
//		Set<String> keySet = paramsOfOpt.keySet();
//		Iterator<String> iterator = keySet.iterator();
//		
//		while(iterator.hasNext()){
//			boolean flag = false;
//			String next = iterator.next();
//			for (int i = 0; i < params.length; i++) {
//				if(next == params[i]){
//					flag = true;
//				}
//			}
//			
//			if(!flag){
//				//TODO 抛异常，输入的key不符合规定
//			}
//		}
//		return true;
//	}



//	private static Map<String, String> getInputParamsOfOpt(String[] args) throws ThesisNullInputParamException {
//		// TODO Auto-generated method stub
////		for (int i = 0; i < args.length; i++) {
////			System.out.print(args[i] + " ");
////		}
//		Map<String, String> map = new HashMap<String, String>();
//		
//		String key = null;
//		String value = null;
//		if(args.length == 0){
//			throw new ThesisNullInputParamException(ThesisNullInputParamException.NoParmaExceptions.NO_PARAM);
//		}
//		
//		for (int i = 1; i < args.length; i++) {
//			if(i%2 == 1){
//				key = args[i];
////				System.out.print(args[i] + "--key");
//			}else{
//				value = args[i];
//				
//				String returnValue = map.put(key, value);
//				if(returnValue != null){
//					//TODO 抛出异常，输入的键有重复
//				}
////				System.out.print(args[i] + "--value");
//				key = null;
//				value = null;
//			}
//		}
//		
//		return map;
//	}



	private static ThesisProjectInterface getProxy() throws IOException{
		
		if(null == proxy){
			proxy = RPC.getProxy(ThesisProjectInterface.class, 1L,  new InetSocketAddress("hadoop1",10000), new Configuration());
		}
		
		return proxy;
		
	}
}
