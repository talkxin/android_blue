package com.example.android_blue;

import java.lang.reflect.Field;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothConnectActivityReceiver extends BroadcastReceiver {

	String strPsw = "0";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(
				"android.bluetooth.device.action.PAIRING_REQUEST")) {
			//  ��Ϊ����ȷ�Ϲ㲥 
			BluetoothDevice btDevice = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			// byte[] pinBytes = BluetoothDevice.convertPinToBytes("1234");
			// device.setPin(pinBytes);
			try {
				// ClsUtils.setPin(btDevice.getClass(), btDevice, strPsw); //
				// �ֻ��������ɼ������
				Field field = btDevice.getClass().getDeclaredField(
						"PAIRING_VARIANT_PIN");
				field.setAccessible(true);
//				String mAddress = String.valueOf(field.get(btDevice));
				ClsUtils.setPin(btDevice.getClass(), btDevice, "0000");
				ClsUtils.createBond(btDevice.getClass(), btDevice);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

	}
}