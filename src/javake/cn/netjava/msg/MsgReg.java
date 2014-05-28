package javake.cn.netjava.msg;
//注册请求消息类
public class MsgReg extends MsgHead{
	private String pwd;//用户密码
	private String nikeName;//用户呢称
// 以下为getter/seter方法
	public String toString(){
		String head=super.toString();
		return head+" pwd:"+pwd
		+" nikeName:"+nikeName;
	}
	//以下为getter/seter方法
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
//	

}
