package com.example.android_blue;

import java.io.File;
import java.util.HashMap;

import android.R.integer;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.MaskFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

/**
 * ������������ķ�װ��
 * 
 * @author talkliu
 * 
 */
public class BluetoothChat {
	// ���handler��״̬
	public static final int MESSAGE_STATE_CHANGE = 1;
	// ��ȡ״̬
	public static final int MESSAGE_READ = 2;
	// ����״̬
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	// ���Ͷ���
	public static final int MESSAGE_TOOBJECT = 6;
	// �����ַ���
	public static final int MESSAGE_TOSTRING = 7;
	// �����ļ�
	public static final int MESSAGE_TOFILE = 8;

	// handler�ش�����Ϣkey
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	/**
	 * �Ƿ����ӳɹ�
	 */
	private boolean isBluetooth = false;

	// ��Ժ������
	private BluetoothDevice device;
	// ��ȡ��ContextȨ��
	private Context context;
	// �������ӹ��������
	private BluetoothChatService mChatService;
	// ������������м��ӵ��߳�
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					// ���ӳɹ�
					Toast.makeText(context, "���ӳɹ�", Toast.LENGTH_SHORT).show();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					// ��������״̬
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					// ��Ϊ����������½��еĲ���
					break;
				}
				break;
			case MESSAGE_WRITE:
				// �Լ����͵���Ϣ
				break;
			case MESSAGE_READ:
				// ���յ���Ϣ
				ChatModule module = (ChatModule) msg.obj;
				switch (msg.arg2) {
				case MESSAGE_TOOBJECT:
					// ȡ��Object
					module.getChatObject();
					break;
				case MESSAGE_TOSTRING:
					// ȡ�������ַ���
					useString(module.getChatString());
					break;
				case MESSAGE_TOFILE:
					// ȡ�����ݵ��ļ�
					useFile(module.getChatFileModule());
					break;
				}
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				// ���ӳɹ���Ĳ���
				// msg.getData().getString(DEVICE_NAME); ��ȡ���ӵ�����
				Toast.makeText(context, "���ӳɹ�", Toast.LENGTH_SHORT).show();
				// ���ӳɹ��󣬽�״̬��Ϊ�ɹ�
				isBluetooth = true;
				break;
			case MESSAGE_TOAST:
				// ʧȥ����ʱ�Ĳ���
				switch (msg.getData().getInt(TOAST)) {
				case BluetoothChatService.STATE_UNABLE:
					if (mChatService != null) {
						mChatService.stop();
						mChatService.start();
						isBluetooth = false;
					}
					break;
				case BluetoothChatService.STATE_LOST:
					// ʧȥ���ӵ�ʱ�����³�ʼ������
					if (mChatService != null) {
						mChatService.stop();
						mChatService.start();
						isBluetooth = false;
					}
					break;
				}
				break;
			}
		}
	};

	/**
	 * ���췽������ʼ��ʱ���������������ӣ�����Ժ�������ӣ�
	 * 
	 * @param context
	 * @param device
	 */
	public BluetoothChat(Context context) {
		this.context = context;
		mChatService = new BluetoothChatService(context, mHandler);
		if (mChatService != null) {
			// ��ʼ�����ӣ�������
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				mChatService.start();
			}
		}
	}

	/**
	 * �رշ���
	 */
	public void stopService() {
		mChatService.stop();
	}

	/**
	 * ������Ϣ�÷���
	 * 
	 * @param message
	 */
	public void sendMessage(byte[] message) {
		// �ж�����״̬
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			// ����ǿ������򲻷���
			return;
		}

		if (null != message && message.length > 0) {
			// ������Ϣ
			try {
				mChatService.write(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * ��������
	 * 
	 * @param device
	 */
	public void goConnect(BluetoothDevice device) {
		mChatService.connect(device);
	}

	/**
	 * �������
	 * 
	 * @param obj
	 */
	private void useObject(Object obj) {

	}

	/**
	 * �����ַ���
	 * 
	 * @param string
	 */
	private void useString(String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ��������ļ�
	 * 
	 * @param fileMap
	 */
	// private void useFile(HashMap<String, Object> fileMap) {
	// // ���д洢�ļ�
	// try {
	// if ((Boolean) fileMap.get(ObjectUtil.file_save)) {
	// fileMap = ObjectUtil.aheadFileMap(fileMap, context);
	// } else {
	// fileMap = ObjectUtil.saveFile(fileMap, this.context);
	// }
	// if (null != fileMap) {
	// gtoFile(fileMap);
	// }
	// if (MainActivity.firstBar.getMax() != Integer.parseInt(fileMap.get(
	// "file_outputSize").toString())) {
	// MainActivity.firstBar.setMax(Integer.parseInt(fileMap.get(
	// "file_outputSize").toString()));
	// MainActivity.firstBar.setVisibility(View.VISIBLE);
	// MainActivity.firstBar.setProgress(Integer.parseInt(fileMap.get(
	// "file_inputSize").toString()));
	// } else {
	// MainActivity.firstBar.setProgress(Integer.parseInt(fileMap.get(
	// "file_inputSize").toString()));
	// }
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
	// e.printStackTrace();
	// }
	// }

	private void useFile(ChatFileModule chatFileModule) {
		// ���д洢�ļ�
		try {
			if (chatFileModule.isSave()) {
				chatFileModule = ObjectUtil.aheadFileMap(chatFileModule,
						context);
			} else {
				chatFileModule = ObjectUtil.saveFile(chatFileModule,
						this.context);
			}
			if (null != chatFileModule) {
				gtoFile(chatFileModule);
			}
			if (null != chatFileModule) {
				int zongliang = Integer.parseInt(String.valueOf(chatFileModule
						.getOutputSize()));
				int zengliang = Integer.parseInt(String.valueOf(chatFileModule
						.getInputSize()));
				if (MainActivity.firstBar.getMax() != zongliang) {
					MainActivity.firstBar.setMax(zongliang);
					MainActivity.firstBar.setVisibility(View.VISIBLE);
				}
				MainActivity.firstBar.setProgress(zengliang);
				MainActivity.zhuangtaiTextView.setText(zongliang + "======"
						+ zengliang);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/**
	 * �������ļ�
	 * 
	 * @param path
	 */
	public void gtoFile(ChatFileModule chatFileModule) {
		// �ж�����״̬
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			// ����ǿ������򲻷���
			return;
		}
		try {
			// HashMap<String, Object> gotoMap = new HashMap<String, Object>();
			// gotoMap.put(BluetoothChatService.MESSAGE_NAME,
			// BluetoothChat.MESSAGE_TOFILE);
			ChatModule chatModule = new ChatModule();
			chatModule.setChatState(BluetoothChat.MESSAGE_TOFILE);
			// ��װ�ɴ����ļ�ģʽ������
			// gotoMap.put(BluetoothChatService.MESSAGE_OBJECT, map);
			chatModule.setChatFileModule(chatFileModule);
			mChatService.write(ObjectUtil.getBytesFromObject(chatModule));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ���ض�������ֹͣ����
	 * 
	 * @return
	 */
	public BluetoothChatService getmChatService() {
		return mChatService;
	}

	/**
	 * �����Ƿ����ӳɹ�
	 * 
	 * @return
	 */
	public boolean isBluetooth() {
		return isBluetooth;
	}

	/**
	 * ��ʼ��
	 * 
	 * @param isBluetooth
	 */
	public void setBluetooth(boolean isBluetooth) {
		this.isBluetooth = isBluetooth;
	}

}
