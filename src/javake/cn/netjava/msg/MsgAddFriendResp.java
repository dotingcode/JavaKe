package javake.cn.netjava.msg;
//添加好友应答消息类
public class MsgAddFriendResp extends MsgHead{
private int  friendJkNum;//好友的JK号
private String friendNikeName;//好友呢称
 
	
	public String toString(){
		String head=super.toString();
		return "friendJkNum:"+ friendJkNum
		+" friendNikeName:"+friendNikeName;
	}
	//以下为getter/seter方法


	public int getFriendJkNum() {
		return friendJkNum;
	}


	public void setFriendJkNum(int friendJkNum) {
		this.friendJkNum = friendJkNum;
	}


	public String getFriendNikeName() {
		return friendNikeName;
	}


	public void setFriendNikeName(String friendNikeName) {
		this.friendNikeName = friendNikeName;
	}
//	 
}
