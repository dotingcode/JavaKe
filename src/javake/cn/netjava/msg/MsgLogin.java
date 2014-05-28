package javake.cn.netjava.msg;
//登陆请求消息类
public class MsgLogin extends MsgHead{
	private String pwd;//用户密码
	//以下为getter/setter方法
	public String toString(){
		String head=super.toString();
		return head+" pwd:"+pwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	 
	

}
