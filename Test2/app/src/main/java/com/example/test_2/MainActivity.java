package com.example.test_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public TextView textView;
    Button button1;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<String> mDevicesList=new HashSet<String>();//设备名
    StringBuffer tvDevices=new StringBuffer();//距离

    Handler mBLHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    scanBluth();

                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        button1 = (Button) findViewById(R.id.button1);
       //设置监听
         button1.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v) {
                 if(tvDevices.length()==0){
                     textView.setText("没有设备");
                 }
                 else
                     textView.setText(tvDevices);
                 Log.e(TAG,"1");
             }


         });

        // 判断手机是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 判断是否打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            // 弹出对话框提示用户是后打开
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
            // 不做提示，强行打开
            // mBluetoothAdapter.enable();
        }else {
            // 不做提示，强行打开
            mBluetoothAdapter.enable();
        }
        // 获取已经配对的设备
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                .getBondedDevices();

        // 判断是否有配对过的设备
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // 遍历
                mDevicesList.add(device.getAddress());
                tvDevices.append(device.getName() + ":" + device.getAddress() + "\n");
            }
        }

        // 找到设备的广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
// 注册广播
        registerReceiver(receiver, filter);
// 搜索完成的广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
// 注册广播
        registerReceiver(receiver, filter);

    }
    //搜索蓝牙
    private void scanBluth() {
// 设置进度条
        setProgressBarIndeterminateVisibility(true);
        setTitle("正在搜索...");
// 判断是否在搜索,如果在搜索，就取消搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
// 开始搜索
        mBluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到的广播类型
            String action = intent.getAction();
            // 发现设备的广播
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 从intent中获取设备
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String aa = String.format("%s", tvDevices.toString());
                if (aa.contains(device.getAddress())) {
                    return;
                } else {
                    // 判断是否配对过
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        // 添加到列表
                        short rssi = intent.getExtras().getShort(
                                BluetoothDevice.EXTRA_RSSI);
                        int iRssi = abs(rssi);
                        // 将蓝牙信号强度换算为距离
                        double power = (iRssi - 59) / 25.0;
                        String mm = new Formatter().format("%.2f", pow(10, power)).toString();
                        tvDevices.append(device.getName() + ":"
                                + device.getAddress() + " ：" + mm + "m" + "\n");
                    }else {

                    }
                }
                // 搜索完成
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                // 关闭进度条
                setProgressBarIndeterminateVisibility(true);
                setTitle("搜索完成！");
                mBLHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

}