package tech.heron.C_S;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tech.heron.exceptions.ThesisNullInputParamException;
import tech.heron.interfaces.InputParamManager;

public class InputParamManagerImpl implements InputParamManager {
	
	
	
	private static InputParamManagerImpl instance;

	public static  InputParamManagerImpl getInstance(){
		if (null == instance) { 
			synchronized (InputParamManagerImpl.class) { 
				if(null == instance) { 
					instance = new InputParamManagerImpl(); } 
			} 
		} 
			
		return instance;
		
	}
	



	@Override
	public Map<String, String> getInputParamsOfOpt(String[] args) throws ThesisNullInputParamException {
		// TODO Auto-generated method stub
		Map<String, String> map = new WritableMap();
		
		String key = null;
		String value = null;
		if(args.length == 0){
			throw new ThesisNullInputParamException(ThesisNullInputParamException.NoParmaExceptions.NO_PARAM);
		}
		
		for (int i = 1; i < args.length; i++) {
			if(i%2 == 1){
				key = args[i];
//				System.out.print(args[i] + "--key");
			}else{
				value = args[i];
				
				String returnValue = map.put(key, value);
				if(returnValue != null){
					//TODO 抛出异常，输入的键有重复
				}
//				System.out.print(args[i] + "--value");
				key = null;
				value = null;
			}
		}
		
		return map;
	}


	@Override
	public boolean checkParmas(Map<String, String> paramsOfOpt, String[] params) {
		// TODO Auto-generated method stub
		Set<String> keySet = paramsOfOpt.keySet();
		Iterator<String> iterator = keySet.iterator();
		
		while(iterator.hasNext()){
			boolean flag = false;
			String next = iterator.next();
			for (int i = 0; i < params.length; i++) {
				if(next == params[i]){
					flag = true;
				}
			}
			
			if(!flag){
				//TODO 抛异常，输入的key不符合规定
			}
		}
		return true;
	}


	@Override
	public String getCorAuthor(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		String[] split = paramsOfOpt.get("--author").split(",");
		return split[0];
	}


	@Override
	public String getFirAuthor(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		String[] split = paramsOfOpt.get("--author").split(",");
		return split[1];
	}




	@Override
	public String generateHbaseColId(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		String aticleName = getArticleNmae(paramsOfOpt);
		String FirstAuthorName = getFirAuthor(paramsOfOpt);
		String CurrentTime =""+ System.currentTimeMillis();
		return aticleName + "|" + FirstAuthorName + "|" + CurrentTime;
	}




	@Override
	public String getArticleNmae(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		return paramsOfOpt.get(input_params_upload[1]);
	}




	@Override
	public String[] getThesisLabels(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		String labels_str = paramsOfOpt.get(input_params_upload[2]);
		return labels_str.split(",");
	}



	@Override
	public String[] getAuthors(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		String authors_str = paramsOfOpt.get(input_params_upload[3]);
		return authors_str.split(",");
	}



	@Override
	public String[] getSubjects(Map<String, String> paramsOfOpt) {
		// TODO Auto-generated method stub
		String subjects_str = paramsOfOpt.get(input_params_upload[4]);
		return subjects_str.split(",");
	}


	
	
	

	

}
