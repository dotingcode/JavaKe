package javake.cn.netjava.msg;
/**
 *JK��ʱͨ��ϵͳ ϵͳ���õ��ĳ�������
 */
public interface IMsgConstance {
  String serverIP="localhost";//������IP
  int serverPort=9090; //�������˿�
 int Server_JK_NUMBER=10000;//��������JK��
 //ϵͳ�õ�����Ϣ���Ͷ���
byte command_reg=0x01;//	      ע��������Ϣ		
byte command_reg_resp=0x12;//	 ע��Ӧ����Ϣ	 

byte command_login=0x02;//	     ��½������Ϣ	 	
byte command_login_resp=0x22;//	 ��½Ӧ����Ϣ	 	
byte command_teamList=0x03;//	 �������������ߺ����б�	 

byte command_onLine=0x04;//	 ���������ͺ���������Ϣ*����Ϣ�� 	
byte command_offLine=0x05;//  ���������ͺ���������Ϣ*����Ϣ�� 	

byte command_chatText=0x06;//	������Ϣ����	 	
byte command_chatFile=0x07;//	�ļ����ͷ���	 

byte command_find=0x08;//	�����û����� *����Ϣ��
byte command_find_resp=0x18;//	���Һ�������Ӧ��

byte command_addFriend=0x09;//	��Ӻ������� 
byte command_addFriend_Resp=0x19;//	��Ӻ���Ӧ��
//��13����Ϣ	
}
