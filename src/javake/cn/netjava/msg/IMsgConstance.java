package javake.cn.netjava.msg;
/**
 *JK即时通信系统 系统所用到的常量定义
 */
public interface IMsgConstance {
  String serverIP="localhost";//服务器IP
  int serverPort=9090; //服务器端口
 int Server_JK_NUMBER=10000;//服务器的JK号
 //系统用到的消息类型定义
byte command_reg=0x01;//	      注册请求消息		
byte command_reg_resp=0x12;//	 注册应答消息	 

byte command_login=0x02;//	     登陆请求消息	 	
byte command_login_resp=0x22;//	 登陆应答消息	 	
byte command_teamList=0x03;//	 服务器发送在线好友列表	 

byte command_onLine=0x04;//	 服务器发送好友上线消息*无消息体 	
byte command_offLine=0x05;//  服务器发送好友下线消息*无消息体 	

byte command_chatText=0x06;//	聊天消息发送	 	
byte command_chatFile=0x07;//	文件传送发送	 

byte command_find=0x08;//	查找用户请求 *无消息体
byte command_find_resp=0x18;//	查找好友请求应答

byte command_addFriend=0x09;//	添加好友请求 
byte command_addFriend_Resp=0x19;//	添加好友应答
//共13种消息	
}
