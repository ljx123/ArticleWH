package tech.heron.util.hbase;


import java.io.IOException;


import java.util.ArrayList;
import java.util.List;

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

public class HbaseUtil {

	public static Configuration config = HBaseConfiguration.create();
	private static final String TABLE_NAME = "Theses";
	private static String[] columnFamilies={"ThesisName","lables","author","creation_time","creation_user","subject","extra"};
	
    static { 
//    	 //Add any necessary configuration files (hbase-site.xml, core-site.xml)
    	config.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"));
    	config.addResource(new Path(System.getenv("HADOOP_CONF_DIR"), "core-site.xml"));
//    	 createSchemaTables(config);
//    	 modifySchema(config);
    	try {
			createTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }	
    
    //创建表
    @SuppressWarnings("deprecation")
	public static void createTable() throws IOException {
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
    
    //根据文章名查询
    
    public static ArrayList<Result> scanByArticleName(String articleNmae){
    	
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
				if(result == null)
					break;
				resultList.add(result);
				////////////////////////////////
				for (Cell keyValue : result.rawCells()) {  
	                System.out.println("列：" + new String(keyValue.getFamilyArray())  
	                        + "====值:" + new String(keyValue.getValueArray()));  
	            } 
				/////////////////////
				
			}
			scanner.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return resultList;
    }
    
    
	
}
