package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

// 完成用户登录和注册
public class UserClientService {
    private User u = new User();
    private Socket socket;



    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        u.setUserId(userId);
        u.setPassword(pwd);

        try {
            // 链接到服务端，发送u对象
            socket = new Socket(InetAddress.getByName("10.0.0.218"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u); // 发送user对象



            // 读取从服务端回复的message
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();



            if (ms.getMsgType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)) { //登录成功

                // 创建一个和服务器端保持通讯的线程 -> 创建一个类 ClientConnectServerThread
                ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
                // 启动客户端线程
                ccst.start();
                // 为了后面客户端的扩展，将线程放到集合中管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId, ccst);
                b = true;

            } else {
                // 如果登录失败，就不能启动和服务器通信的线程，关闭socket
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }


    // 向服务器端请求在线用户列表
    public void onlineFriendList() {
        // 发送一个Message, 类型 MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());

        //发送给服务器
        //拿到当前线程的socket 对应的 ObjectOutputStream对象
        try {
            // 从管理线程集合中，通过UserId，得到这个线程
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId());
            // 拿到线程持有的socket
            Socket socket =  clientConnectServerThread.getSocket();
            // 拿到socket对应的ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);  // 发送一个message，向服务端要在线用户列表
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());

        //发送给服务器
        //拿到当前线程的socket 对应的 ObjectOutputStream对象
        try {
            // 从管理线程集合中，通过UserId，得到这个线程
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId());
            // 拿到线程持有的socket
            Socket socket =  clientConnectServerThread.getSocket();
            // 拿到socket对应的ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);  // 发送一个message，通知服务端当前客户端退出
            System.out.println(u.getUserId() + " 退出系统");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String userId, String content) {
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_COMM_MES);
        message.setSender(u.getUserId());
        message.setGetter(userId);
        message.setContent(content);

        //发送给服务器
        //拿到当前线程的socket 对应的 ObjectOutputStream对象
        try {
            // 从管理线程集合中，通过UserId，得到这个线程
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId());
            // 拿到线程持有的socket
            Socket socket =  clientConnectServerThread.getSocket();
            // 拿到socket对应的ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);  // 发送一个message，通知服务端当前客户端退出
            System.out.println(u.getUserId() + " 发消息给 " + userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
