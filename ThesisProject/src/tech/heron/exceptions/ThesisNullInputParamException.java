package tech.heron.exceptions;

public class ThesisNullInputParamException extends Exception {
	
	
	NoParmaExceptions ex;
	
	public static enum NoParmaExceptions{
		NO_OPT_PARAM,NO_PARAM
	}
	
	
	
	public ThesisNullInputParamException(NoParmaExceptions ex){
//		super(throwable);
		this.ex = ex;
		
	}
	
	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		super.printStackTrace();
		switch (ex) {
		case NO_PARAM:
			
			break;

		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String message = "";
		switch (ex) {
		case NO_PARAM:
			message = "没有输入任何参数";
			break;

		default:
			break;
		}
		return super.toString();
	}
	
}
