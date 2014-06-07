package edu.minggo.chat.model;

/**
 * 消息实体类
 * @author Administrator
 */
public class MessageEntity {

	private String name;
	private String date;             //消息日期
	private String text;			 
	private String imageUrl;         //头像路径
	private int layoutID;            //消息的类型的布局

	private int TtmType;             //消息类型（1.字符串 2.图片 3.声音 4.位置）
	private String remoteDeviceNo;   //远程蓝牙序列号
	private String myDeviceNo;       //我的蓝牙序列号
	private String TtmContent;       //消息内容
	private String ImageFile;        //图片文件路径
	private String VoiceFile;        //声音文件路径
	private String TtmTime;          //消息事件
	private String voiceTime;
	private String imageId;          //我的头像图片ID

	private int type;

	private int isRead; // 0表示未读，1表示已读
	private int isReplyLocation; // 0表示未回复，1表示已回复

	private int isSendImg;

	/**
	 * 文字和表情消息
	 * 
	 * @param TtmContent
	 * @param layoutID
	 * @param imageId 我的头像图片ID
	 * @param TtmTime
	 */
	public MessageEntity(String TtmContent, int layoutID, String imageId,
			String TtmTime) {

		this.layoutID = layoutID;
		this.imageId = imageId;
		this.TtmContent = TtmContent;
		this.TtmTime = TtmTime;
	}

	/**
	 * 声音和图片消息
	 * 
	 * @param text
	 * @param layoutID
	 * @param imageId
	 * @param ImageFile
	 * @param VoiceFile
	 */
	public MessageEntity(String text, int layoutID, String imageId,
			String ImageFile, String VoiceFile) {

		this.text = text;
		this.layoutID = layoutID;
		this.imageId = imageId;
		this.ImageFile = ImageFile;
		this.VoiceFile = VoiceFile;
	}

	public MessageEntity(String myDeviceNo, String remoteDeviceNo,
			String TtmContent, int layoutID, String imageId, int TtmType,
			String TtmTime) {

		this.myDeviceNo = myDeviceNo;
		this.remoteDeviceNo = remoteDeviceNo;
		this.TtmContent = TtmContent;
		this.layoutID = layoutID;
		this.imageId = imageId;
		this.TtmType = TtmType;
		this.TtmTime = TtmTime;
	}

	public MessageEntity(int TtmType, String myDeviceNo,
			String remoteDeviceNo, String TtmContent, String TtmTime,
			int isRead, int isReplyLocation) {

		this.TtmType = TtmType;
		this.myDeviceNo = myDeviceNo;
		this.remoteDeviceNo = remoteDeviceNo;
		this.TtmContent = TtmContent;
		this.TtmTime = TtmTime;
		this.isRead = isRead;
		this.isReplyLocation = isReplyLocation;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLayoutID() {
		return layoutID;
	}

	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public MessageEntity() {
	}

	public int getTtmType() {
		return TtmType;
	}

	public void setTtmType(int ttmType) {
		TtmType = ttmType;
	}

	public String getRemoteDeviceNo() {
		return remoteDeviceNo;
	}

	public void setRemoteDeviceNo(String remoteDeviceNo) {
		this.remoteDeviceNo = remoteDeviceNo;
	}

	public String getMyDeviceNo() {
		return myDeviceNo;
	}

	public void setMyDeviceNo(String myDeviceNo) {
		this.myDeviceNo = myDeviceNo;
	}

	public String getTtmContent() {
		return TtmContent;
	}

	public void setTtmContent(String ttmContent) {
		TtmContent = ttmContent;
	}

	public String getTtmTime() {
		return TtmTime;
	}

	public void setTtmTime(String ttmTime) {
		TtmTime = ttmTime;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImageFile() {
		return ImageFile;
	}

	public void setImageFile(String imageFile) {
		ImageFile = imageFile;
	}

	public String getVoiceFile() {
		return VoiceFile;
	}

	public void setVoiceFile(String voiceFile) {
		VoiceFile = voiceFile;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getIsReplyLocation() {
		return isReplyLocation;
	}

	public void setIsReplyLocation(int isReplyLocation) {
		this.isReplyLocation = isReplyLocation;
	}

	public int getIsSendImg() {
		return isSendImg;
	}

	public void setIsSendImg(int isSendImg) {
		this.isSendImg = isSendImg;
	}

	public String getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(String voiceTime) {
		this.voiceTime = voiceTime;
	}
	
}
