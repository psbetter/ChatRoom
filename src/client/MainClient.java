package client;

import client.view.LoginFrame;

import javax.swing.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈客户端入口类〉
 *
 */

public class MainClient {

    public static void main(String[] args) {

        //设置外观感觉
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new LoginFrame();  //启动登录窗体
    }
}
