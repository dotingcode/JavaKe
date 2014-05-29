package javake.cn.netjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 *JK即时通信系统 用户类定义
 *一个用户对象，包含有自己的好友分组对象队列
 */
public class UserInfo implements java.io.Serializable{
	
	private int jkNum;//标识每一个用户的唯一识别号
	private String pwd; //用户密码
	private String nikeName;//用户别名
	//属于这个用户的分组对象列表
	private List<TeamInfo> teams=new ArrayList();
	
	public UserInfo(int jkNum){
		this.jkNum=jkNum;
	 }
	
	public UserInfo(int jkNum,String nikeName){
		this.jkNum=jkNum;
		this.nikeName=nikeName;
	}
	
    public String toString(){
    	return nikeName.trim()+"("+this.jkNum+")";
    }
    
/**
 * 给此用户加入一个分组对象
 * @param team:一个好友组对象
 */
 public void addTeams( TeamInfo  team) {
		this.teams.add(team);
	}
//以下为getter/setter方法
 public List<TeamInfo> getTeams() {
		return teams;
	}
 
	public int getJkNum() {
		return jkNum;
	}
public void setJkNum(int jkNum) {
	this.jkNum = jkNum;
}
public String getNikeName() {
	return nikeName.trim();
}

public void setNikeName(String nikeName) {
	this.nikeName = nikeName;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
}
