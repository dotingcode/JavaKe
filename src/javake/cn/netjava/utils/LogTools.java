package javake.cn.netjava.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统日志记录工具类
 * @author 蓝杰 www.NetJava.cn
 */
public class LogTools {
	private LogTools(){};//不需要创建对象,构造器private
	private static boolean isDebug=true;//是否调试标志
	//禁止输出一般日志信息
	public static void disDebug(){
		isDebug=false;
	}
	
	 /**
	 * 记录一般日志信息
	 * @param c:信息所在的类
	 * @param msg:消息的对象
	 */
	public static void INFO(Class c,Object msg){
		if(!isDebug){return ;}
     SimpleDateFormat sdf = new SimpleDateFormat("hh-FF-ss");
     String t=sdf.format(new Date());//格式化时间
	System.out.println("INFO:"+t+":"+c.getSimpleName()+":"+msg);	
	}
	
	/**
	 * 记录出错日志信息
	 * @param c:信息所在的类
	 * @param msg:消息的对象
	 */
	public static void ERROR(Class c,Object msg){
		if(!isDebug){return ;}
		SimpleDateFormat sdf = new SimpleDateFormat("hh-FF-ss");
	     String t=sdf.format(new Date());
		System.out.println("ERROR:"+t+":"+c.getSimpleName()+":"+msg);		
	}

	
//	/**计算密码字符串的MD5摘要*/
//	public static byte[] getMD5(String pwd){
//		try{
//		MessageDigest md5=java.security.MessageDigest.getInstance("MD5");
//	    byte[] data=md5.digest(pwd.getBytes());
//	    return data;
//		}catch(Exception ef){
//			ef.printStackTrace();
//	   }
//		return null;
//	}
}
