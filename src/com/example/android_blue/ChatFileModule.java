package com.example.android_blue;

import java.io.Serializable;

public class ChatFileModule implements Serializable {
	/**
	 * �ļ�·��
	 */
	private String Route;
	/**
	 * �ļ���
	 */
	private String name;
	/**
	 * ���ļ���
	 */
	private String newName;
	/**
	 * �ļ���С
	 */
	private Long outputSize;
	/**
	 * �������ļ���С
	 */
	private Long inputSize;
	/**
	 * �Ƿ��������
	 */
	private Integer isGo;
	/**
	 * �ļ�
	 */
	private byte[] fileBytes;
	/**
	 * �洢��ɼ�������
	 */
	public boolean save;
	/**
	 * �ļ�·��
	 */
	public String getRoute() {
		return Route;
	}
	/**
	 * �ļ�·��
	 */
	public void setRoute(String route) {
		Route = route;
	}
	/**
	 * �ļ���
	 */
	public String getName() {
		return name;
	}
	/**
	 * �ļ���
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * ���ļ���
	 */
	public String getNewName() {
		return newName;
	}
	/**
	 * ���ļ���
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}
	/**
	 * �ļ���С
	 */
	public Long getOutputSize() {
		return outputSize;
	}
	/**
	 * �ļ���С
	 */
	public void setOutputSize(Long outputSize) {
		this.outputSize = outputSize;
	}
	/**
	 * �������ļ���С
	 */
	public Long getInputSize() {
		return inputSize;
	}
	/**
	 * �������ļ���С
	 */
	public void setInputSize(Long inputSize) {
		this.inputSize = inputSize;
	}
	/**
	 * �Ƿ��������
	 */
	public Integer getIsGo() {
		return isGo;
	}
	/**
	 * �Ƿ��������
	 */
	public void setIsGo(Integer isGo) {
		this.isGo = isGo;
	}
	/**
	 * �ļ�
	 */
	public byte[] getFileBytes() {
		return fileBytes;
	}
	/**
	 * �ļ�
	 */
	public void setFileBytes(byte[] fileBytes) {
		this.fileBytes = fileBytes;
	}
	/**
	 * �洢��ɼ�������
	 */
	public boolean isSave() {
		return save;
	}
	/**
	 * �洢��ɼ�������
	 */
	public void setSave(boolean save) {
		this.save = save;
	}

}
