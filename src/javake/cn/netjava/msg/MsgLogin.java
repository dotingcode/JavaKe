package javake.cn.netjava.msg;
//��½������Ϣ��
public class MsgLogin extends MsgHead{
	private String pwd;//�û�����
	//����Ϊgetter/setter����
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
