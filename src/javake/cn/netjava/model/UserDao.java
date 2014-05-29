package javake.cn.netjava.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javake.cn.netjava.utils.LogTools;

 
/**
 *JK��ʱͨ��ϵͳ ���ݴ洢ģ��ʵ��
 */
public class UserDao {
     
	/** ����û��Ƿ��½�ɹ�
	 * @param jkNum:�û�jkNum��
	 * @param pwd:�û�����
	 * @return:�ɹ����ش��û�����ʧ�ܷ���null; */
	public  static UserInfo checkLogin(int jkNum,String pwd){
		UserInfo user=userDB.get(jkNum);
		if(null!=user&&user.getPwd().equals(pwd)){
			LogTools.INFO(UserDao.class, "��½�ɹ� jkNum:"+jkNum);
			return user;
		}
		 LogTools.ERROR(UserDao.class, "��½ʧ�� jkNum:"+jkNum);
		return null;
	}
	/** ע��һ���û�����
	 * @param pwd:�û�����
	 * @param nikeName:�û��س�
	 * @return:ע��ɹ����û�jkNum;
	 */
	public static int regUser(String pwd,String nikeName){
		//��������һ��,���������û�ע���jk��
		if(userDB.size()>0){
		 //ȡ��ע��jk�ŵ����ֵ
		 maxJKNum=java.util.Collections.max(userDB.keySet());
		}
		 maxJKNum++;
		UserInfo user=new UserInfo(maxJKNum);
		user.setPwd(pwd);
		user.setNikeName(nikeName);
		//��������û���Ĭ�Ϸ������
		TeamInfo team=new TeamInfo(0,"�ҵĺ���",user);
		user.addTeams(team);//�������;
		userDB.put(maxJKNum, user);
		saveDB();//�洢���ļ�
		return maxJKNum;
	}
	 
	
	/*** ���������û�
	 * @return:ϵͳ��ע����û���*/
	public static List<UserInfo> findUser(){
		List lis=new ArrayList();
		lis.addAll(userDB.values());
		return lis;
	}
	
	/** ��ĳһ���û���Ϊ����,�����Ϊ����
	 * @param srcJKNum:������jk��
	 * @param destJKNum:��������jk�ţ�
	 * @return:���ӵĺ��Ѷ���
	 */
	public static UserInfo  addFriend(int srcJKNum,int destJKNum){
		//Ĭ���û�ֻ��һ������
		UserInfo user1=userDB.get(srcJKNum);
		UserInfo user2=userDB.get(destJKNum);
		//�����ǻ�Ϊ����
		user1.getTeams().get(0).addBudy(user2);
		user2.getTeams().get(0).addBudy(user1);
		saveDB();//�洢���ļ�
		return user2;
	}
	 
	
	/** �����ڴ��е��û����ݱ��ļ���*/
	private static void saveDB(){
		try{
			java.io.OutputStream ous=new java.io.FileOutputStream(dbFileName);
			java.io.ObjectOutputStream oos=new ObjectOutputStream(ous);
			oos.writeObject(userDB);
			oos.flush();
			ous.close(); 
			 LogTools.INFO(UserDao.class, "ˢ�������ļ��ɹ���");
		}catch(Exception ef){
			 LogTools.ERROR(UserDao.class, "ˢ�������ļ�ʧ�ܣ�"+ef);
		}
	}
    
	private static int  maxJKNum=1000;//����JK�ŵĻ���
	//�������ݵ��ļ�����
	private static final String dbFileName="netjavaJK.dat";
	/**���ڴ��б����û���Ϣ�ı�:keyΪjk�ţ�valueΪ�û�����*/
	private static  Map<Integer,UserInfo> userDB=new HashMap();
	
	//��̬�飬���Գ�ʼ���ڴ��е��û���
	static{
		try{
	 java.io.File df=new java.io.File(dbFileName);
	 if(df.exists()&&!df.isDirectory()){//�����ļ����ڣ��Ҳ���Ŀ¼,���ȡ
		java.io.InputStream ins=new java.io.FileInputStream(dbFileName);
		java.io.ObjectInputStream ons=new  ObjectInputStream(ins);
		  userDB=(Map)ons.readObject();
	 LogTools.INFO(UserDao.class,"�������ļ���ȡ���ݳɹ�!");
	 }else{
	 LogTools.INFO(UserDao.class,"�����������ļ��������ձ�");
	 }
    }catch(Exception ef){
 LogTools.ERROR(UserDao.class,"��ʼ�����ļ�����!"+ef);
	 }
	}
}
