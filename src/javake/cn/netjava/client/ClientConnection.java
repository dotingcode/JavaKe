package javake.cn.netjava.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javake.cn.netjava.model.ToolsCreateMsg;
import javake.cn.netjava.model.ToolsParseMsg;
import javake.cn.netjava.msg.IMsgConstance;
import javake.cn.netjava.msg.MsgHead;
import javake.cn.netjava.msg.MsgLogin;
import javake.cn.netjava.msg.MsgLoginResp;
import javake.cn.netjava.msg.MsgReg;
import javake.cn.netjava.msg.MsgRegResp;
import javake.cn.netjava.utils.LogTools;

 
/**
 *JK即时通信系统 
 *客户端的通信模块,提供：
 *1.登陆，注册接口调用；
 *2.在独立线程中接收服务器消息
 *3.将接收到的消息分发给监听器对象
 * @author 蓝杰 www.NetJava.cn
 */
public class ClientConnection extends Thread{
	
    private static ClientConnection ins;//本类单实例对象
    private Socket client;              //与服务器的连结对象
	private java.io.DataOutputStream dous;//输出流对象
	private java.io.DataInputStream dins;//输入流对象
	
	private List<IClientMsgListener> listeners=new ArrayList();
  	
	 /**不需要创建对象,所以构造器私有*/
	private ClientConnection(){}
	
	//单实例对象访问方法
	public static ClientConnection getIns(){
		if(null==ins){
			ins=new ClientConnection();
		}
		return ins;
	}
	
	/**连结上服务器,是否连结成功*/
 public boolean  conn2Server(){
		try{
	 //1.创建一个到服务器端的Socket对象
	    client=new  Socket(IMsgConstance.serverIP,IMsgConstance.serverPort);
		//2.得到输入输出流对象
		 InputStream ins=client.getInputStream();
		//3.包装为可读写原始数据类型的输入输出流
 this.dous=new DataOutputStream(client.getOutputStream());
 this.dins=new DataInputStream(client.getInputStream());
	     return true;
		}catch(Exception ef){
			ef.printStackTrace();
		}
		return false;
	}
 
 /**
	 * 1.登陆服务器
	 * @param nikeName:用户呢称
	 * @param pwd:密码
	 * @return: 注册结果 -1:失败 其它:注册到的jk号
	 */
 public int regServer(String nikeName,String pwd){
	 try{
		 MsgReg mrg=new MsgReg();
		 mrg.setTotalLen(4+1+4+4+10+10);
		 mrg.setType(IMsgConstance.command_reg);
		 mrg.setDest(IMsgConstance.Server_JK_NUMBER);
		 mrg.setSrc(0);
		 mrg.setNikeName(nikeName);
		 mrg.setPwd(pwd);
		  this.sendMsg(mrg);
	//发送了登陆请求之后,必须读到一条应答的消息
	  MsgHead loginResp=readFromServer();
	  MsgRegResp mr=(MsgRegResp)loginResp;
	  if(mr.getState()==0){//注册成功!
	  return mr.getDest();
	  }
	 }catch(Exception ef){ef.printStackTrace();}
	 return -1;
 }
	/**
	 *  登陆服务器
	 * @param jkNum:用户JkNum
	 * @param pwd:密码
	 * @return: 是否登陆成功
	 */
 public boolean loginServer(int jkNum,String pwd){
	  try{
		  MsgLogin ml=new MsgLogin();
		  ml.setTotalLen(4+1+4+4+10);
		  ml.setType(IMsgConstance.command_login);
		  ml.setDest(IMsgConstance.Server_JK_NUMBER);
		  ml.setSrc(jkNum);
		  ml.setPwd(pwd);
		  this.sendMsg(ml);
		  //发送了登陆请求之后,必须读到一条应答的消息
		  MsgHead loginResp=readFromServer();
		  MsgLoginResp mlr=(MsgLoginResp)loginResp;
		  return mlr.getState()==0;
		}catch(Exception ef){
			ef.printStackTrace();
		}
		return false;
	}
	
 //线程中中读取服务器发来的消息，并分发给监听器
 public void run(){
	 while(true){
		 try{
			 //接收一条消息
		 MsgHead m=readFromServer();
		 //将消息分发给监听器去处理
		 for(IClientMsgListener lis:listeners){
			 lis.fireMsg(m);
		 }
		 }catch(Exception ef){
			 ef.printStackTrace();
			 break; //如果读取出错,则退出
		 }
	 }
	 LogTools.INFO(this.getClass(), "客户端接收线程己退出!");
	 
 }
 
 /**
  * 从输入流上读取一条服务器端发来的消息
  * 这个方法会阻塞，必须在独立线程中
  * @return:读取到的消息对象
  */
	public MsgHead readFromServer()throws Exception{
		 int totalLen=dins.readInt();
         LogTools.INFO(this.getClass(), "客户端读到消息总长为:"+totalLen);
		 byte[] data=new byte[totalLen-4];
		 dins.readFully(data); //读取数据块
		 MsgHead msg=ToolsParseMsg.parseMsg(data);//解包为消息对象
		 LogTools.INFO(this.getClass(), "客户端收到消息:"+msg);
		 return msg;
	}
	
	/**发送一条消息到服务器的方法*/
  public void sendMsg(MsgHead msg)throws Exception{
	  LogTools.INFO(this.getClass(), "客户端发出消息:"+msg);
	  byte[] data=ToolsCreateMsg.packMsg(msg);//打包对象为数据块
	 this.dous.write(data);//发送
	 this.dous.flush();
  }
  
  /**
   * 为连结对象加入一个消息处理监听器对象
   * @param l:消息处理监听器对象
   */
  public void addMsgListener(IClientMsgListener l){
	  this.listeners.add(l);
  }
  
  //关闭与一个客户机的连结
  public void closeMe(){
	  try{
		  this.client.close();
	  }catch(Exception ef){}
  }
}
