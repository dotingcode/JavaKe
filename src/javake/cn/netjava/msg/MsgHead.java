package javake.cn.netjava.msg;
/**
 * 所有消息的父类
 * @author 蓝杰 www.NetJava.cn
 */
public class MsgHead {

	private int totalLen;//消息总长度
	private byte type;//消息类型
	private int dest;//目标用户jk号
	private int src;//发送用户的jk号
	//以下为getter/setter方法
	public String toString(){
		return "totalLen:"+totalLen+" type:"+type
		+" dest:"+dest+" src:"+src;
	}

	public int getTotalLen() {
		return totalLen;
	}

	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}
}
