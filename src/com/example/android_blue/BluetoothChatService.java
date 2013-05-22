package com.example.android_blue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

import android.R.integer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ��������Chat�����ķ�����
 * 
 * @author talkliu
 * 
 */
public class BluetoothChatService {
	// Debugging
	private static final String TAG = "BluetoothChatService";
	// ��װ������
	public static String MESSAGE_NAME = "message";
	// ��װ���ļ���
	public static String MESSAGE_OBJECT = "obj";
	// Name for the SDP record when creating server socket
	private static final String NAME = "BluetoothChat";

	// Unique UUID for this application
	private static final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	// Member fields
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;

	// Constants that indicate the current connection state
	// δ֪������
	public static final int STATE_UNABLE = -1;
	// ʧȥ����
	public static final int STATE_LOST = -2;
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming
												// connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing
													// connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote
													// device

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 * 
	 * @param context
	 *            The UI Activity Context
	 * @param handler
	 *            A Handler to send messages back to the UI Activity
	 */
	public BluetoothChatService(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		mHandler = handler;
	}

	/**
	 * Set the current state of the chat connection
	 * 
	 * @param state
	 *            An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		mState = state;

		// Give the new state to the Handler so the UI Activity can update
		mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1)
				.sendToTarget();
	}

	/**
	 * Return the current connection state.
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
	 */
	public synchronized void start() {

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to listen on a BluetoothServerSocket
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
		setState(STATE_LISTEN);
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * 
	 * @param device
	 *            The BluetoothDevice to connect
	 */
	public synchronized void connect(BluetoothDevice device) {

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 * 
	 * @param socket
	 *            The BluetoothSocket on which the connection was made
	 * @param device
	 *            The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Cancel the accept thread because we only want to connect to one
		// device
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// Send the name of the connected device back to the UI Activity
		Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothChat.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		setState(STATE_CONNECTED);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * 
	 * @param out
	 *            The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		setState(STATE_LISTEN);

		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putInt(BluetoothChat.TOAST, STATE_UNABLE);
		// bundle.putString(BluetoothChat.TOAST, "Unable to connect device");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		setState(STATE_LISTEN);

		// ɥʧ����ʱ����Ϣ
		Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putInt(BluetoothChat.TOAST, STATE_LOST);
		// bundle.putString(BluetoothChat.TOAST, "Device connection was lost");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * This thread runs while listening for incoming connections. It behaves
	 * like a server-side client. It runs until a connection is accepted (or
	 * until cancelled).
	 */
	private class AcceptThread extends Thread {
		// The local server socket
		private final BluetoothServerSocket mmServerSocket;
		private int connect = 0;

		public AcceptThread() {
			BluetoothServerSocket tmp = null;

			// Create a new listening server socket
			try {
				tmp = mAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "listen() failed", e);
			}
			mmServerSocket = tmp;
		}

		public void run() {
			setName("AcceptThread");
			BluetoothSocket socket = null;

			// Listen to the server socket if we're not connected
			while (mState != STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.e(TAG, "accept() failed", e);
					break;
					// �ڰ��Сʱ�ڳ�������
					// if (connect == BluetoothCtrl.LONG_TIME_CONNECTED) {
					// break;
					// } else {
					// try {
					// Thread.sleep(5000);
					// connect++;
					// continue;
					// } catch (InterruptedException e1) {
					// // TODO Auto-generated catch block
					// e1.printStackTrace();
					// }
					// }
				} catch (java.lang.NullPointerException e) {
					// TODO: handle exception
					mAdapter.cancelDiscovery();
					if (!mAdapter.isEnabled()) {
						mAdapter.enable();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						break;
					}
				}

				// If a connection was accepted
				if (socket != null) {
					synchronized (BluetoothChatService.this) {
						switch (mState) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							// Either not ready or already connected. Terminate
							// new socket.
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
		}

		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of server failed", e);
			}
		}
	}

	/**
	 * This thread runs while attempting to make an outgoing connection with a
	 * device. It runs straight through; the connection either succeeds or
	 * fails.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		int connect = 0;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery();

			// Make a connection to the BluetoothSocket
			while (true) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					mmSocket.connect();
					break;
				} catch (IOException e) {
					if (connect == BluetoothCtrl.LONG_TIME_CONNECTED) {
						connectionFailed();
						// Close the socket
						try {
							mmSocket.close();
						} catch (IOException e2) {
							Log.e(TAG,
									"unable to close() socket during connection failure",
									e2);
						}
						// Start the service over to restart listening mode
						BluetoothChatService.this.start();
						return;
					} else {
						connect++;
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						continue;

					}
				}
			}

			// Reset the ConnectThread because we're done
			synchronized (BluetoothChatService.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * This thread runs during a connection with a remote device. It handles all
	 * incoming and outgoing transmissions.
	 */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			// �����С
			byte[] buffer = new byte[1024 * 1024];
			int bytes;

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// ��ȡ���ַ���
					bytes = mmInStream.read(buffer);
					ChatModule module = null;
					int out = -1;
					try {
						module = (ChatModule) ObjectUtil
								.getBytesToObject((byte[]) buffer);
						out = module.getChatState();
					} catch (Exception e) {
						// TODO: handle exception
					}
					// �ش���activity
					mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, bytes,
							out, module).sendToTarget();
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();
					break;
				}
			}
		}

		/**
		 * Write to the connected OutStream.
		 * 
		 * @param buffer
		 *            The bytes to write
		 */
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);

				// �����豸���͵����ݷ���Message ��ֹ
				// mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1,
				// buffer).sendToTarget();
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}
