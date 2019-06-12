package tech.heron.C_S;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Writable;

public class WritableMap extends HashMap<String, String> implements Writable {

	
	public WritableMap() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		String string = this.toString();
		System.out.print("========" + string + "=========\n");
		out.writeUTF(string);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		String readLine = in.readUTF();
		String keyValues = readLine.substring(1, readLine.length()-1);
		String[] split = keyValues.split(", ");
		for (int i = 0; i < split.length; i++) {
			String[] split2 = split[i].split("=");
			this.put(split2[0], split2[1]);
		}
	}

}
