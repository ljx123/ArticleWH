package tech.heron.interfaces;

import java.util.Map;

import tech.heron.C_S.WritableMap;
import tech.heron.exceptions.ThesisNullInputParamException;

public interface InputParamManager {
	
	//这个数组的顺序不能改变，只能在数组末尾添加
	static String[] input_params_upload = {"--filepath","--fileName","--keywords","--author","--subject"};

	
	public WritableMap getInputParamsOfOpt(String[] args) throws ThesisNullInputParamException;
	
	public boolean checkParmas(Map<String, String> paramsOfOpt, String[] params);
	
	public String getCorAuthor(Map<String, String> paramsOfOpt);
	
	public String getFirAuthor(Map<String, String> paramsOfOpt);
	
	public String getArticleNmae(Map<String, String> paramsOfOpt);
	
	public String generateHbaseColId(Map<String, String> paramsOfOpt);
	
	public String[] getThesisLabels(Map<String, String> paramsOfOpt);

	String[] getAuthors(Map<String, String> paramsOfOpt);

	String[] getSubjects(Map<String, String> paramsOfOpt);
}
