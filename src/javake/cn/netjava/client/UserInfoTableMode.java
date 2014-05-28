package javake.cn.netjava.client;

import java.util.List;

import javake.cn.netjava.model.UserInfo;
import javake.cn.netjava.server.ServerThread;

import javax.swing.event.TableModelListener;

 
/**
 *JK即时通信系统 显示查找到的用户信息的表模型
 *当查找结果消息从服务器返回时，用以在界面Jtable中
 *显示找到的用户信息
 * @author 蓝杰 www.NetJava.cn
 */
public class UserInfoTableMode 
implements javax.swing.table.TableModel{
   //指向服务器管理类中保存的 用户处理线程对象队列
	private  List<UserInfo> users; 
	
	//创建表模型对象时,传入用户列表
	public UserInfoTableMode(List<UserInfo> ctList){
		this.users=ctList;
	}
     //多少行 实现TableModel中的
    public int getRowCount(){
    	return users.size();
    }

   //多少列 实现TableModel中的
    public int getColumnCount(){
    	return 2; //JK号,用户名
    }
    //列标题 实现TableModel中的
    public String getColumnName(int columnIndex){
    	if(columnIndex==0){
    		return "jk号";
    	}else if(columnIndex==1){
        		return "呢称";
    	}else{ return "error";}
    }
    //每一列的数据类型 实现TableModel中的
    public Class<?> getColumnClass(int columnIndex){
    	return String.class;
    }
    //单元格是否可编辑 实现TableModel中的
    public boolean isCellEditable(int rowIndex, int columnIndex){
    	return false;
    }
    //表格显示时，调用这个方法 实现TableModel中的
    public Object getValueAt(int rowIndex, int columnIndex){
    	//第几行，就是队列中第几个线程对象代表的用户
    	UserInfo  user=users.get(rowIndex);
    	if(columnIndex==0){
    		return user.getJkNum();
    	}else if(columnIndex==1){
        		return user.getNikeName();
    	}else{ return "error"; }
    }
 //暂不使用  
    public void setValueAt(Object a, int r, int c){}
    public void addTableModelListener(TableModelListener l){}
    public void removeTableModelListener(TableModelListener l){}
}
