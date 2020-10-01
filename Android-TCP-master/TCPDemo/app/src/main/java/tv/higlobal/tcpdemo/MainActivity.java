package tv.higlobal.tcpdemo;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public EditText editText;
    public TextView textView_send;
    public TextView textView_receive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.send_editText);
        textView_send = findViewById(R.id.send_textView);
        textView_receive = findViewById(R.id.receive_textView);
        textView_send.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_receive.setMovementMethod(ScrollingMovementMethod.getInstance());

        editText.setText(getWifiIp().toString()  + "\n");
        //单例
        TaskCenter.sharedCenter().setDisconnectedCallback(new TaskCenter.OnServerDisconnectedCallbackBlock() {
            @Override
            public void callback(IOException e) {
                textView_receive.setText(textView_receive.getText().toString() + "断开连接" + "\n");
            }
        });
        TaskCenter.sharedCenter().setConnectedCallback(new TaskCenter.OnServerConnectedCallbackBlock() {
            @Override
            public void callback() {
                textView_receive.setText(textView_receive.getText().toString() + "连接成功" + "\n");
            }
        });
        TaskCenter.sharedCenter().setReceivedCallback(new TaskCenter.OnReceiveCallbackBlock() {
            @Override
            public void callback(String receicedMessage) {
                textView_receive.setText(textView_receive.getText().toString() + receicedMessage + "\n");
            }
        });
    }

    public void sendMessage(View view) {
        String msg = editText.getText().toString();
        textView_send.setText(textView_send.getText().toString() + msg + "\n");
        TaskCenter.sharedCenter().send(msg.getBytes());
    }

    public void connect(View view) {

        String ip=getWifiIp();
        TaskCenter.sharedCenter().connect(ip,54321);//端口号未知
    }

    public void disconnect(View view) {
        TaskCenter.sharedCenter().disconnect();
    }

    public void clear1(View view) {
        textView_send.setText("");
    }

    public void clear2(View view) {
        textView_receive.setText("");
    }

//获取wifi的地址
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
