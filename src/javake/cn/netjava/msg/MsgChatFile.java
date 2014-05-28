package javake.cn.netjava.msg;
//文件传送消息类
public class MsgChatFile extends MsgHead{
   private String fileName;//文件名字
   private byte[] fileData ;//文件的数据
   public String toString(){
	   return super.toString()+" fileName:"+fileName
	   +" 文件长度:"+fileData.length;
   }
   //以下为getter/setter
public String getFileName() {
	return fileName;
}
  
public void setFileName(String fileName) {
	this.fileName = fileName;
}

public byte[] getFileData() {
	return fileData;
}
public void setFileData(byte[] fileData) {
	this.fileData = fileData;
}

}
