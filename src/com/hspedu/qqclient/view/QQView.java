package com.hspedu.qqclient.view;

import com.hspedu.qqclient.service.ManageClientConnectServerThread;
import com.hspedu.qqclient.service.MessageClientService;
import com.hspedu.qqclient.service.UserClientService;
import com.hspedu.qqclient.utils.Utility;

import javax.swing.*;

public class QQView {
    private boolean loop = true; // 控制是否显示菜单
    private String key = "";  // 接收用户的键盘输入
    private UserClientService userClientService = new UserClientService(); // 用于登录服务器，注册用户
    private MessageClientService messageClientService = new MessageClientService();

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统");
        System.exit(0);
    }

    private void mainMenu() {
        while (loop) {
            System.out.println("==============欢迎登录网络通信系统============");
            System.out.println("\t\t 1. 登录系统");
            System.out.println("\t\t 9. 退出系统");
            System.out.println("请输入你的选择：");

            key = Utility.readString(1);

            switch (key) {
                case "1":
                    System.out.println("请输入用户号：");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密码：");
                    String pwd = Utility.readString(50);
                    // 需要到服务端验证用户名和密码
                    // UserClientService
                    if (userClientService.checkUser(userId, pwd)) {
                        System.out.println("==============欢迎 (用户 " + userId + " 登录成功)============");
                        while (loop) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("\n==============网络通讯系统二级菜单(用户 " + userId + " )===============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择： ");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    userClientService.onlineFriendList();
                                    //System.out.println("显示在线用户列表");
                                    break;
                                case "2":
                                    System.out.println("群发消息");
                                    System.out.println("请输入想说的话：");
                                    String contentToGroup = Utility.readString(100);
                                    messageClientService.sendMessageToAll(contentToGroup, userId);
                                    break;
                                case "3":
                                    System.out.println("私聊消息");
                                    System.out.println("请输入想聊天的用户号(在线):");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    String content = Utility.readString(100);
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    // userClientService.sendMessage(getterId, content);
                                    break;
                                case "4":
                                    System.out.println("发送文件");
                                    break;
                                case "9":
                                    loop = false;
                                    userClientService.logOut();
                                    break;
                            }
                        }

                    } else {
                        System.out.println("===============登录失败===============");
                    }
                    break;
                case "9":
                    loop = false;
                    System.out.println("");
                    break;
            }
        }
    }
}
