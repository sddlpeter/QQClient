package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread{
    private Socket socket;

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    private boolean loop = true;

    // 构造器接收一个Socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 因为线程需要在后台和服务器通讯，因此我们用while循环来控制
        while (loop) {
            try {
                // System.out.println("客户端线程，等待读取从服务端发送的消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                // 如果服务端没有消息发过来，此线程会阻塞在这里 (ois.readObject)
                Message message = (Message) ois.readObject();

                // 判断message类型，做业务处理
                // 如果读取到的是 服务端返回的在线用户列表
                if (message.getMsgType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("==============当前在线用户列表================");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("\t\t 用户 " + onlineUsers[i]);
                    }
                } else if (message.getMsgType().equals(MessageType.MESSAGE_COMM_MES)) {
                    System.out.println("用户 " + message.getSender() + " 对用户 " + message.getGetter() + " 说：" + message.getContent());
                } else if (message.getMsgType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    System.out.println("用户 " + message.getSender() + " 对大家说 " + message.getContent());
                } else {
                    System.out.println("是其他类型的message，暂时不处理...");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }

}
