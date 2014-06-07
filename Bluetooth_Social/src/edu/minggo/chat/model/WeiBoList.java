package edu.minggo.chat.model;

import java.io.Serializable;

public class WeiBoList implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ret;		//返回值：0成功，非0失败
	private String msg;		//错误信息
	private int errorcode;//返回错误码
	private WeiBoListData data;
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public WeiBoListData getData() {
		return data;
	}
	public void setData(WeiBoListData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "WeiBoList [ret=" + ret + ", msg=" + msg + ", errorcode="
				+ errorcode + ", data=" + data + "]";
	}
	
	
}
