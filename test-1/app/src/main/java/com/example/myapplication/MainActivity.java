package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1=(Button)findViewById(R.id.button5);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                Toast.makeText(MainActivity.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });

        textView = findViewById(R.id.text);

        textView.setText(getWifiIp().toString()  + "\n");
    }

    public  String getWifiIp() {
        WifiManager wifimanage = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);//获取WifiManager

       //检查wifi是否开启
        if (!wifimanage.isWifiEnabled()) {

            wifimanage.setWifiEnabled(true);

        }

        WifiInfo wifiinfo = wifimanage.getConnectionInfo();
        //将获取的int转为真正的ip地址,参考的网上的，修改了下
        String ip = intToIp(wifiinfo.getIpAddress());
        return ip;

    }
    private String intToIp(int i)  {
        return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF)+ "." + ((i >> 16 ) & 0xFF) +"."+((i >> 24 ) & 0xFF );
    }

}