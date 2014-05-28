package javake.cn.netjava.msg;

import java.util.ArrayList;
import java.util.List;

import javake.cn.netjava.model.TeamInfo;

//好友分组消息类
public class MsgTeamList extends MsgHead{
    //存放好友分组的队列
	 private List<TeamInfo> teamLists=new ArrayList();
	 //以下为getter/setter方法
      //设置这个消息对象的好友列表
	 public void setTeamLists(List<TeamInfo> teamLists){
		 this.teamLists=teamLists;
	 }
	 //取得所有好友列表
	 public List<TeamInfo> getTeamLists(){
		 return this.teamLists;
	 }
	 
	 public String toString(){
		 return super.toString()+this.teamLists.toString();
	 }
//	
}
