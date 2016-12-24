package com.topeet.serialtest;


//
public class serial {

	static {
		System.loadLibrary("pl2303_driver");
	}
	//flag=0 flag=1;
	public native int 	Open(int Port,int Rate,int flag);
	public native int 	Close(int flag);
	public native int[]	Read(int flag);
	public native int	Write(int[] buffer,int len,int flag);

} 
 