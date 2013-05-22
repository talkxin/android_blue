package com.example.android_blue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class ClsUtils {
	/**
	 * 
	 * ���豸��� �ο�Դ�룺platform/packages/apps/Settings.git
	 * 
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */

	static public boolean createBond(Class btClass, BluetoothDevice btDevice)

	throws Exception

	{
		try {
			System.out.println("�������");
			Method createBondMethod = btClass.getMethod("createBond");
			createBondMethod.invoke(btDevice);
			// ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	/**
	 * 
	 * ���豸������ �ο�Դ�룺platform/packages/apps/Settings.git
	 * 
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */

	static public boolean removeBond(Class btClass, BluetoothDevice btDevice)

	throws Exception

	{
		try {
			Method removeBondMethod = btClass.getMethod("removeBond");
			removeBondMethod.invoke(btDevice);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	// ����pin��
	static public boolean setPin(Class btClass, BluetoothDevice btDevice,
			String str) throws Exception {
		try {
			Method removeBondMethod = btClass.getDeclaredMethod("setPin",
					byte[].class);
			removeBondMethod.setAccessible(true);
			removeBondMethod.invoke(btDevice, str.getBytes("UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	// ȡ���û�����

	static public boolean cancelPairingUserInput(Class btClass,
			BluetoothDevice device) throws Exception {
		// System.out.println("ȡ���ù㲥");
		try {
			Method createBondMethod = btClass
					.getMethod("cancelPairingUserInput");
			createBondMethod.invoke(device);
			// cancelBondProcess()
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	// ȡ�����

	static public boolean cancelBondProcess(Class btClass,
			BluetoothDevice device) throws Exception {
		Method createBondMethod = btClass.getMethod("cancelBondProcess");
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	/**
	 * 
	 * ��ʾ��
	 * 
	 * @param clsShow
	 */

	static public void printAllInform(Class clsShow) {
		try {
			// ȡ�����з���
			Method[] hideMethod = clsShow.getMethods();
			int i = 0;
			for (; i < hideMethod.length; i++) {
				// System.out.println("������--��" + hideMethod[i].getName()
				// + ";and the i is:"
				//
				// + i);
			}
			// ȡ�����г���
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++) {
				if (allFields[i].getName().equals("mAddress"))
					System.out.println("����--��" + allFields[i].getName());
			}
		}

		catch (SecurityException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
