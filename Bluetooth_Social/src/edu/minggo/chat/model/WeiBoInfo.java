package edu.minggo.chat.model;

import java.io.Serializable;
import java.util.List;

public class WeiBoInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;	//微博内容
	private String origtext;//原始内容
	private String from;	//来源
	private String fromurl;	//来源url
	private String name;	//发表人账户名
	private String nick;	//发表人昵称
	private String head;	//发表者头像url
	private int status;		//微博状态，0-正常，1-系统删除，2-审核中，3-用户删除，4-根删除
	private int self;		//是否自已发的的微博，0-不是，1-是
	private int type;		//微博类型，1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论
	private int count;		//被转载次数
	private int mcount;		//点评次数
	private long timestamp;	//发表时间
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOrigtext() {
		return origtext;
	}
	public void setOrigtext(String origtext) {
		this.origtext = origtext;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFromurl() {
		return fromurl;
	}
	public void setFromurl(String fromurl) {
		this.fromurl = fromurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSelf() {
		return self;
	}
	public void setSelf(int self) {
		this.self = self;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getMcount() {
		return mcount;
	}
	public void setMcount(int mcount) {
		this.mcount = mcount;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	public WeiBoInfo getSource() {
		return source;
	}
	public void setSource(WeiBoInfo source) {
		this.source = source;
	}
	private List<String> image;//图片
	private WeiBoInfo source;
	@Override
	public String toString() {
		return "WeiBodinfo [text=" + text + ", origtext=" + origtext
				+ ", from=" + from + ", fromurl=" + fromurl + ", name=" + name
				+ ", nick=" + nick + ", head=" + head + ", status=" + status
				+ ", self=" + self + ", type=" + type + ", count=" + count
				+ ", mcount=" + mcount + ", timestamp=" + timestamp
				+ ", image=" + image + ", source=" + source + "]";
	}
	  
	
}
