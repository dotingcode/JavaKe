package javake.cn.netjava.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ϵͳ��־��¼������
 * @author ���� www.NetJava.cn
 */
public class LogTools {
	private LogTools(){};//����Ҫ��������,������private
	private static boolean isDebug=true;//�Ƿ���Ա�־
	//��ֹ���һ����־��Ϣ
	public static void disDebug(){
		isDebug=false;
	}
	
	 /**
	 * ��¼һ����־��Ϣ
	 * @param c:��Ϣ���ڵ���
	 * @param msg:��Ϣ�Ķ���
	 */
	public static void INFO(Class c,Object msg){
		if(!isDebug){return ;}
     SimpleDateFormat sdf = new SimpleDateFormat("hh-FF-ss");
     String t=sdf.format(new Date());//��ʽ��ʱ��
	System.out.println("INFO:"+t+":"+c.getSimpleName()+":"+msg);	
	}
	
	/**
	 * ��¼������־��Ϣ
	 * @param c:��Ϣ���ڵ���
	 * @param msg:��Ϣ�Ķ���
	 */
	public static void ERROR(Class c,Object msg){
		if(!isDebug){return ;}
		SimpleDateFormat sdf = new SimpleDateFormat("hh-FF-ss");
	     String t=sdf.format(new Date());
		System.out.println("ERROR:"+t+":"+c.getSimpleName()+":"+msg);		
	}

	
//	/**���������ַ�����MD5ժҪ*/
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
