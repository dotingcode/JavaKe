package javake.cn.netjava.model;

 import java.io.DataInputStream;
import java.util.ArrayList;
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
 *JK即时通信系统:解包消息工具类
 *将读到的数据块，根据协议规约，解包为消息对象
 */
public class ToolsParseMsg {
	/**
	 * 将从流上读到的数据块，解包为消息对象
	 * @param data:读到的数据块
	 * @return:解包后的消息对象
	 */
	public static MsgHead parseMsg(byte[] data)throws Exception{
		int totalLen=data.length+4;//算出消息总长
		java.io.ByteArrayInputStream bins=new java.io.ByteArrayInputStream(data);//转换为内存流
		java.io.DataInputStream dins=new java.io.DataInputStream(bins);
		byte msgType= dins.readByte();//读取消息类型
		int dest=dins.readInt();//读取目标jk号
		int src=dins.readInt();//读取源jk号
		   MsgHead msgHead=new MsgHead();//将消息头数据赋值
		   msgHead.setTotalLen(totalLen);
		   msgHead.setType(msgType);
		   msgHead.setDest(dest);
		   msgHead.setSrc(src);
		if(msgType==IMsgConstance.command_login){//登陆请求
			String pwd=readString(dins,10);
			MsgLogin ml=new MsgLogin();
			copyHead(msgHead,ml);//复制消息头数据
			ml.setPwd(pwd);
			return ml;
			
		}
		else if(msgType==IMsgConstance.command_login_resp){//登陆应答
			byte state=dins.readByte();//读取状态字段，一个字节
			MsgLoginResp ml=new MsgLoginResp();
			copyHead(msgHead,ml);//复制消息头数据
		    ml.setState(state);
			return ml;
			
		}else if(msgType==IMsgConstance.command_reg){
			MsgReg mr=new MsgReg();
			copyHead(msgHead,mr);
			String nikeName=readString(dins,10);
			mr.setNikeName(nikeName);
			String pwd=readString(dins,10);
			mr.setPwd(pwd);
			return mr;
	      }
		else if(msgType==IMsgConstance.command_reg_resp){
			MsgRegResp mr=new MsgRegResp();
			copyHead(msgHead,mr);
			mr.setState(dins.readByte());
			return mr;
	      }
		else if(msgType==IMsgConstance.command_chatText){//登陆应答
			int len=totalLen-4-1-4-4;//计算聊天内容字节的长度
			String content=readString(dins,len);//读取聊天内容字符串
			MsgChatText ml=new MsgChatText();
			copyHead(msgHead,ml);
		    ml.setMsgContent(content);
			return ml;
			
		}
		else if(msgType==IMsgConstance.command_chatFile){//如果是文件数据包
			String fileName=readString(dins,256);//读取文件数据包
			int fileLen=totalLen-4-1-4-4-256;//计算文件数据字节的长度
			MsgChatFile ml=new MsgChatFile();
			copyHead(msgHead,ml);
		    ml.setFileName(fileName);
		    byte[] fileData=new byte[fileLen];
		    dins.readFully(fileData);
		    ml.setFileData(fileData);
			return ml;
		}else if(msgType==IMsgConstance.command_find_resp){
			MsgFindResp mf=new MsgFindResp();
			copyHead(msgHead,mf);
			int userCount=dins.readInt();
			for(int i=0;i<userCount;i++){
				String nakeName=readString(dins,10);
				int jkNum=dins.readInt();
				UserInfo user=new UserInfo(jkNum,nakeName);
				mf.addUserInfo(user);
			}
			return mf;
		}
		else if(msgType==IMsgConstance.command_teamList){//如果好友列表数据包
			MsgTeamList mbl=new MsgTeamList();
			copyHead(msgHead,mbl);
			int listCount=dins.readInt();//标识有几个分组的数据
			List<TeamInfo> teamLists=new ArrayList();  
			while(listCount>0){
				  listCount--;
			  String teamName=readString(dins,10);//读取一个分组名字
			  TeamInfo team=new TeamInfo(teamName);//创建一个分组对象
			  byte JKCount=dins.readByte();//组内有几个用户
			  while(JKCount>0){
			  JKCount--;
			  int  jkNum=dins.readInt();//读取一个用户的jk号
			  String budyNikeName=readString(dins,10);//读取这个用户的呢称
			  UserInfo ui=new UserInfo(jkNum,budyNikeName);
			  team.addBudy(ui);
			  }
			  teamLists.add(team);
			  }
			mbl.setTeamLists(teamLists);
			return mbl;
		}
		//添加好友
		 else if(msgType==IMsgConstance.command_addFriend){
			 MsgAddFriend mf=new MsgAddFriend();
				copyHead(msgHead,mf);
				int jkNum=dins.readInt();//要加入的好友的号码
				mf.setFriendJkNum(jkNum);
				return mf;
	        }
      //添加好友的应答
		 else if(msgType==IMsgConstance.command_addFriend_Resp){
			 MsgAddFriendResp mfr=new MsgAddFriendResp();
			 copyHead(msgHead,mfr);
			  int jkNum=dins.readInt();//要加入的好友的号码
			 String nkName=readString(dins,10);
			 mfr.setFriendJkNum(jkNum);
			 mfr.setFriendNikeName(nkName);
			 return mfr;
		}
		else if(msgType==IMsgConstance.command_offLine
			||msgType==IMsgConstance.command_onLine
			||msgType==IMsgConstance.command_find){//无消息体
				return msgHead;
			}
		else{
		 String logMsg="解包未知消息类型，无法解包:type:"+msgType;
		 LogTools.ERROR(ToolsParseMsg.class, logMsg);//记录日志
		}
		return null;
	}
 
	/**
	 * 复制消息头的数据:
	 * @param head:消息头
	 * @param dest:要复制到的目标消息对象
	 */
	private static void copyHead(MsgHead head,MsgHead dest){
		dest.setTotalLen(head.getTotalLen());
		dest.setType(head.getType());
		dest.setDest(head.getDest());
		dest.setSrc(head.getSrc());
	}
	/**
	 * 从流中读取len长度个字节，编码为字符串返回
	 * @param dins:要读取的流对象
	 * @param len:读取的字节长度
	 * @return:编码后的字符串
	 */
	private static String readString(DataInputStream dins,int len)
	throws Exception{
		byte[] data=new byte[len];
		dins.readFully(data);
		return new String(data);//使用系统默认字符集编码
	}  
}
