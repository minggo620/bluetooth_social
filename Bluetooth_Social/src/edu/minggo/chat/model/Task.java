package edu.minggo.chat.model;

import java.util.Map;

public class Task {
	private int taskID;//
	@SuppressWarnings("rawtypes")
	private Map taskParam;//
	public static final int TASK_GET_PAIR_DEVICE = 0;    //获取配对的蓝牙设备
	public static final int TASK_DETECT_DEVICE=1;        //探测蓝牙设备
	public static final int TASK_GET_DETECT_DEVICE = 2;  //获取探测蓝牙设备
	public static final int TASK_CONECT_DEVICE = 3;      //连接蓝牙设备
	public static final int TASK_TRANSFER_STRING_MESSAGE = 4;  //发送字符串信息
	public static final int TASK_TRANSFER_PHOTO_MEASSAGE = 5;  //发发送图片信息
	public static final int TASK_TRANSFER_VOICE_MESSAGE = 6;   //发送语音信息
	public static final int TASK_LOGIN_SUCCESS = 7; //登陆成功主页初始显示
	public static final int TASK_CHAT_ONRESUME = 8; //页面重新回到当前
	public static final int TASK_SEND_MESSAGE = 9;  //发送信息
	public static final int TASK_INIT_SERVICE = 10; //初始化整个系统的服务
	public static final int TASK_SEND_IMAGE = 11;//发送图片
	public static final int TASK_SEND_SOUND = 12;//发送声音
	public static final int TASK_SEND_LOCATION = 19;//发送位置
	public static final int TASK_GET_USER_HOMETIMEINLINE = 13;//获取朋友列表
	public static final int TASK_GET_MY_INFORMATION = 14;//获取自己信息
	public static final int TASK_MODIFY_MYINFO = 15;//获取自己信息
	public static final int TASK_MYPHOTO_ADD = 16;//添加照片到相册
	public static final int TASK_GET_USER_ADDRESS = 17;//通讯录列表
	public static final int TASK_REFREAH_GALLERY = 18;//更新相册
	public static final int TASK_CHESS_NEXT = 20;//更新相册
	
	public Task(int id, @SuppressWarnings("rawtypes") Map param) {
		this.taskID = id;
		this.taskParam = param;
	}
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	@SuppressWarnings("rawtypes")
	public Map getTaskParam() {
		return taskParam;
	}
	public void setTaskParam(@SuppressWarnings("rawtypes") Map taskParam) {
		this.taskParam = taskParam;
	}
	
}
