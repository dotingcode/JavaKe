package javake.cn.netjava.msg;
//ע��������Ϣ��
public class MsgReg extends MsgHead{
	private String pwd;//�û�����
	private String nikeName;//�û��س�
// ����Ϊgetter/seter����
	public String toString(){
		String head=super.toString();
		return head+" pwd:"+pwd
		+" nikeName:"+nikeName;
	}
	//����Ϊgetter/seter����
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
