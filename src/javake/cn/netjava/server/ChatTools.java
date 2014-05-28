package javake.cn.netjava.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javake.cn.netjava.model.TeamInfo;
import javake.cn.netjava.model.UserDao;
import javake.cn.netjava.model.UserInfo;
import javake.cn.netjava.msg.IMsgConstance;
import javake.cn.netjava.msg.MsgAddFriend;
import javake.cn.netjava.msg.MsgAddFriendResp;
import javake.cn.netjava.msg.MsgFindResp;
import javake.cn.netjava.msg.MsgHead;



/**
 * �������ܹ���ͻ������߳�,ת����Ϣ����
 * ����ֻ��Ҫ�ṩ��������,���Խ�Ϊ��̬����
 * @author ���� www.NetJava.cn
 */
public class ChatTools {
	//���洦���̵߳Ķ��ж���
	private static Map<UserInfo,ServerThread> stList=new HashMap();
	private ChatTools(){}//����Ҫ�����������,��������˽��
 
	/**
	 * ���û���½�ɹ��󽫶�Ӧ�Ĵ����̶߳�����뵽������
	 * ��������ѷ���������Ϣ
	 * @param ct:�����̶߳���
	 */
	public static void addClient(UserInfo user,ServerThread ct){
		stList.put(user,ct);
		//���������ߵ���Ϣ
		sendOnOffLineMsg(user,true);
	}
	/**
	 * �û��˳�ϵͳ
	 * 1.�Ƴ���������еĴ����̶߳���
	 * 2.������ѷ���������Ϣ
	 * @param user:�˳��û�����
	 */
	public static void removeClient(UserInfo user){
		ServerThread ct=stList.remove(user);
		ct.disConn();
		ct=null;//�Ƴ�,��������û��Ĵ����̶߳���
		sendOnOffLineMsg(user,false);//������ѷ����伺���ߵ���Ϣ
	}
	/**
	 * ������û��ĺ��ѷ��� ����/������Ϣ 
	 * @param user:����/���ߵ��û�
	 */
	public static void sendOnOffLineMsg(UserInfo user,boolean onLine){
		//������û��ĺ��ѷ��ͣ��Ҽ����ߵ���Ϣ
		List<TeamInfo> teams=user.getTeams();
		for(TeamInfo team:teams){
			List<UserInfo> users= team.getBudyList();
			for(UserInfo destUser:users){
				//����û������ڱ��У���֤��������
				ServerThread otherST=stList.get(destUser);
				if(null!=otherST){
					MsgHead onLineMsg=new MsgHead();
					onLineMsg.setTotalLen(4+1+4+4);
					if(onLine){
					onLineMsg.setType(IMsgConstance.command_onLine);
					}else{//�����趨Ϊ������Ϣ
					 onLineMsg.setType(IMsgConstance.command_offLine);
					}
					onLineMsg.setDest(destUser.getJkNum());
					onLineMsg.setSrc(user.getJkNum());
				    otherST.sendMsg2Me(onLineMsg);//������Ϣ
				}
			}
		}
	}
	
	/**
	 * �������е�ĳһ���û�������Ϣ
	 * @param srcUser��������
	 * @param msg:��Ϣ����
	 */
	public synchronized static void sendMsg2One(UserInfo srcUser,MsgHead msg){
		//1.���Һ���
		if(msg.getType()==IMsgConstance.command_find){
			//�����������ߵĺ����б�
			Set<UserInfo> users=stList.keySet();
			Iterator<UserInfo> its=users.iterator();
			MsgFindResp mfr=new MsgFindResp();
			mfr.setType(IMsgConstance.command_find_resp);
			mfr.setSrc(msg.getDest());
			
			mfr.setDest(msg.getSrc());
			while(its.hasNext()){
				UserInfo user=its.next();
			   if(user.getJkNum()!=msg.getSrc()){
				mfr.addUserInfo(user);
				}
			}
			//�����û�����
			int count=mfr.getUsers().size();
			mfr.setTotalLen(4+1+4+4+4+count*(4+10));
			//���Ͳ��ҵ��������û���
			stList.get(srcUser).sendMsg2Me(mfr);
			return;
		}
		//2.��Ӻ���,ֻ���,���������ҲҪ������Ϣ
        if(msg.getType()==IMsgConstance.command_addFriend){
        	MsgAddFriend maf=(MsgAddFriend)msg;
        	UserInfo destUser=UserDao.addFriend(msg.getSrc(),maf.getFriendJkNum());
        	MsgAddFriendResp mar=new MsgAddFriendResp();
        	mar.setTotalLen(4+1+4+4+4+10);
        	mar.setDest(msg.getSrc());
        	mar.setSrc(msg.getDest());
        	mar.setType(IMsgConstance.command_addFriend_Resp);
        	mar.setFriendJkNum(destUser.getJkNum());//Ҫ��ӵ��û���JK��
        	mar.setFriendNikeName(destUser.getNikeName());
        	stList.get(srcUser).sendMsg2Me(mar);//���Ͳ��ҽ��
        	//�����ӵ���Ҳ��һ����Ӻ��ѵ���Ϣ
        	mar.setDest(destUser.getJkNum());
        	mar.setSrc(msg.getDest());
        	mar.setFriendJkNum(srcUser.getJkNum());
        	mar.setFriendNikeName(srcUser.getNikeName());
        	//������ӵ��û�δ����,��ͻ������!
        	stList.get(destUser).sendMsg2Me(mar); 
        	
        	return ;
		}
        //3.�����������ļ���Ϣ��ת������
        if(msg.getType()==IMsgConstance.command_chatText
        ||msg.getType()==IMsgConstance.command_chatFile){
       //���������,���ļ���Ϣ,��ת��
		Set<UserInfo> users=stList.keySet();
		Iterator<UserInfo> its=users.iterator();
		while(its.hasNext()){
			UserInfo user=its.next();
			if(user.getJkNum()==msg.getDest()){
			  stList.get(user).sendMsg2Me(msg);
			}
		}
     }
	}
}
