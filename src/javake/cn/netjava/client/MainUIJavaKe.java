package javake.cn.netjava.client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javake.cn.netjava.model.UserInfo;
import javake.cn.netjava.msg.IMsgConstance;
import javake.cn.netjava.msg.MsgAddFriend;
import javake.cn.netjava.msg.MsgFindResp;
import javake.cn.netjava.msg.MsgHead;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
*JK即时通信系统 客户端主界面
* 1.主界面对象是通信连结器的一个消息监听器它实现了
* IClientMsgListener接口,当有消息到达时，主界面对象负责处理消息
* 2.主界面通过树形组件，显示用户的好友表
* 3.通过菜单，提供查找好友的功能
* @author 蓝杰 www.NetJava.cn
*/
public class MainUIJavaKe extends JFrame

implements IClientMsgListener{
	
	private int jkNum;//用户的jk号
	private JKUserTree userTree;//好友列表树
	//取得连结器的单实例对象
	private ClientConnection conn=ClientConnection.getIns();
	
	//创建主界面对象时，传入对应的jk号，
	//并创建树对象，加到界面上
	public MainUIJavaKe(int jkNum){
		this.jkNum=jkNum;
		userTree=new JKUserTree(jkNum);
	}
	
	//显示主界面
	public void showMainUI(){
		this.setTitle("JavaKe:"+jkNum);
		java.awt.FlowLayout fl=new java.awt.FlowLayout();
		this.setLayout(fl);
		this.setSize(200,700);
		JMenuBar mb=getMB();//添加界面上的菜单条
		this.setJMenuBar(mb);
		this.add(userTree);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
	}
	
	//实现通讯消息监听器接口中的方法，
	//处理通讯层来的消息
	public void fireMsg(MsgHead m){
		//如果是查找返回的消息:弹出查找结果框，
		if(m.getType()==IMsgConstance.command_find_resp){
			MsgFindResp resp=(MsgFindResp)m;
			 List<UserInfo> users=resp.getUsers();//找到的用户结果
			 showFindResult(users);//弹出查找结果框，
		}
		else if(m.getType()==IMsgConstance.command_chatText
    		   ||m.getType()==IMsgConstance.command_chatFile
    		   ||m.getType()==IMsgConstance.command_addFriend_Resp
    		   ||m.getType()==IMsgConstance.command_teamList
    		   ||m.getType()==IMsgConstance.command_offLine
    		   ||m.getType()==IMsgConstance.command_onLine){
    	   //如果是聊天，文件，加好友应答，好友列表消息，由用户树去处理
			this.userTree.onMsgRecive(m);
		}else{
			JOptionPane.showMessageDialog(this,"什么消息?"+m.getType());
		}
       
	}
	
	/**取得界面的放有菜单的菜单条对象*/
	private javax.swing.JMenuBar getMB(){
	 JMenuBar mb=new  JMenuBar();//创建一个菜单条对象
	 JMenu me_file=new  JMenu("文件");//创建菜单对象
	 JMenuItem mi_find=new  JMenuItem("查找好友");//其下的菜单项:
	 mi_find.setActionCommand("find");
	 JMenuItem mi_exit=new  JMenuItem("退出");
	 mi_exit.setActionCommand("exit"); 
	 //将菜单项加到菜单上面
	 me_file.add(mi_find);
	 me_file.add(mi_exit);
		 //菜单项目的事件监听器
    ActionListener al=new  ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 //当点了菜单时，这里就会调用
				 String command=e.getActionCommand();
				 if(command.equals("exit")){
					 System.exit(0);//退出
				 }
				 if(command.equals("find")){
					 findAction();//查找在线用户
				 }
			 }
		 };
		 mi_find.addActionListener(al);
		 mi_exit.addActionListener(al);
		 mb.add(me_file);
		return mb;//返回菜单条对象
	}
	
	/**发送查找在线用户的消息*/
	private void findAction(){
		try{
		 MsgHead findMsg=new MsgHead();
		 findMsg.setTotalLen(4+1+4+4);
		 findMsg.setType(IMsgConstance.command_find);
		 findMsg.setSrc(jkNum);
		 findMsg.setDest(IMsgConstance.Server_JK_NUMBER);
		 conn.sendMsg(findMsg);
		}catch(Exception ef){
			ef.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(this,"查找失败!");
		}
	}
	
	/**
	 * 当服务器返回查找结果时，将返回的用户对象列表，
	 * 通过Jtable显示在弹出对话框中
	 * @param users:查找返回的用户对象表
	 */
	private void showFindResult(final List<UserInfo> users){
		final javax.swing.JDialog jda=new javax.swing.JDialog();
		jda.setTitle("好友查找结果:");
		jda.setSize(300,400);
		jda.setLayout(new  FlowLayout());
		final JTable table=new JTable();
		UserInfoTableMode model=new UserInfoTableMode(users);
		table.setModel(model);
		//双击某个用户,则表示添加这个用户
		table.addMouseListener(new java.awt.event.MouseAdapter(){
			 public void mouseClicked(MouseEvent e) {
				 if(e.getClickCount()==2){//如果是双击
					int index= table.getSelectedRow();//得到选中的行
					if(index!=-1){//如果选中了表中的一行，就是要加的好友
						UserInfo destU=users.get(index);
						//添加好友的信息发送
						MsgAddFriend ma=new MsgAddFriend();
						ma.setTotalLen(4+1+4+4+4);
						ma.setDest(IMsgConstance.Server_JK_NUMBER);
						ma.setSrc(jkNum);
						ma.setType(IMsgConstance.command_addFriend);
						ma.setFriendJkNum(destU.getJkNum());
						try{//发送加好友的信息
						 conn.sendMsg(ma);
						}catch(Exception ef){
							ef.printStackTrace();
						}
					}
				 }
			 }
		});
		jda.add(table);
		jda.setVisible(true);
	}
	

}
