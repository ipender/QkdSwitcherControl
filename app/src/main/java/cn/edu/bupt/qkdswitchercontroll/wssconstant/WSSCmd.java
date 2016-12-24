package cn.edu.bupt.qkdswitchercontroll.wssconstant;

public class WSSCmd {

	/**
	 * 指令名称：SNO?
	 * 发送格式：SNO?  
	 * 功能：查看串口配置号
	 * 返回参数格式：SNO39277
	 *            OK
	 *    错误信息：
	 **/
	public final  static String  COMMEND_SN0="SNO?"+"\n";
	/**
	 * 指令名称：URA 
	 * 发送格式：URA 1,1,1.0;2,2,2.0
	 * 功能：将channel 1 to port 1 with 1 dB attenuation
	 * 返回参数格式：URA
	 *            ok
	 **/
	public final static String COMMEND_URA="URA ";
	/**
	 * 指令名称：RSW
	 * 发送格式：RSW
	 * 功能：update WSS configuration,一般跟在URA指令后使用
	 * 返回参数格式:RSW 
	 *           OK
	 **/
	public final static String COMMEND_RSW="RSW"+"\n";
	/**
	 * 指令名称：RRA?
	 * 发送格式：RRA?
	 * 功能：Read Back the new channel plan from the WSS 
	 * 返回参数格式:RRA?
	 *           xxxxxxxxxxxxxxxxxxxxxxx
	 *           xxxxxxxxxxxxxxxxxxxxxxx
	 *           OK
	 **/
	public final static String COMMEND_RRA="RRA?"+"\n";
	
	/*
	 * WSS复位指令
	 * 正确返回OK
	 */
	public static final String COMMEND_RES = "RES"+"\n";
	/**
	 * 所用光速换算数值299792.50
	 */
    public final static double SPEED_CONSTANT=299792.5;


}
