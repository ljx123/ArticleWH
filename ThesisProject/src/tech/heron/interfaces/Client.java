package tech.heron.interfaces;

public interface Client {

	//这个数组的顺序不能改变，只能在数组末尾添加
	static String[] input_params_upload = {"--filepath","--fileName","--keywords","--author"}; 
}
