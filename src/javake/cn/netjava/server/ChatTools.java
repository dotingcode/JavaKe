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
 * 服务器管管理客户处理线程,转发消息的类
 * 此类只需要提供方法调用,所以皆为静态方法
 * @author 蓝杰 www.NetJava.cn
 */
public class ChatTools {
	//保存处理线程的队列对象
	private static Map<UserInfo,ServerThread> stList=new HashMap();
	private ChatTools(){}//不需要创建引类对象,构造器则私有
 
	/**
	 * 当用户登陆成功后将对应的处理线程对象加入到队列中
	 * 并给其好友发送上线消息
	 * @param ct:处理线程对象
	 */
	public static void addClient(UserInfo user,ServerThread ct){
		stList.put(user,ct);
		//发送其上线的消息
		sendOnOffLineMsg(user,true);
	}
	/**
	 * 用户退出系统
	 * 1.移除处理队列中的处理线程对象
	 * 2.对其好友发送下线消息
	 * @param user:退出用户对象
	 */
	public static void removeClient(UserInfo user){
		ServerThread ct=stList.remove(user);
		ct.disConn();
		ct=null;//移除,清理这个用户的处理线程对象
		sendOnOffLineMsg(user,false);//给其好友发送其己离线的消息
	}
	/**
	 * 当这个用户的好友发送 上线/下线消息 
	 * @param user:上线/下线的用户
	 */
	public static void sendOnOffLineMsg(UserInfo user,boolean onLine){
		//给这个用户的好友发送：我己上线的消息
		List<TeamInfo> teams=user.getTeams();
		for(TeamInfo team:teams){
			List<UserInfo> users= team.getBudyList();
			for(UserInfo destUser:users){
				//这个用户对象在表中，就证明其在线
				ServerThread otherST=stList.get(destUser);
				if(null!=otherST){
					MsgHead onLineMsg=new MsgHead();
					onLineMsg.setTotalLen(4+1+4+4);
					if(onLine){
					onLineMsg.setType(IMsgConstance.command_onLine);
					}else{//否则设定为下线消息
					 onLineMsg.setType(IMsgConstance.command_offLine);
					}
					onLineMsg.setDest(destUser.getJkNum());
					onLineMsg.setSrc(user.getJkNum());
				    otherST.sendMsg2Me(onLineMsg);//发送消息
				}
			}
		}
	}
	
	/**
	 * 给队列中的某一个用户发送消息
	 * @param srcUser：发送者
	 * @param msg:消息内容
	 */
	public synchronized static void sendMsg2One(UserInfo srcUser,MsgHead msg){
		//1.查找好友
		if(msg.getType()==IMsgConstance.command_find){
			//返回所有在线的好友列表
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
			//在线用户个数
			int count=mfr.getUsers().size();
			mfr.setTotalLen(4+1+4+4+4+count*(4+10));
			//发送查找到的在线用户表
			stList.get(srcUser).sendMsg2Me(mfr);
			return;
		}
		//2.添加好友,只添加,给被添加者也要发送信息
        if(msg.getType()==IMsgConstance.command_addFriend){
        	MsgAddFriend maf=(MsgAddFriend)msg;
        	UserInfo destUser=UserDao.addFriend(msg.getSrc(),maf.getFriendJkNum());
        	MsgAddFriendResp mar=new MsgAddFriendResp();
        	mar.setTotalLen(4+1+4+4+4+10);
        	mar.setDest(msg.getSrc());
        	mar.setSrc(msg.getDest());
        	mar.setType(IMsgConstance.command_addFriend_Resp);
        	mar.setFriendJkNum(destUser.getJkNum());//要添加的用户的JK号
        	mar.setFriendNikeName(destUser.getNikeName());
        	stList.get(srcUser).sendMsg2Me(mar);//发送查找结果
        	//给被加的人也发一个添加好友的信息
        	mar.setDest(destUser.getJkNum());
        	mar.setSrc(msg.getDest());
        	mar.setFriendJkNum(srcUser.getJkNum());
        	mar.setFriendNikeName(srcUser.getNikeName());
        	//如果被加的用户未上线,这就会出问题!
        	stList.get(destUser).sendMsg2Me(mar); 
        	
        	return ;
		}
        //3.如果是聊天或传文件消息，转发即是
        if(msg.getType()==IMsgConstance.command_chatText
        ||msg.getType()==IMsgConstance.command_chatFile){
       //如果是聊天,传文件消息,则转发
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
