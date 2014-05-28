package javake.cn.netjava.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javake.cn.netjava.model.TeamInfo;
import javake.cn.netjava.model.UserInfo;
import javake.cn.netjava.msg.IMsgConstance;
import javake.cn.netjava.msg.MsgAddFriendResp;
import javake.cn.netjava.msg.MsgChatText;
import javake.cn.netjava.msg.MsgHead;
import javake.cn.netjava.msg.MsgTeamList;
import javake.cn.netjava.utils.LogTools;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


 
/**
 *JK即时通信系统 
 *在主界面中显示好友/分组列表的树形结构
 *并处理相关消息
 * @author 蓝杰 www.NetJava.cn
 */
public class JKUserTree extends javax.swing.JTree{
 
	private DefaultMutableTreeNode root;//树上的根节点
	private int jkNum;//用户jk号
	//取得连结器的单实例对象
	private ClientConnection conn=ClientConnection.getIns();
	
	//创建树时，传入jk号
	public JKUserTree(int jkNum){
		this.jkNum=jkNum;
		root=new DefaultMutableTreeNode("我的好友");
	   DefaultTreeModel tm=new DefaultTreeModel(root);
		this.setModel(tm);
		//给树加上Mouse事件监听器，双击弹出界面给用户发消息
		this.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){//双击事件
				showSendFrame();//弹出发送消息框
				}
			}
		});
	}
	
	/**
     *处理客户端接收到一条消息时的动作,被主界面窗体对象调用
	 *1.登陆成功后，接收到好友分组
	 *2.添加一个好友成功
	 *3.接到聊天消息 4.接到文件传送消息
	 * @param m:接收到的消息对象
	 */
	public void onMsgRecive(MsgHead m){
	   if(m.getType()==IMsgConstance.command_teamList){//好友列表
		   MsgTeamList ms=(MsgTeamList)m;
		   List<TeamInfo> teams=ms.getTeamLists();
		   for(TeamInfo team:teams){
			   List<UserInfo> users=team.getBudyList();
			   for(UserInfo user:users){
				   DefaultMutableTreeNode newNode=new DefaultMutableTreeNode(user);
					root.add(newNode);//将新建的节点加载到树上
			   }
		   }
		}//添加好友的应答
	   else if(m.getType()==IMsgConstance.command_addFriend_Resp){
		   MsgAddFriendResp mf=(MsgAddFriendResp)m;
		   UserInfo budy=new UserInfo(mf.getFriendJkNum());
		   budy.setNikeName(mf.getFriendNikeName());
		   DefaultMutableTreeNode newNode=new DefaultMutableTreeNode(budy);
			root.add(newNode);//将新建的节点加载到树上
	   }
	   else if(m.getType()==IMsgConstance.command_chatText){
		   MsgChatText mt=(MsgChatText)m;
		   javax.swing.JOptionPane.showMessageDialog(this,mt.getSrc()+"说:"+mt.getMsgContent());
	   }
	   else if(m.getType()==IMsgConstance.command_chatFile){
		   javax.swing.JOptionPane.showMessageDialog(this,"收到文件,你怎么办?");
	   }
	   else if(m.getType()==IMsgConstance.command_onLine){
		 //此处应将其在树上的图标变亮
		   javax.swing.JOptionPane.showMessageDialog(this,m.getSrc()+"上线啦!");
	   }
	   else if(m.getType()==IMsgConstance.command_offLine){
		   //此处应将其在树上的图标变灰
		   javax.swing.JOptionPane.showMessageDialog(this,m.getSrc()+"下线啦!");
	   }
	   else{   //将消息对象包装为新建的节点
		   javax.swing.JOptionPane.showMessageDialog(this,"什么消息?类型:"+m.getType());
		}
	   javax.swing.SwingUtilities.updateComponentTreeUI(this);//刷新界面
		}

 
	 /* 双击树上的好友节点时，弹出消息发送框*/
	private void showSendFrame(){
		 //得到树上选中的节点:
		javax.swing.tree.TreePath tp=this.getSelectionPath();
		if(tp==null){//未选中树节点
			return ;
		}
		Object obj=tp.getLastPathComponent();//取得选中的节点
		DefaultMutableTreeNode node=(DefaultMutableTreeNode)obj;
		Object userO=node.getUserObject();//取得节点内的对象
		if(userO instanceof UserInfo){//选中的是一个用户节点对象
		 final	UserInfo destUser=(UserInfo)userO;
	       //弹出发送消息框
		final javax.swing.JDialog jda=new javax.swing.JDialog();
		String s="你对:"+destUser.getNikeName()+"("+destUser.getJkNum()+")说:";
		 jda.setTitle(s);
		jda.setSize(200,150);
		java.awt.FlowLayout fl=new java.awt.FlowLayout();
		jda.setLayout(fl);
		javax.swing.JLabel la=new javax.swing.JLabel("请输入要发的消息(按回车发送):");
		jda.add(la);
		final javax.swing.JTextField jta=new javax.swing.JTextField(15);
		jda.add(jta);
		jda.setVisible(true);
		//在输入框中按下回车，即发送:发送事件监听器实现
		jta.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 String msg=jta.getText();
				 MsgChatText mct=new MsgChatText();
				 mct.setType(IMsgConstance.command_chatText);
				 mct.setTotalLen(4+1+4+4+msg.getBytes().length);
				 mct.setDest(destUser.getJkNum());
				 mct.setSrc(jkNum);
				 mct.setMsgContent(msg);
				 try{
				 conn.sendMsg(mct);
				 }catch(Exception ef){
					 LogTools.ERROR(this.getClass(), "发送出错:"+ef);
				 }
				 jda.dispose();
			 }
		});
		}
	}
	
}
