package javake.cn.netjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 *JK��ʱͨ��ϵͳ ���ѷ�����Ķ���
 * @author ���� www.NetJava.cn
 */
public class TeamInfo implements java.io.Serializable{
	
	 private int id;  //��ʶ���
	 private String name; //������
	 private UserInfo owerUser;//�˱�������JK�û�����
	//�ڴ˷����ڵĺ��Ѷ����
	 private List<UserInfo> budyList=new ArrayList();
  
	 /**
	  * ����һ���������
	  * @param id:�����ΨһID
	  * @param name:������
	  * @param owerUser:���������û�����
	  */
	 public TeamInfo(int id,String name,UserInfo owerUser){
		 this.id=id;
		 this.name=name;
		 this.owerUser=owerUser;
	 }
	 
	//Ϊ�˷������һ�����Ѷ���
		public void addBudy(UserInfo  buddy ) {
			this.budyList.add(buddy);
		}
//����Ϊgetter/setter����
	 
	 public TeamInfo(String name){
		 
		 this.name=name;
		 
	 }
	 
	 public String toString(){
		 return "id:"+id+" name:"+name;
	 }
	 
	 public List<UserInfo> getBudyList() {
			return budyList;
		}
	 
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	 

	public UserInfo getOwerUser() {
		return owerUser;
	}

	 

	
	

}
