package javake.cn.netjava.model;

 import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javake.cn.netjava.msg.IMsgConstance;
import javake.cn.netjava.msg.MsgAddFriend;
import javake.cn.netjava.msg.MsgAddFriendResp;
import javake.cn.netjava.msg.MsgChatFile;
import javake.cn.netjava.msg.MsgChatText;
import javake.cn.netjava.msg.MsgFindResp;
import javake.cn.netjava.msg.MsgHead;
import javake.cn.netjava.msg.MsgLogin;
import javake.cn.netjava.msg.MsgLoginResp;
import javake.cn.netjava.msg.MsgReg;
import javake.cn.netjava.msg.MsgRegResp;
import javake.cn.netjava.msg.MsgTeamList;
import javake.cn.netjava.utils.LogTools;

 
/**
 *JK即时通信系统:打包消息工具类
 *将消息对象，根据协议规约，打包为字节数组以备网络发送
 */
public class ToolsCreateMsg {
	 
	/**
	 * 将消息对象打包为字节数组返回
	 * @param msg :要打包的消息对象
	 * @return:返回的字节数组
	 */
	public static byte[] packMsg(MsgHead msg)throws IOException{
		//创建内存输出流
		java.io.ByteArrayOutputStream bous=new java.io.ByteArrayOutputStream();
		java.io.DataOutputStream dous=new java.io.DataOutputStream(bous);
		 writeHead(dous,msg);//先写入消息头,所有消息头结构相同
		int msgType=msg.getType();//取得消息类型标识
       if(msgType==IMsgConstance.command_login){//登陆请求
			 MsgLogin ml=(MsgLogin)msg;
			 writeString(dous,10,ml.getPwd());
		}
		else if(msgType==IMsgConstance.command_login_resp){//登陆应答
			 MsgLoginResp mlr=(MsgLoginResp)msg;
			 dous.writeByte(mlr.getState());			
		}
		else if(msgType==IMsgConstance.command_reg){//注册请求
			 MsgReg ml=(MsgReg)msg;
			 writeString(dous,10,ml.getNikeName());
			 writeString(dous,10,ml.getPwd());
		}
		else if(msgType==IMsgConstance.command_reg_resp){//注册应答
			MsgRegResp mr=(MsgRegResp)msg;
			 dous.writeByte(mr.getState());
	      }
		else if(msgType==IMsgConstance.command_chatText){//登陆应答
			 MsgChatText mt=(MsgChatText)msg;
			 dous.write(mt.getMsgContent().getBytes());
		}
		else if(msgType==IMsgConstance.command_chatFile){//如果是文件数据包
			 MsgChatFile mt=(MsgChatFile)msg;
			 writeString(dous,256,mt.getFileName());
			 dous.write(mt.getFileData());//写入文件数据
		}
		else if(msgType==IMsgConstance.command_find_resp){
			MsgFindResp mf=(MsgFindResp)msg;
			List<UserInfo> users=mf.getUsers();
			dous.writeInt(users.size());
			for(UserInfo user:users){
				writeString(dous,10,user.getNikeName());
				dous.writeInt(user.getJkNum());
			}
		}
		else if(msgType==IMsgConstance.command_teamList){//如果好友列表数据包
			MsgTeamList mdb= (MsgTeamList)msg;
			 List<TeamInfo> teams=mdb.getTeamLists();
			 int listsCount=teams.size();//分组个数
			 //写入有多少个分组
			 dous.writeInt(listsCount);
			 for(TeamInfo team:teams){
				 //写入一个分组名字
				 writeString(dous,10,team.getName());
				   List<UserInfo> users=team.getBudyList();
				 //写入组内有多少个好友对象
				 dous.writeByte(users.size());
				 for(UserInfo user:users){
					 //写入每一个好友的jk号
					 dous.writeInt(user.getJkNum());
					 //写入每一个好友的呢称
					 writeString(dous,10,user.getNikeName());
				 }
			 }
		} //添加好友:      
		 else if(msgType==IMsgConstance.command_addFriend){
			 MsgAddFriend mt=(MsgAddFriend)msg;
			 //要加入的好友的号码
			 dous.writeInt(mt.getFriendJkNum());
	        }//添加好友的应答:
		 else if(msgType==IMsgConstance.command_addFriend_Resp){
			 MsgAddFriendResp mt=(MsgAddFriendResp)msg;
			 dous.writeInt(mt.getFriendJkNum());//要加入的好友的号码
			 writeString(dous,10,mt.getFriendNikeName());
		}
		 else if(msgType==IMsgConstance.command_offLine
			||msgType==IMsgConstance.command_onLine
			||msgType==IMsgConstance.command_find){//无消息体
			 
	 }else{
    String logMsg="创建未知消息类型，无法打包:type:"+msgType;
	LogTools.ERROR(ToolsCreateMsg.class, logMsg);//记录日志
		  }
         dous.flush();
		byte[] data=bous.toByteArray();
		return data;//返回打包后的数据,以方便发送
	}
	
	/**
	 * 统一方法：向流中写入消息头数据
	 * @param dous:要写入的流对象
	 * @param m:消息头对象
	 */
	private static void writeHead(DataOutputStream dous,MsgHead m)
	throws IOException{
		dous.writeInt(m.getTotalLen());
		dous.writeByte(m.getType());
		dous.writeInt(m.getDest());
		dous.writeInt(m.getSrc());
	}
	 /**
	 * 向流中写入len长度的字符串
	 * 如果s的字节长度不足len个，补'\0'
	 * @param dous:输出流对象
	 * @param len:要写入的长度
	 * @param s:要写入到流中的字符串
	 */
	private static void writeString(DataOutputStream dous,int len,String s)
	throws IOException{
		byte[] data=s.getBytes();
		if(data.length>len){
			throw new IOException("写入长度为"+data.length+",超长!");
		}
		dous.write(data);
		 while(len>data.length){//如果短，需要补0
			 dous.writeByte('\0');//补二进制0
			 len--;
		 }
	}
}
