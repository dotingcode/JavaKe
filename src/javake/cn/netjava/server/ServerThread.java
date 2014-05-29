package javake.cn.netjava.server;

 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

import javake.cn.netjava.model.TeamInfo;
import javake.cn.netjava.model.ToolsCreateMsg;
import javake.cn.netjava.model.ToolsParseMsg;
import javake.cn.netjava.model.UserDao;
import javake.cn.netjava.model.UserInfo;
import javake.cn.netjava.msg.IMsgConstance;
import javake.cn.netjava.msg.MsgHead;
import javake.cn.netjava.msg.MsgLogin;
import javake.cn.netjava.msg.MsgLoginResp;
import javake.cn.netjava.msg.MsgReg;
import javake.cn.netjava.msg.MsgRegResp;
import javake.cn.netjava.msg.MsgTeamList;
import javake.cn.netjava.utils.LogTools;


/**
 * JK即时通信系统:
 * 处理客户机的连结对象的线程类
 * 1.登陆/注册请求,2.接收,发送消息方法封装
 */
public class ServerThread extends Thread{

	private java.net.Socket client;//线程中处理的客户对象
	private java.io.DataOutputStream dous;//输出流对象
	private java.io.DataInputStream dins;//输入流对象
	private UserInfo owerUser; //这个线程处理对像代表的用户对象
	private boolean loginOK=false;//是否己登陆成功的标志
	//创建时必须传入一个Socket对象，即服务器与客户机的连结对象
	public ServerThread(java.net.Socket client){
		this.client=client;
	}
	
	/**
	 * 连结成功后,服务器要读取第一条消息,
	 * 可能是登陆消息,也可能是注册请求
	 * @return:是否读取成功
	 */
	private boolean readFirstMsg(){
		try{
		//包装为可读写原始数据类型的输入输出流
		 this.dous=new DataOutputStream(client.getOutputStream());
	     this.dins=new DataInputStream(client.getInputStream());
	     MsgHead msg=reciveData(); //读取第一条消息:
	     if(msg.getType()==IMsgConstance.command_login){//如果是登陆请求
	        MsgLogin ml=(MsgLogin)msg;
	        return checkLogin(ml);
	     }
	     if(msg.getType()==IMsgConstance.command_reg){//如果是注册请求
	    	 MsgReg mr=(MsgReg)msg;
	    	 //保存注册,生成JK号,回复应答消息
	    	int jkNum= UserDao.regUser(mr.getPwd(), mr.getNikeName());
	    	MsgRegResp mrs=new MsgRegResp();//创建一个应答消息对象
	    	mrs.setTotalLen(4+1+4+4+1);
	    	mrs.setType(IMsgConstance.command_reg_resp);
	    	mrs.setDest(jkNum);
	    	mrs.setSrc(IMsgConstance.Server_JK_NUMBER);
	    	mrs.setState((byte)0);
	    	this.sendMsg2Me(mrs);//发送注册应答消息
	     }
		}catch(Exception ef){
			LogTools.ERROR(this.getClass(), "readFirstMsg失败:"+ef);
		}
		return false;
	}
	
	//取得这个线程对象代表的用户对象；
	public UserInfo getOwerUser(){
		return this.owerUser;
	}
	/**检查登陆是否成功 msg:接收到的登陆请求消息对象*/
	private boolean checkLogin(MsgLogin msg){
	     //到数据库模块验证是否登陆可成功
	     owerUser=UserDao.checkLogin(msg.getSrc(),msg.getPwd());
	     if(owerUser!=null){
	    	 //登陆成功1.回应答包，2.将这个处理线程对象加到队列中
	    	 MsgLoginResp mlr=new MsgLoginResp();
	    	 mlr.setTotalLen(4+1+4+4+1);
	    	 mlr.setType(IMsgConstance.command_login_resp);
	    	 mlr.setSrc(IMsgConstance.Server_JK_NUMBER);
	    	 mlr.setDest(owerUser.getJkNum());//接收者号码
	    	 mlr.setState((byte)0);
	    	 this.sendMsg2Me(mlr);//发送应答消息
	    	 //创建好友列表消息对象,并发送
	    	 MsgTeamList mt=new MsgTeamList();
	    	 int len=4+1+4+4+4;
	    	 List<TeamInfo> teams=owerUser.getTeams();
	    	 for(TeamInfo team:teams){
	    		 int uCount=team.getBudyList().size();
	    		 len+=10+1;
	    		 len+=uCount*(10+4);
	    	 }
	    	 mt.setTotalLen(len);
	    	 mt.setDest(owerUser.getJkNum());
	    	 mt.setSrc(IMsgConstance.Server_JK_NUMBER);
	    	 mt.setType(IMsgConstance.command_teamList);
	    	 mt.setTeamLists(owerUser.getTeams());
	    	 this.sendMsg2Me(mt);//发送好友分组列表
	    	 return true;
	     } 
	     this.disConn();//登陆失败,直接断开
	    return false;
	}
	//线程中执行接收消息的方法
	public void run(){
		 try{
		loginOK=readFirstMsg();
	    if(loginOK){
	    //如果登陆成功，将这个处理线程对象加入到队列中
	     ChatTools.addClient(this.owerUser ,this);
	    }
        while(loginOK){
        MsgHead msg=this.reciveData();//循环接收消息
        ChatTools.sendMsg2One(this.owerUser,msg);//分发这条消息
	     }
		 }catch(Exception ef){
	 LogTools.ERROR(this.getClass(), "服务器读消息出错:"+ef);
	 }//用户离线了,从队列中移除这个用户对应的处理线程对象
	 ChatTools.removeClient(this.owerUser);
	}
	/**
	 * 从输入流上读取数据块,解包为消息对象
	 * @return:将读到的数据块解析为消息对象
	 */
	private MsgHead reciveData()throws Exception{
	     int len=dins.readInt(); //读取消息长度
	     LogTools.INFO(this.getClass(), "服务器读消息长度:"+len);
	     byte[] data=new byte[len-4];
	     dins.readFully(data);
         MsgHead msg=ToolsParseMsg.parseMsg(data);//解析为消息对象
         LogTools.INFO(this.getClass(), "服务器读到消息对象:"+msg);
	     return msg;
	}
	
	/**
	 * 发送一条消息对象给这个对象所代表的客户端用户
	 * @param msg:要发送的消息对象
	 * @return:是否发送成功
	 */
	public boolean sendMsg2Me(MsgHead msg){
	   try{
	  byte[] data= ToolsCreateMsg.packMsg(msg);//将消息对象打包为字节组
	 this.dous.write(data);
	 this.dous.flush();
	 LogTools.INFO(this.getClass(), "服务器发出消息对象:"+msg);
	 return true;
		}catch(Exception ef){
        LogTools.ERROR(this.getClass(), "服务器发出消息出错:"+msg);
		}
    return false;
	}
	 
	/**
	 * 断开连结这个处理线程与客户机的连结,
	 * 发生异常,或处理线程退出时调用
	 */
	public void disConn(){
		try{
			loginOK=false;
			this.client.close();
		}catch(Exception ef){}
	}
}
