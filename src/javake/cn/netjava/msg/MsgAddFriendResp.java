package javake.cn.netjava.msg;
//��Ӻ���Ӧ����Ϣ��
public class MsgAddFriendResp extends MsgHead{
private int  friendJkNum;//���ѵ�JK��
private String friendNikeName;//�����س�
 
	
	public String toString(){
		String head=super.toString();
		return "friendJkNum:"+ friendJkNum
		+" friendNikeName:"+friendNikeName;
	}
	//����Ϊgetter/seter����


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
