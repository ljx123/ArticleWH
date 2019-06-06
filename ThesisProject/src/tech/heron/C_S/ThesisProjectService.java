package tech.heron.C_S;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;

import tech.heron.util.hbase.HbaseUtil;

public class ThesisProjectService{
	
	public static void main(String[] args) {
		try {
			Server server;
			server = new RPC.Builder(new Configuration())        
			.setBindAddress("192.168.64.11")
			.setPort(10000)
			.setProtocol(ThesisProjectInterface.class)
			.setInstance(new ThesisProjectInterfaceImpl())
			.build();
			server.start();
		} catch (HadoopIllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	

}
