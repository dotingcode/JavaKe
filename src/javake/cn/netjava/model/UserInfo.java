package javake.cn.netjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 *JK��ʱͨ��ϵͳ �û��ඨ��
 *һ���û����󣬰������Լ��ĺ��ѷ���������
 */
public class UserInfo implements java.io.Serializable{
	
	private int jkNum;//��ʶÿһ���û���Ψһʶ���
	private String pwd; //�û�����
	private String nikeName;//�û�����
	//��������û��ķ�������б�
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
 * �����û�����һ���������
 * @param team:һ�����������
 */
 public void addTeams( TeamInfo  team) {
		this.teams.add(team);
	}
//����Ϊgetter/setter����
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
