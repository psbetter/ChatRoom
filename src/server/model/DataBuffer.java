package server.model;

import common.model.entity.User;
import server.model.entity.OnlineUserTableModel;
import server.model.entity.RegistedUserTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈服务器端数据缓存〉
 *
 */

public class DataBuffer {
    // 服务器端套接字
    public static ServerSocket serverSocket;
    //在线用户的IO Map
    public static Map<Long, OnlineClientIOCache> onlineUserIOCacheMap;
    //在线用户Map
    public static Map<Long, User> onlineUsersMap;
    //装载每个用户的好友列表map的对象数组
    public static ArrayList<List<User>> matesList;
    //服务器配置参数属性集
    public static Properties configProp;
    // 已注册用户表的Model
    public static RegistedUserTableModel registedUserTableModel;
    // 当前在线用户表的Model
    public static OnlineUserTableModel onlineUserTableModel;
    // 当前服务器所在系统的屏幕尺寸
    public static Dimension screenSize;
    // 在线总人数
    public static JLabel lblUserCount;
    // 当前在线人数
    public static JTextField txtNumber;
    // 用户消息区域
    public static TextArea tareMessage;
    // 服务日志区域
    public static TextArea serverLog;
    // 服务器ip地址
    public static String ip;

    static{
        // 初始化
        onlineUserIOCacheMap = new ConcurrentSkipListMap<Long,OnlineClientIOCache>();
        onlineUsersMap = new ConcurrentSkipListMap<Long, User>();
        configProp = new Properties();
        registedUserTableModel = new RegistedUserTableModel();
        onlineUserTableModel = new OnlineUserTableModel();
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        matesList = new ArrayList<List<User>>(100);
        lblUserCount = new JLabel("在线总人数 0 人");
        txtNumber = new JTextField("0 人", 10);
        tareMessage = new TextArea(20, 20);
        serverLog = new TextArea(20, 50);

        // 加载服务器配置文件
        try {
            configProp.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
