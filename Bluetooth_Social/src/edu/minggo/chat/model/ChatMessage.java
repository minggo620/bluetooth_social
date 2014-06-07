package edu.minggo.chat.model;


public class ChatMessage {
	private int messageID;
	private String bluetoothAddress;
	private String photoURL;
	private String voiceURL;
	
	public int getMessageID() {
		return messageID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	public String getBluetoothAddress() {
		return bluetoothAddress;
	}
	public void setBluetoothAddress(String bluetoothAddress) {
		this.bluetoothAddress = bluetoothAddress;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}
	public String getVoiceURL() {
		return voiceURL;
	}
	public void setVoiceURL(String voiceURL) {
		this.voiceURL = voiceURL;
	}
	
}
