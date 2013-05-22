package com.example.android_blue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;

import com.example.android_blue.R.string;

import android.R.integer;
import android.R.raw;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.Toast;

public class ObjectUtil {
	/**
	 * sd·��
	 */
	private static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/";
	/**
	 * �����С
	 */
	public static final int UPLOAD_CHCHE = 1024 * 2;
	/**
	 * ��ʱ�ļ���չ��
	 */
	private static String temporary = ".linshi";

	/**
	 * ���ļ�ת����byte����
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Object[] getBytesFromFile(File file, long start, int size)
			throws IOException {
		InputStream is = new FileInputStream(file);
		is.skip(start);
		byte[] bytes = new byte[size];
		// ��Ҫ����
		int isGo = 0;
		long length = start + is.read(bytes, 0, size);
		isGo = file.length() == length ? 1 : 0;
		is.close();
		return new Object[] { length, bytes, isGo };
	}

	/**
	 * ������ת����byte����
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] getBytesFromObject(Object object) throws Exception {
		ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
		ObjectOutputStream output = new ObjectOutputStream(arrayOutput);
		output.writeObject(object);
		byte[] out = arrayOutput.toByteArray();
		arrayOutput.close();
		output.close();
		return out;
	}

	/**
	 * ��byte����ת����Object����
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object getBytesToObject(byte[] bytes) throws Exception {
		ByteArrayInputStream arrayInput = new ByteArrayInputStream(bytes);
		ObjectInputStream input = new ObjectInputStream(arrayInput);
		Object obj = input.readObject();
		arrayInput.close();
		input.close();
		return obj;
	}

	/**
	 * ����һ������map
	 * 
	 * @param string
	 * @return
	 */
	public static ChatFileModule startFileMap(String file_Route, Context context)
			throws Exception {
		File file = new File(file_Route);
		ChatFileModule chatFileModule = null;
		if (file.exists()) {
			chatFileModule = new ChatFileModule();
			// �޳���·��
			String pathString = file
					.getParentFile()
					.toString()
					.replaceAll(
							Environment.getExternalStorageDirectory()
									.toString(), "");
			// �ļ���·��(��������·��)
			chatFileModule.setRoute(pathString);
			// �ļ���
			chatFileModule.setName(file.getName());
			// �ļ���С
			chatFileModule.setOutputSize(file.length());
			// ȡ���ļ�
			Object[] output = ObjectUtil
					.getBytesFromFile(file, 0, UPLOAD_CHCHE);
			// �����ϴ�����
			chatFileModule.setFileBytes((byte[]) output[1]);
			// �Ƿ���Ҫ����
			chatFileModule.setIsGo((Integer) output[2]);
			// �����ش�С
			chatFileModule.setInputSize((Long) output[0]);
			// �Ƿ��Ѵ洢
			chatFileModule.setSave(false);
		}
		return chatFileModule;
	}

	/**
	 * �����ϴ�Map
	 * 
	 * @param map
	 * @param context
	 * @return
	 */
	public static ChatFileModule aheadFileMap(ChatFileModule chatFileModule,
			Context context) throws Exception {
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(SDPATH);
		sbBuffer.append(chatFileModule.getRoute() == null ? "" : chatFileModule
				.getRoute());
		sbBuffer.append(chatFileModule.getName());
		File file = new File(sbBuffer.toString());
		if (file.exists()) {
			// ȡ���ļ�
			Object[] output = ObjectUtil.getBytesFromFile(file,
					chatFileModule.getInputSize(), UPLOAD_CHCHE);
			// �����ϴ�����
			chatFileModule.setFileBytes((byte[]) output[1]);
			// �Ƿ���Ҫ����
			chatFileModule.setIsGo((Integer) output[2]);
			// �����ش�С
			chatFileModule.setInputSize((Long) output[0]);
			// �Ƿ��Ѵ洢
			chatFileModule.setSave(false);
		} else {
			return null;
		}
		return chatFileModule;
	}

	/**
	 * ���д洢
	 * 
	 * @param map
	 * @return
	 */
	public static final int newSaveSize = 1024 * 1024;
	public static final int newSaveGo = newSaveSize / UPLOAD_CHCHE - 1;
	public static byte[] newSave = new byte[newSaveSize];
	public static int newSaveint = 0;
	public static long nowInput = 0;

