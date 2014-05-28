package javake.cn.netjava.msg;

import java.util.ArrayList;
import java.util.List;

import javake.cn.netjava.model.UserInfo;

//查找好友请求消息类
public class MsgFindResp extends MsgHead{
    //存放好友分组的队列
	 private List<UserInfo> users=new ArrayList();
      
	 //加一个用户对象
	 public void addUserInfo(UserInfo bu){
		 this.users.add(bu);
	 }
	 //取得所有好友列表
	 public List<UserInfo> getUsers(){
		 return this.users;
	 }
	 
	 public String toString(){
		 return super.toString()+this.users.toString();
	 }
	
}
