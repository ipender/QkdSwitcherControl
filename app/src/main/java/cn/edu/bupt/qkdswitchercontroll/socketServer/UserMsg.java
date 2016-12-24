package cn.edu.bupt.qkdswitchercontroll.socketServer;

public class UserMsg {
	public int FromFlag;
	public int getFromFlag() {
		return FromFlag;
	}
	public void setFromFlag(int fromFlag) {
		FromFlag = fromFlag;
	}
	
	
	public String getSourceIP() {
		return SourceIP;
	}
	public void setSourceIP(String sourceIP) {
		SourceIP = sourceIP;
	}
	public String getQuantumDesIP() {
		return QuantumDesIP;
	}
	public void setQuantumDesIP(String quantumDesIP) {
		QuantumDesIP = quantumDesIP;
	}
	public String getClassicDesIP() {
		return ClassicDesIP;
	}
	public void setClassicDesIP(String classicDesIP) {
		ClassicDesIP = classicDesIP;
	}
	public String getSynOptical() {
		return SynOptical;
	}
	public void setSynOptical(String synOptical) {
		SynOptical = synOptical;
	}
	public String getQuantumOptical() {
		return QuantumOptical;
	}
	public void setQuantumOptical(String quantumOptical) {
		QuantumOptical = quantumOptical;
	}
	public String getClassicOptical() {
		return ClassicOptical;
	}
	public void setClassicOptical(String classicOptical) {
		ClassicOptical = classicOptical;
	}
	public String SourceIP;
	public String QuantumDesIP;
	public String ClassicDesIP;
	public String SynOptical;
	public String QuantumOptical;
	public String ClassicOptical;
	public int getIsConnect() {
		return isConnect;
	}
	public void setIsConnect(int isConnect) {
		this.isConnect = isConnect;
	}
	public int isConnect;

}
