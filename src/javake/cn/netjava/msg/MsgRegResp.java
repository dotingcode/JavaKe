package javake.cn.netjava.msg;
//注册应答消息类
public class MsgRegResp extends MsgHead{
    private byte state;//注册状态0:成功 其它：其它失败代码
	
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
