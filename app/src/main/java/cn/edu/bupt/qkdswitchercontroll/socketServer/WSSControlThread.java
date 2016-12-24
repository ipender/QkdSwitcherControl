package cn.edu.bupt.qkdswitchercontroll.socketServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.bool;
import android.R.integer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.topeet.serialtest.Com;
import com.topeet.serialtest.serial;

import cn.edu.bupt.qkdswitchercontroll.constant.ServerConstant;
import cn.edu.bupt.qkdswitchercontroll.constant.SignalConstant;
import cn.edu.bupt.qkdswitchercontroll.constant.ViewConstant;
import cn.edu.bupt.qkdswitchercontroll.device.WSSSetup;
import cn.edu.bupt.qkdswitchercontroll.httpServer.CommunicateObject;
import cn.edu.bupt.qkdswitchercontroll.httpServer.HttpServer;
import cn.edu.bupt.qkdswitchercontroll.view.LineUsing;
import cn.edu.bupt.qkdswitchercontroll.wssconstant.WSSCmd;

//与WSS交互类
public class WSSControlThread implements Runnable {

	private static final String TAG = "SocketThread";
	public  serial SWSS2 = new serial();
	public  serial SWSS1 = new serial();

	// 设置一个保存链路信息的public ArrayList<> msg;
	public void init() {

		int fd2= SWSS2.Open(4, 115200, Com.WSS2);
		Log.i(TAG, "Open WSS2 at fd"+fd2);
		int fd1=SWSS1.Open(3, 115200, Com.WSS1);//
		Log.i(TAG, "Open WSS1 at fd"+fd1);
		
	}

