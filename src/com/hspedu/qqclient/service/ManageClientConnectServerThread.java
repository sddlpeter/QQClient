package com.hspedu.qqclient.service;


import java.util.HashMap;

// 管理客户端连接到服务器端的线程的类
public class ManageClientConnectServerThread {
    // key是用户的id，val是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }

    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }

    public static void removeClientConnectServerThread(String userId) {
        hm.remove(userId);
    }
}
