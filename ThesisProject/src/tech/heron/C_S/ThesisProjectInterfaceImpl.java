package tech.heron.C_S;

import java.util.ArrayList;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;

import tech.heron.util.hbase.HbaseUtil;

public class ThesisProjectInterfaceImpl implements ThesisProjectInterface {



	@Override
	public boolean Service_check_upload_params(String thesisName) {
		ArrayList<Result> scanByArticleName = new HbaseUtil().scanByArticleName(thesisName);
		
		return scanByArticleName.size()>0?false:true;
	}

	@Override
	public boolean saveThesisMessageToHbase(Map<String, String> paramsOfOpt,
			String hdfsAddr) {
		new HbaseUtil().insertOneThesisRecord(paramsOfOpt, hdfsAddr);
		return false;
	}



}
