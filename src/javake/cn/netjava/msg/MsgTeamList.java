package javake.cn.netjava.msg;

import java.util.ArrayList;
import java.util.List;

import javake.cn.netjava.model.TeamInfo;

//���ѷ�����Ϣ��
public class MsgTeamList extends MsgHead{
    //��ź��ѷ���Ķ���
	 private List<TeamInfo> teamLists=new ArrayList();
	 //����Ϊgetter/setter����
      //���������Ϣ����ĺ����б�
	 public void setTeamLists(List<TeamInfo> teamLists){
		 this.teamLists=teamLists;
	 }
	 //ȡ�����к����б�
	 public List<TeamInfo> getTeamLists(){
		 return this.teamLists;
	 }
	 
	 public String toString(){
		 return super.toString()+this.teamLists.toString();
	 }
//	
}
