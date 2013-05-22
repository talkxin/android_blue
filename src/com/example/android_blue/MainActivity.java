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
	// // 蓝牙驱动
	// private static BluetoothDevice remoteDevice;
	// // 蓝牙通讯类
	// private static BluetoothChat bluetoothChat;
	public String addr;
	public String pin = "123456";
	/**
	 * 蓝牙连接控制
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
	// * 配对线程
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
	// * 移除配对进程
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
	// * 配对进程方法
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
	// // 如果未匹配，则继续执行
	// pair(strAddr, strPsw);
	// handler.postDelayed(runnable, 5000);
	// } else {
	// // 已匹配，移除线程
	// handler.removeCallbacks(runnable);
	// // 移除线程后立即连接
	// bluetoothChat.goConnect(device);
	// }
	//
	// }
	//
	// // 进行配对
	// public boolean pair(String strAddr, String strPsw) {
	// boolean result = false;
	// BluetoothAdapter bluetoothAdapter = BluetoothAdapter
	// .getDefaultAdapter();
	// // 蓝牙地址--> bluetoothAdapter.getAddress()
	// bluetoothAdapter.cancelDiscovery();
	// if (!bluetoothAdapter.isEnabled()) {
	// // 未开启蓝牙
	// bluetoothAdapter.enable();
	//
	// } else {
	// // 已开启蓝牙
	// }
	// if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // 检查蓝牙地址是否有效
	// Log.d("mylog", "devAdd un effient!");
	// }
	// // 远程设备地址--> strAddr
	// BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
	//
	// if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	// // 手机未匹配
	// try {
	// Log.d("mylog", "NOT BOND_BONDED");
	// ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
	// ClsUtils.createBond(device.getClass(), device);
	// remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
	// result = true;
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// Log.d("mylog", "setPiN failed!");
	// e.printStackTrace();
	// } //
	// handler.postDelayed(runnable, 5000);
	// } else {
	// // 手机已匹配则连接
	// bluetoothChat.goConnect(device);
	// // Log.d("mylog", "HAS BOND_BONDED");
	// // try {
	// // ClsUtils.createBond(device.getClass(), device);
	// // ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
	// // ClsUtils.createBond(device.getClass(), device);
	// // remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
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
	// * 移除配对进程
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
	// // 该匹配正在移除
	// ClsUtils.removeBond(device.getClass(), device);
	// handler.postDelayed(runnable2, 5000);
	// } else {
	// // 已经移除
	// handler.removeCallbacks(runnable2);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
}
