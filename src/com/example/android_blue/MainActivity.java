package com.example.android_blue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	// // ��������
	// private static BluetoothDevice remoteDevice;
	// // ����ͨѶ��
	// private static BluetoothChat bluetoothChat;
	public String addr;
	public String pin = "123456";
	/**
	 * �������ӿ���
	 */
	public BluetoothCtrl bluetoothCtrl;
	//
	// Handler handler = new Handler();

	public static ProgressBar firstBar = null;
	public static TextView zhuangtaiTextView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView text = (TextView) findViewById(R.id.myBlueTooth);
		// bluetoothChat = new BluetoothChat(this.getApplicationContext());
		bluetoothCtrl = new BluetoothCtrl(this.getApplicationContext());
		text.setText(BluetoothAdapter.getDefaultAdapter().getAddress());
		Button bbpc = (Button) findViewById(R.id.bbpc);
		Button bb23 = (Button) findViewById(R.id.bb23);
		Button bb40 = (Button) findViewById(R.id.bb40);
		Button bb40xoom = (Button) findViewById(R.id.bb40xoom);
		Button button2 = (Button) findViewById(R.id.cc);
		bbpc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addr = "60:D8:19:EB:7E:A6";
				// pair(addr, pin);
				bluetoothCtrl.pair(addr, pin);
				// handler.postDelayed(runnable, 5000);
			}
		});
		bb23.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addr = "14:AB:39:FD:8A:8B";
				// pair(addr, pin);
				bluetoothCtrl.pair(addr, pin);
				// handler.postDelayed(runnable, 5000);
			}
		});
		bb40.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addr = "74:2F:68:FD:8A:8B";
				// pair(addr, pin);
				bluetoothCtrl.pair(addr, pin);
				// handler.postDelayed(runnable, 5000);
			}
		});
		bb40xoom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addr = "98:4B:4A:C9:DB:B7";
				// pair(addr, pin);
				bluetoothCtrl.pair(addr, pin);
				// handler.postDelayed(runnable, 5000);
			}
		});
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// endPir(addr);
				bluetoothCtrl.endPir();
			}
		});
		Button lianjieButton = (Button) findViewById(R.id.lianjie);
		lianjieButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChatModule chatModule = new ChatModule();
				chatModule.setChatState(BluetoothChat.MESSAGE_TOSTRING);
				chatModule.setChatString(addr);
				try {
					// bluetoothChat.sendMessage(ObjectUtil
					// .getBytesFromObject(chatModule));
					bluetoothCtrl.getBluetoothChat().sendMessage(
							ObjectUtil.getBytesFromObject(chatModule));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Button lianjieFileButton = (Button) findViewById(R.id.lianjieFile);
		lianjieFileButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String SDPATH = Environment.getExternalStorageDirectory() + "/";
				try {
					ChatFileModule chatModule = ObjectUtil.startFileMap(SDPATH
							+ "2012525109383746.apk", MainActivity.this);
					// bluetoothChat.gtoFile(chatModule);
					bluetoothCtrl.getBluetoothChat().gtoFile(chatModule);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		firstBar = (ProgressBar) findViewById(R.id.firstBar);
		zhuangtaiTextView = (TextView) findViewById(R.id.zhuangtai);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bluetoothCtrl != null) {
			bluetoothCtrl.getBluetoothChat().getmChatService().stop();
		}
	}

	// /**
	// * ����߳�
	// */
	// Runnable runnable = new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// runpair(addr, pin);
	// }
	// };
	// /**
	// * �Ƴ���Խ���
	// */
	// Runnable runnable2 = new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// endPir(addr);
	// }
	// };
	//
	// /**
	// * ��Խ��̷���
	// *
	// * @param strAddr
	// * @param strPsw
	// */
	// public void runpair(String strAddr, String strPsw) {
	// // boolean result = false;
	// BluetoothAdapter bluetoothAdapter = BluetoothAdapter
	// .getDefaultAdapter();
	// bluetoothAdapter.cancelDiscovery();
	// BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
	// if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	// // ���δƥ�䣬�����ִ��
	// pair(strAddr, strPsw);
	// handler.postDelayed(runnable, 5000);
	// } else {
	// // ��ƥ�䣬�Ƴ��߳�
	// handler.removeCallbacks(runnable);
	// // �Ƴ��̺߳���������
	// bluetoothChat.goConnect(device);
	// }
	//
	// }
	//
	// // �������
	// public boolean pair(String strAddr, String strPsw) {
	// boolean result = false;
	// BluetoothAdapter bluetoothAdapter = BluetoothAdapter
	// .getDefaultAdapter();
	// // ������ַ--> bluetoothAdapter.getAddress()
	// bluetoothAdapter.cancelDiscovery();
	// if (!bluetoothAdapter.isEnabled()) {
	// // δ��������
	// bluetoothAdapter.enable();
	//
	// } else {
	// // �ѿ�������
	// }
	// if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // ���������ַ�Ƿ���Ч
	// Log.d("mylog", "devAdd un effient!");
	// }
	// // Զ���豸��ַ--> strAddr
	// BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
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
	// // Log.d("mylog", "HAS BOND_BONDED");
	// // try {
	// // ClsUtils.createBond(device.getClass(), device);
	// // ClsUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
	// // ClsUtils.createBond(device.getClass(), device);
	// // remoteDevice = device; // ����󶨳ɹ�����ֱ�Ӱ�����豸���󴫸�ȫ�ֵ�remoteDevice
	// // result = true;
	// // } catch (Exception e) {
	// // // TODO Auto-generated catch block
	// // Log.d("mylog", "setPiN failed!");
	// // e.printStackTrace();
	// // }
	// }
	// return result;
	// }
	//
	// /**
	// * �Ƴ���Խ���
	// *
	// * @param strAddr
	// */
	// public void endPir(String strAddr) {
	// BluetoothAdapter bluetoothAdapter = BluetoothAdapter
	// .getDefaultAdapter();
	// bluetoothAdapter.cancelDiscovery();
	// BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
	// try {
	// if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
	// // ��ƥ�������Ƴ�
	// ClsUtils.removeBond(device.getClass(), device);
	// handler.postDelayed(runnable2, 5000);
	// } else {
	// // �Ѿ��Ƴ�
	// handler.removeCallbacks(runnable2);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
}
