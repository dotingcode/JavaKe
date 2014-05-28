package javake.cn.netjava.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
 
/**
 *JK即时通信系统 客户端启动主类
 * 1.客户端启动主类，显示登陆界面
 * 2.如果登陆成功，显示主界面
 * 3.提供注册功能，如果注册成功，返回登陆界面
 * @author 蓝杰 www.NetJava.cn
 */
public class Main {
    private JFrame jf_login;//登陆界面
	//登陆界面上的jk号输入框，密码输入框
	private  JFormattedTextField jta_jkNum;
	private  javax.swing.JTextField jta_pwd;
	//取得连结器的单实例对象
	private ClientConnection conn=ClientConnection.getIns();
 
	//显示登陆界面:
	public void showLoginUI()throws Exception{
		 jf_login=new javax.swing.JFrame("javaKe请登陆:");
		java.awt.FlowLayout fl=new java.awt.FlowLayout();
		jf_login.setLayout(fl);
		jf_login.setSize(200,160);
		//jk号输入框,格式化为只能输入数字
		MaskFormatter mfName=new MaskFormatter("##########");
		jta_jkNum=new JFormattedTextField( );
		 mfName.install(jta_jkNum);
 		jta_jkNum.setColumns(10);
		jta_pwd=new javax.swing.JPasswordField(12);
		jta_pwd.setColumns(10);
		//用户名密码的标签
		JLabel la_name=new JLabel("jk 号:");
		JLabel la_pwd=new JLabel("密  码:");
		
		jf_login.add(la_name);
		jf_login.add(jta_jkNum);
		jf_login.add(la_pwd);
		jf_login.add(jta_pwd);
		
		javax.swing.JButton bu_login=new javax.swing.JButton("Login");
		bu_login.setActionCommand("login");
		javax.swing.JButton bu_Register=new javax.swing.JButton("Register");
		bu_Register.setActionCommand("reg");
		//登陆或注册的事件监听器
		ActionListener buttonAction=new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				String command=e.getActionCommand();
				if(command.equals("login")){
					loginAction(); //执行登陆方法
				}
             if(command.equals("reg")){
            	 showRegForm();//跳出注册界面
				}
			 }
		};
		bu_login.addActionListener(buttonAction);
		bu_Register.addActionListener(buttonAction);
		
		jf_login.add(bu_login);
		jf_login.add(bu_Register);
		jf_login.setLocationRelativeTo(null);//居中
		jf_login.setVisible(true);
	}
	//注册事件处理
	private void showRegForm(){
		final JFrame jf_reg=new JFrame("javaKe请注册:");
		java.awt.FlowLayout fl=new java.awt.FlowLayout();
		jf_reg.setLayout(fl);
		jf_reg.setSize(200,160);
		final JTextField jta_regNikeName=new JTextField(12);
		final JTextField jta_regPwd=new JTextField(12);
		JLabel la_regName=new JLabel("呢 称:");
		JLabel la_regPwd=new JLabel("密  码:");
		jf_reg.add(la_regName);
		jf_reg.add(jta_regNikeName);
		jf_reg.add(la_regPwd);
		jf_reg.add(jta_regPwd);
		JButton bu_reg=new JButton("Register User");
		jf_reg.add(bu_reg);
		jf_reg.setLocationRelativeTo(null);//居中
		jf_reg.setVisible(true);
           //注册按钮事件监听器对象
		ActionListener buttonAction=new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 //取得注册的呢称和密码
	    String nikeName=jta_regNikeName.getText().trim();
		String pwd=jta_regPwd.getText().trim();
		String s="服务器连结失败!";
		if(ClientConnection.getIns().conn2Server()){//如果能连结上服务器
			int jkNum=conn.regServer(nikeName, pwd);
			  s="注册失败,错识码:"+jkNum;
			if(jkNum!=-1){
				 s="注册成功,你的JK号:"+jkNum;
			}
		 }
		javax.swing.JOptionPane.showMessageDialog(jf_reg, s);
		conn.closeMe();
		jf_reg.dispose();
		}};
		bu_reg.addActionListener(buttonAction);
	}

//登陆事件处理
	private void loginAction(){
		//1.取得输入的jk号和密码
	 	String jkStr=jta_jkNum.getText().trim();
		int jkNum=Integer.parseInt(jkStr);
		String pwd=jta_pwd.getText();
		 //2.连结上服务器
		if(conn.conn2Server()){//如果能连结上服务器
			//3.登陆
			if(conn.loginServer(jkNum, pwd)){
				//4.显示聊天主界面 //登陆成功了，要关掉登陆界面
				MainUIJavaKe mainUI=new MainUIJavaKe(jkNum);
				mainUI.showMainUI();
				conn.start();//5.启动接收线程
				//6.将用户树加给连结对象,做为消息监听器
				conn.addMsgListener(mainUI);
				jf_login.dispose();//关闭登陆界面
			}else{
				conn.closeMe();
				JOptionPane.showMessageDialog(jf_login, "登陆失败,请确认帐号正确!");
			}
		}else{
			conn.closeMe();
		JOptionPane.showMessageDialog(jf_login, "连结失败,请确认服务器开启,IP和端口正确!");
		}
	}
	
	//启动主类
	public static void main(String[] args) 
	throws Exception{
		Main qu=new Main();
		qu.showLoginUI();
	}
}
