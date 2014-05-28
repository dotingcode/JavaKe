package javake.cn.netjava.msg;
//文本聊天消息类
public class MsgChatText extends MsgHead{
	private String msgContent;//消息内容
	
	public String toString(){
		return super.toString()+" 内容:"+this.msgContent;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

}
