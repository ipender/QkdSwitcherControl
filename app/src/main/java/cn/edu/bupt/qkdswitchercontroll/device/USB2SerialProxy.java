package cn.edu.bupt.qkdswitchercontroll.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Pandeng on 16-12-18.
 */
public class USB2SerialProxy {

    private static final String TAG = "USB2SerialProxy";
    private Context mContext;
    D2xxManager mFtd2xxMgr;

    public enum PORT {
        A, B, C, D
    }

    public enum BaudRate {
        bps_9600(9600),
        bps_115200(115200);
        private final int baudRate;

        private BaudRate(int baudRate) {
            this.baudRate = baudRate;
        }

        public int value() {
            return this.baudRate;
        }
    }

    public enum DataBits {
        bits_7((byte) 7),
        bits_8((byte) 8);

        private final byte dataBits;

        private DataBits(byte bits) {
            this.dataBits = bits;
        }

        public byte value() {
            return this.dataBits;
        }
    }

    public enum StopBits {
        bits_1((byte) 0),
        bits_2((byte) 1);

        private final byte stopBits;

        private StopBits(byte bits) {
            this.stopBits = bits;
        }

        public byte value() {
            return this.stopBits;
        }
    }

    public enum Parity {
        NONE((byte) 0),
        ODD((byte) 1),
        EVEN((byte) 2),
        MARK((byte) 3),
        SPACE((byte) 4);

        private final byte parity;

        private Parity(byte parity) {
            this.parity = parity;
        }

        public byte value() {
            return this.parity;
        }
    }

    public enum FlowControl {
        NONE((short) 0),
        RTS_CTS((short) 256),
        DTR_DSR((short) 512),
        XON_XOFF((short) 1024);

        private final short flowControl;

        private FlowControl(short flowControl) {
            this.flowControl = flowControl;
        }

        public short value() {
            return this.flowControl;
        }
    }

    public interface FtdiUartRead {
        void readWhenUartCallback(byte[] readData, int length);
    }

    final private HashMap<PORT, FtdiUartRead> mUartReadMap = new HashMap<PORT, FtdiUartRead>(4);

    final private HashMap<PORT, FT_Device> mDevices = new HashMap<PORT, FT_Device>(4);

    public USB2SerialProxy(Context context) {
        mContext = context;
        try {
            mFtd2xxMgr = D2xxManager.getInstance(mContext);
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }

        if(!mFtd2xxMgr.setVIDPID(0x0403, 0xada1))
            Log.i("ftd2xx-java","setVIDPID Error");

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);
        context.registerReceiver(mUsbReceiver, filter);

        int devCount = mFtd2xxMgr.createDeviceInfoList(mContext);
        if (devCount <= 0) {
            Log.e(TAG, "There is not Serial Port!!");
            return;
        }
        mDevices.put(PORT.A, mFtd2xxMgr.openByIndex(mContext, 0));
        mDevices.put(PORT.B, mFtd2xxMgr.openByIndex(mContext, 1));
        mDevices.put(PORT.C, mFtd2xxMgr.openByIndex(mContext, 2));
        mDevices.put(PORT.D, mFtd2xxMgr.openByIndex(mContext, 3));
        configSerialParam(PORT.A, BaudRate.bps_115200, DataBits.bits_8,
                StopBits.bits_1, Parity.NONE, FlowControl.NONE);
        configSerialParam(PORT.B, BaudRate.bps_115200, DataBits.bits_8,
                StopBits.bits_1, Parity.NONE, FlowControl.NONE);
        configSerialParam(PORT.C, BaudRate.bps_115200, DataBits.bits_8,
                StopBits.bits_1, Parity.NONE, FlowControl.NONE);
        configSerialParam(PORT.D, BaudRate.bps_115200, DataBits.bits_8,
                StopBits.bits_1, Parity.NONE, FlowControl.NONE);
        new UartReadThread(mDevices, readDataHandler).start();

    }

    public boolean configSerialParam(PORT port, BaudRate baudRate, DataBits dataBits,
                                     StopBits stopBits, Parity parity, FlowControl flowControl) {
        FT_Device ftDev = mDevices.get(port);
        if (ftDev.isOpen() == false) {
            Log.e(TAG, "SetConfig: ftDev not open!!!!!!  index:" + port.ordinal());
            return false;
        }
        // configure our port
        // reset to UART mode for 232 devices
        ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

        ftDev.setBaudRate(baudRate.value());
        ftDev.setDataCharacteristics(dataBits.value(), stopBits.value(), parity.value());
        ftDev.setFlowControl(flowControl.value(), (byte) 0, (byte) 0);
        ftDev.setLatencyTimer((byte) 16);
        return true;
    }

    public int sendMessage(PORT port, byte[] data) {
        FT_Device ftDev = mDevices.get(port);
        int msgLen = ftDev.write(data, data.length);
        Log.d(TAG, "Port." + port.name() + " Send " + msgLen + " bytes message!");
        return msgLen;
    }

    public void registerForUartRead(PORT port, FtdiUartRead uartRead) {
        mUartReadMap.put(port, uartRead);
    }

    public int available(PORT port) {
        return mDevices.get(port).getQueueStatus();
    }

    public int read(PORT port, byte[] readData, int length) {
        return mDevices.get(port).read(readData, length);
    }

    private static final String DATA_KEY = "UART";
    final Handler readDataHandler = new Handler(){
        FT_Device ftDev;
        Bundle dataBundle;
        byte[] dataBytes;
        FtdiUartRead mFtdiUartRead;
        PORT mPort;
        @Override
        public void dispatchMessage(Message msg) {
            mPort = (PORT)msg.obj;
            if (mPort == null) return;
            ftDev = mDevices.get(mPort);
            if (ftDev == null) return;
            dataBundle = msg.getData();
            dataBytes = dataBundle.getByteArray(DATA_KEY);
            mFtdiUartRead = mUartReadMap.get(mPort);
            if (mFtdiUartRead == null) {
                Log.d(TAG, "Port." + mPort.name() + " received data, but don't have a receiver!");
                return;
            }
            mFtdiUartRead.readWhenUartCallback(dataBytes, msg.arg1);
        }

    };

    private class UartReadThread extends Thread {
        final HashMap<PORT, FT_Device> device;
        FT_Device ftDev;
        int available = 0;
        byte[] bytesBuf = new byte[1024];
        Bundle dataBundle;
        final Handler readDataHdler;
        public UartReadThread(HashMap<PORT, FT_Device> device, Handler readHandler) {
            this.device = device;
            readDataHdler = readHandler;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (PORT port:PORT.values()) {
                    ftDev = device.get(port);
                    available = ftDev.getQueueStatus();
                    if (available > 0) {
                        bytesBuf = new byte[available];
                        ftDev.read(bytesBuf, available);
                        dataBundle = new Bundle();
                        dataBundle.putByteArray(DATA_KEY, Arrays.copyOf(bytesBuf, available));
                        Message msg = new Message();
                        msg.obj = port;
                        msg.setData(dataBundle);
                        msg.arg1 = available;
                        readDataHdler.sendMessage(msg);
                    }
                }

            }
        }
    }

    /***********USB broadcast receiver*******************************************/
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String TAG = "FragL";
            String action = intent.getAction();
            if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                Log.i(TAG,"DETACHED...");
            }
        }
    };
}
