package javake.cn.netjava.msg;
//��¼����Ӧ����Ϣ��
public class MsgLoginResp extends MsgHead{
	private byte state;//��½״̬ 0:�ɹ� ����:ʧ�ܴ���
//	
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
