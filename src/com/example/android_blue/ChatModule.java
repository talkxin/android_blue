package com.example.android_blue;

import java.io.Serializable;

public class ChatModule implements Serializable {
	/**
	 * �Ի�״̬
	 */
	private int chatState;
	/**
	 * �û��洢�����Ķ���
	 */
	private Object chatObject;
	/**
	 * �û��洢�������ַ���
	 */
	private String chatString;
	/**
	 * �û��洢�������ļ�����
	 */
	private ChatFileModule chatFileModule;

	/**
	 * ���췽��
	 * 
	 * @param chatState
	 * @param chatObject
	 * @param chatString
	 * @param chatFileModule
	 */
	public ChatModule(int chatState, Object chatObject, String chatString,
			ChatFileModule chatFileModule) {
		this.chatState = chatState;
		this.chatObject = chatObject;
		this.chatString = chatString;
		this.chatFileModule = chatFileModule;
	}

	/**
	 * �չ���
	 */
	public ChatModule() {

	}

	public int getChatState() {
		return chatState;
	}

	public void setChatState(int chatState) {
		this.chatState = chatState;
	}

	public Object getChatObject() {
		return chatObject;
	}

	public void setChatObject(Object chatObject) {
		this.chatObject = chatObject;
	}

	public String getChatString() {
		return chatString;
	}

	public void setChatString(String chatString) {
		this.chatString = chatString;
	}

	public ChatFileModule getChatFileModule() {
		return chatFileModule;
	}

	public void setChatFileModule(ChatFileModule chatFileModule) {
		this.chatFileModule = chatFileModule;
	}
}
