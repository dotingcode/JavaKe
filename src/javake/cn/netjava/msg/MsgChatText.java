package javake.cn.netjava.msg;
//�ı�������Ϣ��
public class MsgChatText extends MsgHead{
	private String msgContent;//��Ϣ����
	
	public String toString(){
		return super.toString()+" ����:"+this.msgContent;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

}
