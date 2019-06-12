package tech.heron.util.hbase;


import java.io.IOException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ClusterConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;

import com.sun.xml.bind.v2.runtime.output.InPlaceDOMOutput;

import tech.heron.C_S.InputParamManagerImpl;
import tech.heron.C_S.WritableMap;
import tech.heron.interfaces.InputParamManager;

public class HbaseUtil {

	public Configuration config;
	private final String TABLE_NAME = "Thesis";
	private String[] columnFamilies={"ThesisName","lables","author","creation_time","creation_user","subject","hdfsAddr","extra"};
	
//    static { 
////    	 //Add any necessary configuration files (hbase-site.xml, core-site.xml)
////    	 createSchemaTables(config);
////    	 modifySchema(config);
//    	try {
//    		config = HBaseConfiguration.create();
//    		config.addResource(new Path("hbase-site.xml"));
//    		config.addResource(new Path("core-site.xml"));
//			createTable();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }	
	
	public HbaseUtil(){
		init();
	}
	
	private void init(){
		config = HBaseConfiguration.create();
		config.addResource(new Path("hbase-site.xml"));
		config.addResource(new Path("core-site.xml"));
		try {
			createTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    
    //创建表
    @SuppressWarnings("deprecation")
	public void createTable() throws IOException {
    	String tableName = TABLE_NAME;
    	Connection connection = ConnectionFactory.createConnection(config);
    	Admin admin = connection.getAdmin();
    	HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));
    	
    	if (admin.tableExists(table.getTableName())) {
    		return;
    	}
    	
    	//设定列族
    	for (int i = 0; i < columnFamilies.length; i++) {
			
    		table.addFamily(new HColumnDescriptor(columnFamilies[i]).setCompressionType(Algorithm.NONE));
		}
    	
    	//创建表
    	admin.createTable(table);
    	admin.close();
    	connection.close();
    	
    	
    }
    
    public void insertOneThesisRecord(WritableMap paramsOfOpt , String hdfsAddr){
    	//先生成一个唯一id
    	String ColumnId = InputParamManagerImpl.getInstance().generateHbaseColId(paramsOfOpt);
    	//根据id生成一个put实例
    	Put put = new Put(ColumnId.getBytes());
    	//加入文章名称信息
    	put.addColumn(columnFamilies[0].getBytes(), "value".getBytes(), InputParamManagerImpl.getInstance().getArticleNmae(paramsOfOpt).getBytes());
    	//加入文章关键字信息
    	String[] labels = InputParamManagerImpl.getInstance().getThesisLabels(paramsOfOpt);
    	for (int i = 0; i < labels.length; i++) {
    		put.addColumn(columnFamilies[1].getBytes(), ("value"+i).getBytes(), labels[i].getBytes());
		}
    	//加入文章作者信息 通讯作者，第一作者，第二作者。。。。。。
    	String[] authors = InputParamManagerImpl.getInstance().getAuthors(paramsOfOpt);
    	for (int i = 0; i < authors.length; i++) {
    		put.addColumn(columnFamilies[2].getBytes(), ("value"+i).getBytes(), labels[i].getBytes());
		}
    	//加入文章涉及的学科
    	String[] subjects = InputParamManagerImpl.getInstance().getSubjects(paramsOfOpt);
    	for (int i = 0; i < authors.length; i++) {
    		put.addColumn(columnFamilies[5].getBytes(), ("value"+i).getBytes(), labels[i].getBytes());
		}
    	
    	//加入文章上传时间
    	String updateTime = ColumnId.split("|")[2];
    	put.addColumn(columnFamilies[3].getBytes(), "value".getBytes(), updateTime.getBytes());
    	
    	//加入文章在hdfs中的保存位置
    	put.addColumn(columnFamilies[6].getBytes(), "value".getBytes(), hdfsAddr.getBytes());
    	Connection connection;
		try {
			connection = ConnectionFactory.createConnection(config);
			Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
			table.put(put);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
    	
    	
    }
    
    //根据文章名查询
    
    public ArrayList<Result> scanByArticleName(String articleNmae){
    	
    	
    	
    	ArrayList<Result> resultList = new ArrayList<Result>();
    	
    	try {
			Connection connection = ConnectionFactory.createConnection(config);
			Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
			List<Filter> filters = new ArrayList<Filter>();
			Filter filter1 = new SingleColumnValueFilter(Bytes.toBytes("ThesisName"), Bytes.toBytes("value"), CompareOp.EQUAL,Bytes.toBytes(articleNmae));
			filters.add(filter1);
			FilterList filterList = new FilterList(filters);
			Scan scan = new Scan();
			scan.setFilter(filterList);
			ResultScanner scanner = table.getScanner(scan);
			for (Result result : scanner) {
				resultList.add(result);
				
				////////////////////////////////
//				for (Cell keyValue : result.rawCells()) {  
//					System.out.println("列：" + new String(keyValue.getFamilyArray())  
//					+ "====值:" + new String(keyValue.getValueArray()));  
//				} 
				/////////////////////
			}
			System.out.print("resultlist.size()=" + resultList.size());
			scanner.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return resultList;
    }
    
    
	
}
