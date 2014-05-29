package javake.cn.netjava.server;

import java.net.ServerSocket;

import javake.cn.netjava.utils.LogTools;


/**
 * javaKe服务器实现类
 * 创建服务器对象,启动并等待连结进入
 */
public class Main extends Thread{
   private int port;//服务器端口
	/**
	 * 创建服务器对象时，必须传入端口号
	 * @param port:服务器所在端口号
	 */
	public Main(int port){
		this.port=port;
	}
	
	public void run(){//在线程中启动服务器
		setupServer();
	}
	//在指定的端口上启动服务器
	private void setupServer(){
		try{
 ServerSocket sc=new  ServerSocket(this.port);
    LogTools.INFO(this.getClass(), "服务器创建成功:"+port);
      while(true){
   java.net.Socket client=sc.accept();//等待连结进入
   String cAdd=client.getRemoteSocketAddress().toString();
   LogTools.INFO(this.getClass(), "进入连结:"+cAdd);
     ServerThread ct=new ServerThread(client);
     ct.start();//启动一个处理线程，去处理这个连结对象...
     }
     }catch(Exception ef){
	 LogTools.ERROR(this.getClass(), "服务器创建失败:"+ef);
      }
	}
	//启动主函数
	public static void main(String args[]){
		Main cs=new Main(9090);
		cs.start();
	}
	 
}