	public static ChatFileModule saveFile(ChatFileModule chatFileModule,
			Context context) throws Exception {
		if (chatFileModule.getInputSize() > nowInput) {
			// ����д�����
			// ��ֹ�ظ���
			nowInput = chatFileModule.getInputSize();
			byte[] writByte = chatFileModule.getFileBytes();
			StringBuffer sbBuffer = new StringBuffer();
			sbBuffer.append(SDPATH);
			sbBuffer.append(chatFileModule.getRoute() == null ? ""
					: chatFileModule.getRoute());
			sbBuffer.append(chatFileModule.getName().split("\\.")[0]
					+ temporary);
			StringBuffer mksBuffer = new StringBuffer();
			mksBuffer.append(SDPATH);
			mksBuffer.append(chatFileModule.getRoute() == null ? ""
					: chatFileModule.getRoute());
			File file = new File(sbBuffer.toString());
			File mk = new File(mksBuffer.toString());
			if (!mk.exists() || !file.exists()) {
				// �����ļ��к���ʱ�ļ��ļ�
				mk.mkdirs();
				file.createNewFile();
			}
			switch (chatFileModule.getIsGo()) {
			case 0:
				// ��Ҫ����
				switch (newSaveint) {
				case newSaveGo:
					RandomAccessFile rf = new RandomAccessFile(file, "rwd");
					rf.seek(file.length());
					rf.write(newSave, 0, newSaveSize);
					rf.close();
					newSaveint = 0;
					newSave = new byte[newSaveSize];
					break;

				default:
					System.arraycopy(writByte, 0, newSave, newSaveint
							* UPLOAD_CHCHE, UPLOAD_CHCHE);
					newSaveint++;
					break;
				}
				chatFileModule.setFileBytes(null);
				chatFileModule.setSave(true);
				return chatFileModule;
			case 1:
				RandomAccessFile rf = new RandomAccessFile(file, "rwd");
				rf.seek(file.length());
				// ������Ϊ����д��
				if (newSaveint != 0) {
					switch (newSaveint) {
					case newSaveGo:
						rf.write(newSave, 0, newSaveSize);
						break;

					default:
						rf.write(newSave, 0, UPLOAD_CHCHE * newSaveint);
						break;
					}
					newSaveint = 0;
					newSave = new byte[newSaveSize];
					rf.seek(file.length());
				}
				String sizeString = String.valueOf(chatFileModule
						.getOutputSize() - file.length());
				rf.write(writByte, 0, Integer.parseInt(sizeString));
				rf.close();
				StringBuffer newNameBuffer = new StringBuffer();
				newNameBuffer.append(SDPATH);
				newNameBuffer.append(chatFileModule.getRoute() == null ? ""
						: chatFileModule.getRoute());
				newNameBuffer.append(chatFileModule.getName());
				file.renameTo(new File(newNameBuffer.toString()));
				chatFileModule = null;
				Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
				return null;
			}

//			RandomAccessFile rf = new RandomAccessFile(file, "rwd");
//			rf.seek(file.length());
//			switch (chatFileModule.getIsGo()) {
//			case 0:
//				// ��Ҫ����
//				rf.write(writByte, 0, UPLOAD_CHCHE);
//				rf.close();
//				chatFileModule.setFileBytes(null);
//				chatFileModule.setSave(true);
//				return chatFileModule;
//			case 1:
//				// ����Ҫ����
//				// ������ص���
//				String sizeString = String.valueOf(chatFileModule
//						.getOutputSize() - file.length());
//				rf.write(writByte, 0, Integer.parseInt(sizeString));
//				rf.close();
//				StringBuffer newNameBuffer = new StringBuffer();
//				newNameBuffer.append(SDPATH);
//				newNameBuffer.append(chatFileModule.getRoute() == null ? ""
//						: chatFileModule.getRoute());
//				newNameBuffer.append(chatFileModule.getName());
//				file.renameTo(new File(newNameBuffer.toString()));
//				chatFileModule = null;
//				Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
//				return null;
//			}
			return chatFileModule;
		} else {
			chatFileModule.setSave(true);
			chatFileModule.setFileBytes(null);
			return chatFileModule;
		}
	}
}
