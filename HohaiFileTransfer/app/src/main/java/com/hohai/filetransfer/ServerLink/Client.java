package com.hohai.filetransfer.ServerLink;

import android.os.Build;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
public class Client extends Socket{
    private  static final  String host = "114.212.81.221";
    private static final int port = 8080;
    // 与服务端建立连接
    private Socket client;

    private FileInputStream fis;

    private DataOutputStream dos;

    /**
     * 构造函数<br/>
     * 与服务器建立连接
     * @throws Exception
     */

    public Client() throws Exception {
        super(host,port);
        this.client = this;
        //传输约定
        System.out.println("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");

    }
//注册
    public void login (){
        String devname= Build.MODEL;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            dos.write(1111);//注册
            dos.flush();
            dos.write(devname.getBytes());//转换为字节,发送设备信息
            dos.flush();
            dos.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }
////接收注册信息
    public void LoginList(){





    }
//注销注册
    public void  cancellation(){
        try {
            dos = new DataOutputStream(client.getOutputStream());
            dos.write(5555);//写出去
            dos.flush();
            dos.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务端传输文件
     * @throws Exception
     */
    public void sendFile() throws Exception {
        try {
            File file = new File("...");//文件
            if(file.exists()) {
                fis = new FileInputStream(file);//读取文件
                dos = new DataOutputStream(client.getOutputStream());

                // 文件名和长度
                dos.writeUTF(file.getName());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();

                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);//写出去
                    dos.flush();
                    progress += length;
                    System.out.print("| " + (100*progress/file.length()) + "% |");
                }
                System.out.println();
                System.out.println("======== 文件传输成功 ========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
                fis.close();
            if(dos != null)
                dos.close();
            client.close();
        }
    }


}
