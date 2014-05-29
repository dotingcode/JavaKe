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
 *JK��ʱͨ��ϵͳ:�����Ϣ������
 *�����������ݿ飬����Э���Լ�����Ϊ��Ϣ����
 */
public class ToolsParseMsg {
	/**
	 * �������϶��������ݿ飬���Ϊ��Ϣ����
	 * @param data:���������ݿ�
	 * @return:��������Ϣ����
	 */
	public static MsgHead parseMsg(byte[] data)throws Exception{
		int totalLen=data.length+4;//�����Ϣ�ܳ�
		java.io.ByteArrayInputStream bins=new java.io.ByteArrayInputStream(data);//ת��Ϊ�ڴ���
		java.io.DataInputStream dins=new java.io.DataInputStream(bins);
		byte msgType= dins.readByte();//��ȡ��Ϣ����
		int dest=dins.readInt();//��ȡĿ��jk��
		int src=dins.readInt();//��ȡԴjk��
		   MsgHead msgHead=new MsgHead();//����Ϣͷ���ݸ�ֵ
		   msgHead.setTotalLen(totalLen);
		   msgHead.setType(msgType);
		   msgHead.setDest(dest);
		   msgHead.setSrc(src);
		if(msgType==IMsgConstance.command_login){//��½����
			String pwd=readString(dins,10);
			MsgLogin ml=new MsgLogin();
			copyHead(msgHead,ml);//������Ϣͷ����
			ml.setPwd(pwd);
			return ml;
			
		}
		else if(msgType==IMsgConstance.command_login_resp){//��½Ӧ��
			byte state=dins.readByte();//��ȡ״̬�ֶΣ�һ���ֽ�
			MsgLoginResp ml=new MsgLoginResp();
			copyHead(msgHead,ml);//������Ϣͷ����
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
		else if(msgType==IMsgConstance.command_chatText){//��½Ӧ��
			int len=totalLen-4-1-4-4;//�������������ֽڵĳ���
			String content=readString(dins,len);//��ȡ���������ַ���
			MsgChatText ml=new MsgChatText();
			copyHead(msgHead,ml);
		    ml.setMsgContent(content);
			return ml;
			
		}
		else if(msgType==IMsgConstance.command_chatFile){//������ļ����ݰ�
			String fileName=readString(dins,256);//��ȡ�ļ����ݰ�
			int fileLen=totalLen-4-1-4-4-256;//�����ļ������ֽڵĳ���
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
		else if(msgType==IMsgConstance.command_teamList){//��������б����ݰ�
			MsgTeamList mbl=new MsgTeamList();
			copyHead(msgHead,mbl);
			int listCount=dins.readInt();//��ʶ�м������������
			List<TeamInfo> teamLists=new ArrayList();  
			while(listCount>0){
				  listCount--;
			  String teamName=readString(dins,10);//��ȡһ����������
			  TeamInfo team=new TeamInfo(teamName);//����һ���������
			  byte JKCount=dins.readByte();//�����м����û�
			  while(JKCount>0){
			  JKCount--;
			  int  jkNum=dins.readInt();//��ȡһ���û���jk��
			  String budyNikeName=readString(dins,10);//��ȡ����û����س�
			  UserInfo ui=new UserInfo(jkNum,budyNikeName);
			  team.addBudy(ui);
			  }
			  teamLists.add(team);
			  }
			mbl.setTeamLists(teamLists);
			return mbl;
		}
		//��Ӻ���
		 else if(msgType==IMsgConstance.command_addFriend){
			 MsgAddFriend mf=new MsgAddFriend();
				copyHead(msgHead,mf);
				int jkNum=dins.readInt();//Ҫ����ĺ��ѵĺ���
				mf.setFriendJkNum(jkNum);
				return mf;
	        }
      //��Ӻ��ѵ�Ӧ��
		 else if(msgType==IMsgConstance.command_addFriend_Resp){
			 MsgAddFriendResp mfr=new MsgAddFriendResp();
			 copyHead(msgHead,mfr);
			  int jkNum=dins.readInt();//Ҫ����ĺ��ѵĺ���
			 String nkName=readString(dins,10);
			 mfr.setFriendJkNum(jkNum);
			 mfr.setFriendNikeName(nkName);
			 return mfr;
		}
		else if(msgType==IMsgConstance.command_offLine
			||msgType==IMsgConstance.command_onLine
			||msgType==IMsgConstance.command_find){//����Ϣ��
				return msgHead;
			}
		else{
		 String logMsg="���δ֪��Ϣ���ͣ��޷����:type:"+msgType;
		 LogTools.ERROR(ToolsParseMsg.class, logMsg);//��¼��־
		}
		return null;
	}
 
	/**
	 * ������Ϣͷ������:
	 * @param head:��Ϣͷ
	 * @param dest:Ҫ���Ƶ���Ŀ����Ϣ����
	 */
	private static void copyHead(MsgHead head,MsgHead dest){
		dest.setTotalLen(head.getTotalLen());
		dest.setType(head.getType());
		dest.setDest(head.getDest());
		dest.setSrc(head.getSrc());
	}
	/**
	 * �����ж�ȡlen���ȸ��ֽڣ�����Ϊ�ַ�������
	 * @param dins:Ҫ��ȡ��������
	 * @param len:��ȡ���ֽڳ���
	 * @return:�������ַ���
	 */
	private static String readString(DataInputStream dins,int len)
	throws Exception{
		byte[] data=new byte[len];
		dins.readFully(data);
		return new String(data);//ʹ��ϵͳĬ���ַ�������
	}  
}
