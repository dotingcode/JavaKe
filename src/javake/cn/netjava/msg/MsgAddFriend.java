package javake.cn.netjava.msg;
//添加好友请求消息类
public class MsgAddFriend extends MsgHead{
	private int  friendJkNum;//好友的JK号
	//以下为getter/seter/toString方法
	public String toString(){
		String head=super.toString();
		return head+" friendJkNum:"+ friendJkNum;
	}
	//以下为getter/seter方法
    public int getFriendJkNum() {
		return friendJkNum;
	}


	public void setFriendJkNum(int friendJkNum) {
		this.friendJkNum = friendJkNum;
	}
	 
	 
}
