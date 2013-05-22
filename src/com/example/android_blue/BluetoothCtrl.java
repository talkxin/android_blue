package com.example.android_blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class BluetoothCtrl {
	// ��������
	private static BluetoothDevice remoteDevice;
	// ����ͨѶ��
	private BluetoothChat bluetoothChat;
	// ����handler
	private Handler handler = new Handler();
	// �����context
	private Context context;

	private BluetoothAdapter bluetoothAdapter;
	/**
	 * ���ӵ�������ַ
	 */
	private String addr;
	/**
	 * ���ӵ�pin��
	 */
	private String pin;

	/**
	 * ���췽������Context
	 * 
	 * @param context
	 */
	public BluetoothCtrl(Context context) {
		this.context = context;
		// ��ȡ��������
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// ����������ʼ��
		bluetoothChat = new BluetoothChat(context);
	}

	// Runnable startRunnable = new Runnable() {
	// boolean on = true;
	//
	// @Override
	// public void run() {
	// bluetoothAdapter.cancelDiscovery();
	// if (!bluetoothAdapter.isEnabled()) {
	// // δ��������
	// if (on) {
	// bluetoothAdapter.enable();
	// on = false;
	// }
	// handler.postDelayed(startRunnable, 1000);
	// } else {
	// // �����������
	// pair(addr, pin);
	// // �Ƴ������������
	// handler.removeCallbacks(startRunnable);
	// }
	// }
	// };

	public class startRunnable extends Thread {
		boolean on = true;

		@Override
		public void run() {
			while (true) {
				bluetoothAdapter.cancelDiscovery();
				if (!bluetoothAdapter.isEnabled()) {
					// δ��������
					if (on) {
						bluetoothAdapter.enable();
						on = false;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// �����������
					pair(addr, pin);
					break;
					// �Ƴ������������
					// handler.removeCallbacks(startRunnable);
				}
			}
		}
	}

	/**
	 * ����߳�
	 */
	// Runnable runnable = new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// runpair(addr, pin);
	// }
	// };
	public static int LONG_TIME_CONNECTED = 360;

	public class runnable extends Thread {
		int count = 0;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			bluetoothChat = new BluetoothChat(context);
			if (!BluetoothAdapter.checkBluetoothAddress(addr)) { // ���������ַ�Ƿ���Ч
				return;
			}
			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(addr);
			while (true) {
				if (count == LONG_TIME_CONNECTED) {
					Looper.loop();
				}
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// ���δƥ�䣬�����ִ��
					// handler.postDelayed(runnable, 5000);
					try {
						ClsUtils.setPin(device.getClass(), device, pin); // �ֻ��������ɼ������
						ClsUtils.createBond(device.getClass(), device);
						remoteDevice = device; // �����ϾͰ�����豸���󴫸�ȫ�ֵ�remoteDevice
						Thread.sleep(5000);
						count++;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// ��ֵ��ȫ��
					remoteDevice = device;
					// �Ƴ��̺߳���������
					try {
						bluetoothChat.goConnect(device);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					Looper.loop();
				}
			}
		}
	}

	/**
	 * �Ƴ���Խ���
	 */
	Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			endPir();
		}
	};

	/**
	 * ��Խ��̷���
	 * 
	 * @param strAddr
	 * @param strPsw
	 */
	// public void runpair(String strAddr, String strPsw) {
	// // boolean result = false;
	// // BluetoothAdapter bluetoothAdapter = BluetoothAdapter
	// // .getDefaultAdapter();
	// // bluetoothAdapter.cancelDiscovery();
	// BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
	// if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	// // ���δƥ�䣬�����ִ��
	// pair(strAddr, strPsw);
	// // handler.postDelayed(runnable, 5000);
	// } else {
	// // ��ֵ��ȫ��
	// this.remoteDevice = device;
	// // ��ƥ�䣬�Ƴ��߳�
	// handler.removeCallbacks(runnable);
	// // �Ƴ��̺߳���������
	// bluetoothChat.goConnect(device);
	// }
	//
	// }

	// �������
	public boolean pair(String strAddr, String strPsw) {
		// ��ȫ�ֵĵ�ַ���ֵ
		this.addr = strAddr;
		this.pin = strPsw;
		boolean result = false;
		// BluetoothAdapter bluetoothAdapter = BluetoothAdapter
		// .getDefaultAdapter();
		// // ������ַ--> bluetoothAdapter.getAddress()
		// bluetoothAdapter.cancelDiscovery();
		if (!bluetoothAdapter.isEnabled()) {
			// δ��������
			// bluetoothAdapter.enable();
			// handler.post(startRunnable);
			new startRunnable().start();
			return false;

		} else {
			new runnable().start();
			// // ����������ʼ��
			// bluetoothChat = new BluetoothChat(context);
			// if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { //
			// ���������ַ�Ƿ���Ч
			// return false;
			// }
			// // Զ���豸��ַ--> strAddr
			// BluetoothDevice device =
			// bluetoothAdapter.getRemoteDevice(strAddr);
			//
			// if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			// // �ֻ�δƥ��
			// try {
			// Log.d("mylog", "NOT BOND_BONDED");
			// ClsUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
			// ClsUtils.createBond(device.getClass(), device);
			// remoteDevice = device; // �����ϾͰ�����豸���󴫸�ȫ�ֵ�remoteDevice
			// result = true;
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// Log.d("mylog", "setPiN failed!");
			// e.printStackTrace();
			// } //
			// handler.postDelayed(runnable, 5000);
			// } else {
			// // �ֻ���ƥ��������
			// bluetoothChat.goConnect(device);
			// }
		}
		return result;
	}

	/**
	 * �Ƴ���Խ���
	 * 
	 * @param strAddr
	 */
	public void endPir() {
		// ����������ֹͣ
		if (bluetoothChat.isBluetooth()) {
			bluetoothChat.getmChatService().stop();
			bluetoothChat.getmChatService().start();
			bluetoothChat.setBluetooth(false);
			// ���߳���ͣ2��
			handler.postDelayed(runnable2, 2000);
		}
		// BluetoothAdapter bluetoothAdapter = BluetoothAdapter
		// .getDefaultAdapter();
		// bluetoothAdapter.cancelDiscovery();
		if (addr == null || addr.equals(""))
			return;
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(this.addr);
		try {
			if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
				// ��ƥ�������Ƴ�
				ClsUtils.removeBond(device.getClass(), device);
				handler.postDelayed(runnable2, 5000);
			} else {
				// �Ѿ��Ƴ�
				handler.removeCallbacks(runnable2);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public BluetoothChat getBluetoothChat() {
		if (!bluetoothAdapter.isEnabled()) {
			return null;
		}
		return bluetoothChat;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public static BluetoothDevice getRemoteDevice() {
		return remoteDevice;
	}

}
