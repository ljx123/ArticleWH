package tech.heron.interfaces;

import java.util.Map;

import tech.heron.exceptions.ThesisNullInputParamException;

public interface InputParamManager {

	public String getFirstAuthor();
	
	public Map<String, String> getInputParamsOfOpt(String[] args) throws ThesisNullInputParamException;
	
	public boolean checkParmas(Map<String, String> paramsOfOpt, String[] params);
	
	public String getCorAuthor(Map<String, String> paramsOfOpt);
	
	public String getFirAuthor(Map<String, String> paramsOfOpt);
}