	public boolean checkDevice() {
		try {
			if (!checkWSS()) {
				Thread.sleep(1000);
				if (!checkWSS()) {
					Log.i(TAG, "init error,please restart");
					return false;
				}
			}
			Log.i(TAG, "WSS check OK!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return true;
	}

	public static Handler handler;
	public int fromPort;
	public int userNums;

	public byte[] BandRoute = new byte[6];
	List<CommunicateObject> signalList = new ArrayList<CommunicateObject>();
	Map<String, CommunicateObject> signalMap = new HashMap<String, CommunicateObject>();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
		// send check result to server
		checkDevice();
		Looper.prepare();

		while (true) {
			// 根据解析的json数据控制相应的通道。是否需要记录已用通道，还是直接可以查询WSS的该通道是否可用
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {// Server 收到消息回调此处
					super.handleMessage(msg);
					// TODO Auto-generated method stub
					CommunicateObject comObject = (CommunicateObject) msg.obj;
					Log.i(TAG, "接收到Server传来的信息");
					signalMap.put(comObject.getSourceIP() + comObject.FromFlag,
							comObject);
					int response = LinkEstablish(comObject);
					Log.i(TAG, "配置后返回信息" + response);
					if (response == 1) {
						Log.i(TAG, "response" + BandRoute[0] + BandRoute[1]
								+ BandRoute[3] + BandRoute[4]);
						if (BandRoute[0] == 1) {// 第一WSS
							if (BandRoute[1] == 1 && BandRoute[4] == 1) { // 量子光经典光都从1出去
								LineUsing.usingLine[ViewConstant.WSS1_LINE1] = 11;
							} else if (BandRoute[1] == 2 && BandRoute[4] == 1) { // 量子光从2出去,经典从1出
								LineUsing.usingLine[ViewConstant.WSS1_LINE2] = 11;
								LineUsing.usingLine[ViewConstant.WSS1_LINE1] = 10;

							} else if (BandRoute[1] == 1 && BandRoute[4] == 2) { // 量子光从2出去,经典从1出
								LineUsing.usingLine[ViewConstant.WSS1_LINE1] = 11;
								LineUsing.usingLine[ViewConstant.WSS1_LINE2] = 10;
							} else if (BandRoute[1] == 2 && BandRoute[4] == 2) {
								LineUsing.usingLine[ViewConstant.WSS1_LINE2] = 11;
							}
						} else if (BandRoute[0] == 2) {// 第一WSS
							if (BandRoute[1] == 1 && BandRoute[4] == 1) { // 量子光经典光都从1出去
								LineUsing.usingLine[ViewConstant.WSS2_LINE1] = 11;
							} else if (BandRoute[1] == 2 && BandRoute[4] == 1) { // 量子光从2出去,经典从1出
								LineUsing.usingLine[ViewConstant.WSS2_LINE2] = 11;
								LineUsing.usingLine[ViewConstant.WSS2_LINE1] = 10;

							} else if (BandRoute[1] == 1 && BandRoute[4] == 2) { // 量子光从2出去,经典从1出
								LineUsing.usingLine[ViewConstant.WSS2_LINE1] = 11;
								LineUsing.usingLine[ViewConstant.WSS2_LINE2] = 10;
							} else if (BandRoute[1] == 2 && BandRoute[4] == 2) {
								LineUsing.usingLine[ViewConstant.WSS2_LINE2] = 11;
							}
						} else {
							LineUsing.usingLine[ViewConstant.WSS2_LINE2] = 0;
							LineUsing.usingLine[ViewConstant.WSS2_LINE1] = 0;
							LineUsing.usingLine[ViewConstant.WSS1_LINE2] = 0;
							LineUsing.usingLine[ViewConstant.WSS1_LINE1] = 0;
						}
					}
					Log.i(TAG, "WSS返回信息" + response);
					try {
						HttpServer.queue.put(response);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i(TAG, comObject.getClassicOptical());
				}
			};
			Looper.loop();
		}
	}

	/**
	 * 1.判断是否为断开信令
	 */
	public int LinkEstablish(CommunicateObject com) {
		boolean isOK = false;
		if (com.getIsConnect() != SignalConstant.LinkConnect) {// 断开信令
			String flag = com.getSourceIP() + com.getFromFlag();
			if (signalMap.containsKey(flag)) { // 从已用消息集合中移除
				signalMap.remove(flag);
			}
			if (com.FromFlag == 1) {
				isOK = setupWSS(com, SWSS1, Com.WSS1, 1);
				if (isOK) {
					BandRoute[0] = 0;
				}
			} else if (com.FromFlag == 2) {
				isOK = setupWSS(com, SWSS2, Com.WSS2, 1);
				if (isOK) {
					BandRoute[3] = 0;
				}
			}
			Log.i(TAG, "接收到断开信令，返回结果"+isOK);
			return 1; // 返回给server，表示已处理完毕
		} else {
			if (com.FromFlag == 1) {// 输入1端口来的信息，即需要WSS1进行链路分配
				isOK = setupWSS(com, SWSS1, Com.WSS1, 0);
				if (isOK) {
					BandRoute[0] = 1;
					BandRoute[3] = 1;
				}
			} else if (com.FromFlag == 2) {
				isOK = setupWSS(com, SWSS2, Com.WSS2, 0);
				if (isOK) {
					BandRoute[0] = 2;
					BandRoute[3] = 2;
				}
			}
			Log.i(TAG, "接收到建立链路信令，返回结果"+isOK);
		}
		if (isOK) { // 链路分配正确
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * WSS配置
	 */
	public boolean setupWSS(CommunicateObject msg, serial serial, int flag,
			int reset) {
		String[] cmd = CommandBuild(msg);
		if (reset == 0)
			Log.i(TAG, "写入的配置指令：" + cmd[reset]);
		else {
			Log.i(TAG, "写入的配置指令：" + cmd[reset]);
		}
		String response = WSSSetup.getInstance().sendAndGetResponse(serial,
				flag, cmd[reset]);
		if (response == null) {
			try {
				Thread.sleep(2000);
				response = WSSSetup.getInstance().sendAndGetResponse(serial,
						flag, cmd[reset]);
				if (response == null)
					return false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//
		}
		if (response.contains("OK")) {
			response = WSSSetup.getInstance().sendAndGetResponse(serial, flag,
					WSSCmd.COMMEND_RSW);
			if (response.contains("OK"))// 返回OK，表示正常
				return true;
		}

		return false;
	}

	/*
	 * 根据通道情况组合WSS配置指令
	 */
	public String[] CommandBuild(CommunicateObject comObj) {
		Map<String, List<Integer>> map = channelPort(comObj);

		StringBuilder sBuilder = new StringBuilder();
		StringBuilder sbRes = new StringBuilder();
		sbRes.append(WSSCmd.COMMEND_URA);
		sBuilder.append(WSSCmd.COMMEND_URA);
		List<Integer> list1 = map.get("OutPort1");
		for (int tmp : list1) {
			sBuilder.append(String.valueOf(tmp) + ",1,0.0;");// 设置1端口的配置指令
			sbRes.append(String.valueOf(tmp) + ",99,99.9;");// 设置1端口的配置指令
			BandRoute[1] = 1;// 通过WSS1 去往端口1，传输的是三个光
			BandRoute[2] = 2;
			BandRoute[4] = 1;
			BandRoute[5] = 2;
		}

		List<Integer> list2 = map.get("OutPort2");
		for (int tmp : list2) {
			sBuilder.append(String.valueOf(tmp) + ",2,0.0;");// 设置2端口的配置指令
			sbRes.append(String.valueOf(tmp) + ",99,99.9;");// 设置1端口的复位指令
			BandRoute[1] = 2;
			BandRoute[2] = 2;
			BandRoute[4] = 2;
			BandRoute[5] = 2;
		}
		List<Integer> list3 = map.get("OutPort12");
		for (int i = 0; i < list3.size(); i++) {
			if (i != 2) {// 0，1 代表是量子和同步的通道，这两个去的端口是一样的
				sBuilder.append(String.valueOf(list3.get(i)) + ",2,0.0;");// 量子和同步去端口2
				sbRes.append(String.valueOf(list3.get(i)) + ",99,99.9;");// 设置1端口的复位指令
				BandRoute[1] = 2;
				BandRoute[2] = 2;
			} else {
				sBuilder.append(String.valueOf(list3.get(i)) + ",1,0.0;");
				sBuilder.append(String.valueOf(list3.get(i)) + ",99,99.9;");// 设置1端口的复位指令
				BandRoute[4] = 1;
				BandRoute[5] = 1;
			}
		}

		String cmd = sBuilder.substring(0, sBuilder.length() - 1);
		cmd += "\n";
		String cmdRes = sbRes.substring(0, sbRes.length() - 1);
		cmdRes += "\n";
		String[] res = new String[2];
		res[0] = cmd;
		res[1] = cmdRes;
		return res;
	}

	/*
	 * 根据信令中IP设定相应的通道。
	 */
	public Map<String, List<Integer>> channelPort(CommunicateObject comObject) {
		// List<UserMsg> userMsgs=
		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		List<Integer> list_ip1 = new ArrayList<Integer>();
		List<Integer> list_ip2 = new ArrayList<Integer>();
		List<Integer> list_ip_diff = new ArrayList<Integer>();

		if (comObject.getClassicDesIP() != null
				&& comObject.getQuantumDesIP().equals(
						comObject.getClassicDesIP())) { // 量子光与经典光Ip相同
			Log.i(TAG, "量子光与经典光路径相同");
			if (ServerConstant.OutPortIP1.equals(comObject.getClassicDesIP())) { // 从端口1出去
				list_ip1.add(getChannel(Double.valueOf(comObject
						.getQuantumOptical())));
				list_ip1.add(getChannel(Double.valueOf(comObject
						.getSynOptical())));
				list_ip1.add(getChannel(Double.valueOf(comObject
						.getClassicOptical())));
			} else if (ServerConstant.OutPortIP2.equals(comObject
					.getClassicDesIP())) { // 端口2出去
				list_ip2.add(getChannel(Double.valueOf(comObject
						.getQuantumOptical())));
				list_ip2.add(getChannel(Double.valueOf(comObject
						.getSynOptical())));
				list_ip2.add(getChannel(Double.valueOf(comObject
						.getClassicOptical())));
			}
		} else {
			list_ip_diff.add(getChannel(Double.valueOf(comObject
					.getQuantumOptical())));
			list_ip_diff.add(getChannel(Double.valueOf(comObject
					.getSynOptical())));
			list_ip_diff.add(getChannel(Double.valueOf(comObject
					.getClassicOptical())));
		}
		map.put("OutPort1", list_ip1);
		map.put("OutPort2", list_ip2);
		map.put("OutPort12", list_ip_diff);
		return map;
	}

	// public void getusers()//json 里一个用户
	/**
	 * 功能描述:根据给定波长或波长范围判定是哪个通道的数据 返回值：通道的值 进行就近取整
	 **/
	public int getChannel(double wave) {
		Double temp = 1 + (WSSCmd.SPEED_CONSTANT / wave - 191.35) * 20;
		return (int) (temp + 0.5);
	}

	public boolean checkWSS() {
		System.out.println("WSS1 hashcode:"+SWSS1.hashCode());
		if (!WSSSetup.getInstance().checkWSS(SWSS1, Com.WSS1)) {
			System.out.println("WSS1 " + "init error");
//			return false;
		}
//		if (!WSSSetup.getInstance().checkWSS(SWSS2, Com.WSS2)) {
//			System.out.println("WSS2 "  + "init error");
////			return false;
//		}
		System.out.println("WSS2 hashcide:"+SWSS2.hashCode());
		return true;
	}

}
