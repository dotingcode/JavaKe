package javake.cn.netjava.msg;
//ע��Ӧ����Ϣ��
public class MsgRegResp extends MsgHead{
    private byte state;//ע��״̬0:�ɹ� ����������ʧ�ܴ���
	
	public String toString(){
		String head=super.toString();
		return head+" state:"+state;
	}
	//����Ϊgetter/seter����
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}

}
