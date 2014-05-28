package javake.cn.netjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 *JK即时通信系统 好友分组类的定义
 * @author 蓝杰 www.NetJava.cn
 */
public class TeamInfo implements java.io.Serializable{
	
	 private int id;  //组识别号
	 private String name; //组名字
	 private UserInfo owerUser;//此本组所属JK用户对象
	//在此分组内的好友对象表
	 private List<UserInfo> budyList=new ArrayList();
  
	 /**
	  * 创建一个分组对象
	  * @param id:此组的唯一ID
	  * @param name:组名字
	  * @param owerUser:组所属的用户对象
	  */
	 public TeamInfo(int id,String name,UserInfo owerUser){
		 this.id=id;
		 this.name=name;
		 this.owerUser=owerUser;
	 }
	 
	//为此分组加入一个好友对象
		public void addBudy(UserInfo  buddy ) {
			this.budyList.add(buddy);
		}
//以下为getter/setter方法
	 
	 public TeamInfo(String name){
		 
		 this.name=name;
		 
	 }
	 
	 public String toString(){
		 return "id:"+id+" name:"+name;
	 }
	 
	 public List<UserInfo> getBudyList() {
			return budyList;
		}
	 
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	 

	public UserInfo getOwerUser() {
		return owerUser;
	}

	 

	
	

}
