package javake.cn.netjava.msg;
//�ļ�������Ϣ��
public class MsgChatFile extends MsgHead{
   private String fileName;//�ļ�����
   private byte[] fileData ;//�ļ�������
   public String toString(){
	   return super.toString()+" fileName:"+fileName
	   +" �ļ�����:"+fileData.length;
   }
   //����Ϊgetter/setter
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
