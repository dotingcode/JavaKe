package javake.cn.netjava.client;

import javake.cn.netjava.msg.MsgHead;
 
/**
 *JK��ʱͨ��ϵͳ 
 *ͨ��ģ�����Ϣ����������ӿڶ���
 */
public interface IClientMsgListener {

	/**
	 * ������յ���һ����Ϣ
	 * @param msg:���յ�����Ϣ����
	 */
	public void fireMsg(MsgHead msg);
}
