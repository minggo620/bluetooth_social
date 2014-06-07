package edu.minggo.chat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeiBoListData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long timestamp;
	private int hasnext;
	private int totalNum;
	private List<WeiBoInfo> info = new ArrayList<WeiBoInfo>();
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getHasnext() {
		return hasnext;
	}
	public void setHasnext(int hasnext) {
		this.hasnext = hasnext;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public List<WeiBoInfo> getInfo() {
		return info;
	}
	public void setInfo(List<WeiBoInfo> info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "WeiBoData [timestamp=" + timestamp + ", hasnext=" + hasnext
				+ ", totalNum=" + totalNum + ", info=" + info + "]";
	}
	
}
