package javake.cn.netjava.server;

import java.net.ServerSocket;

import javake.cn.netjava.utils.LogTools;


/**
 * javaKe������ʵ����
 * ��������������,�������ȴ��������
 */
public class Main extends Thread{
   private int port;//�������˿�
	/**
	 * ��������������ʱ�����봫��˿ں�
	 * @param port:���������ڶ˿ں�
	 */
	public Main(int port){
		this.port=port;
	}
	
	public void run(){//���߳�������������
		setupServer();
	}
	//��ָ���Ķ˿�������������
	private void setupServer(){
		try{
 ServerSocket sc=new  ServerSocket(this.port);
    LogTools.INFO(this.getClass(), "�����������ɹ�:"+port);
      while(true){
   java.net.Socket client=sc.accept();//�ȴ��������
   String cAdd=client.getRemoteSocketAddress().toString();
   LogTools.INFO(this.getClass(), "��������:"+cAdd);
     ServerThread ct=new ServerThread(client);
     ct.start();//����һ�������̣߳�ȥ��������������...
     }
     }catch(Exception ef){
	 LogTools.ERROR(this.getClass(), "����������ʧ��:"+ef);
      }
	}
	//����������
	public static void main(String args[]){
		Main cs=new Main(9090);
		cs.start();
	}
	 
}
