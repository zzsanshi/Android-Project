package com.hohai.filetransfer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.hohai.filetransfer.ServerLink.Client;

public class MainActivity extends BaseActivity {

    private static final int CODE_REQ_PERMISSIONS = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requireSomePermission();
    }


    private void requireSomePermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQ_PERMISSIONS);
    }
//
    public void startFileSenderActivity(View view) {

        startActivity(new Intent(this, SendFileActivity.class));

        //连接服务器进行注册
        //获得注册信息
    }

    public void startFileReceiverActivity(View view) {

        startActivity(new Intent(this, ReceiveFileActivity.class));
        //注册信息进行,并表示愿意接收文件,若在统一局域网中则直连
    }
public void receiveLoginList() throws Exception {
    Client c1=new Client();
    c1.LoginList();


}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQ_PERMISSIONS) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showToast("缺少权限，请先授予权限");
                    showToast(permissions[i]);
                    return;
                }
            }
            showToast("已获得权限");
        }
    }

    public void checkPermission(View view) {
        try {
            Client c1=new Client();
            c1.login();//发送约定,进行注册
            c1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//进行注册
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQ_PERMISSIONS);
    }

    public static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "可用的";
            case WifiP2pDevice.INVITED:
                return "邀请中";
            case WifiP2pDevice.CONNECTED:
                return "已连接";
            case WifiP2pDevice.FAILED:
                return "失败的";
            case WifiP2pDevice.UNAVAILABLE:
                return "不可用的";
            default:
                return "未知";
        }
    }

}