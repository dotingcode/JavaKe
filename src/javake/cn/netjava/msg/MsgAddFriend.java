package javake.cn.netjava.msg;
//��Ӻ���������Ϣ��
public class MsgAddFriend extends MsgHead{
	private int  friendJkNum;//���ѵ�JK��
	//����Ϊgetter/seter/toString����
	public String toString(){
		String head=super.toString();
		return head+" friendJkNum:"+ friendJkNum;
	}
	//����Ϊgetter/seter����
    public int getFriendJkNum() {
		return friendJkNum;
	}


	public void setFriendJkNum(int friendJkNum) {
		this.friendJkNum = friendJkNum;
	}
	 
	 
}
