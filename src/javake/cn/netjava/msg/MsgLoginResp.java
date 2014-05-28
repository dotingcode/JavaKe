package javake.cn.netjava.msg;
//登录请求应答消息类
public class MsgLoginResp extends MsgHead{
	private byte state;//登陆状态 0:成功 其它:失败代码
//	
	public String toString(){
		String head=super.toString();
		return head+" state:"+state;
	}
	//以下为getter/seter方法
	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

}
