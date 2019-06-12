package tech.heron.C_S;

import java.io.IOException;
import java.util.Map;

public interface ThesisProjectInterface {
	public static final long versionID = 1L;
//	public String test();
	
	
	/*
	 * 返回true代表库中没有同样名称的论文
	 * 返回false则相反
	 * */
	public boolean Service_check_upload_params(String thesisName);
	
	public boolean saveThesisMessageToHbase(WritableMap paramsOfOpt , String hdfsAddr) throws IOException;
}
