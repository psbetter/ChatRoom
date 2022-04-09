package client.view;

import client.model.DataBuffer;
import client.model.entity.MatesListModel;
import client.util.ClientUtil;
import common.model.entity.Request;
import common.model.entity.Response;
import common.model.entity.ResponseStatus;
import common.model.entity.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static client.model.DataBuffer.matesList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 */

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = -3426717670093483287L;
    private final int WindowWidth = (int)(DataBuffer.screenSize.width * 0.3);
    private final int WindowHeight = (int)(DataBuffer.screenSize.height * 0.3);

    private JPanel pnlLogin;
    private JButton btnLogin, btnRegister, btnExit;
    private JLabel lblServer, lblUserName, lblPassword, lblLogo;
    private JTextField txtUserName, txtServer;
    private JPasswordField pwdPassword;
    private String strServerIp;

    // 用于将窗口定位
    private Dimension scrnsize;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public LoginFrame(){
        this.init();
        setVisible(true);
    }

    public String getStrServerIp(){
        return this.strServerIp;
    }

    public void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("用户登录");
        this.setSize(this.WindowWidth, this.WindowHeight);
        //设置默认窗体在屏幕中央
        int x = (int)toolkit.getScreenSize().getWidth();
        int y = (int)toolkit.getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);
        this.setResizable(false);

        pnlLogin = new JPanel();
        this.getContentPane().add(pnlLogin);

        lblServer = new JLabel("服务器IP:");
        lblUserName = new JLabel("用户ID:");
        lblPassword = new JLabel("密  码:");
        JLabel tiptxt = new JLabel("用户ID必须为数字");
        txtServer = new JTextField(20);
        txtServer.setText("127.0.0.1");
        txtUserName = new JTextField(20);
        pwdPassword = new JPasswordField(20);
        btnLogin = new JButton("登录");
        btnLogin.setToolTipText("登录到服务器");
        btnLogin.setMnemonic('L');
        btnRegister = new JButton("注册");
        btnRegister.setToolTipText("注册新用户");
        btnRegister.setMnemonic('R');
        btnExit = new JButton("退出");
        btnExit.setToolTipText("退出系统");
        btnExit.setMnemonic('X');
        /***********************************************************************
         * 该布局采用手动布局 setBounds设置组件位置 * setFont设置字体、字型、字号 * setForeground设置文字的颜色 *
         * setBackground设置背景色 * setOpaque将背景设置为透明
         */
        pnlLogin.setLayout(null); // 组件用手动布局
        pnlLogin.setBackground(new Color(52, 130, 203));

        lblServer.setBounds(50, 75, 80, 25);
        txtServer.setBounds(130, 75, 160, 25);
        lblUserName.setBounds(50, 115, 80, 25);
        txtUserName.setBounds(130, 115, 160, 25);
        tiptxt.setBounds(130, 145, 160, 15);
        lblPassword.setBounds(50, 165, 80, 25);
        pwdPassword.setBounds(130, 165, 160, 25);
        btnLogin.setBounds(50, 205, 80, 25);
        btnRegister.setBounds(130, 205, 80, 25);
        btnExit.setBounds(210, 205, 80, 25);

        Font fontstr = new Font("宋体", Font.PLAIN, 12);
        lblServer.setFont(fontstr);
        txtServer.setFont(fontstr);
        lblUserName.setFont(fontstr);
        txtUserName.setFont(fontstr);
        tiptxt.setFont(new Font("宋体", Font.PLAIN, 8));
        lblPassword.setFont(fontstr);
        pwdPassword.setFont(fontstr);
        btnLogin.setFont(fontstr);
        btnRegister.setFont(fontstr);
        btnExit.setFont(fontstr);

        lblUserName.setForeground(Color.BLACK);
        lblPassword.setForeground(Color.BLACK);
        btnLogin.setBackground(Color.ORANGE);
        btnRegister.setBackground(Color.ORANGE);
        btnExit.setBackground(Color.ORANGE);
        tiptxt.setForeground(Color.RED);

        pnlLogin.add(lblServer);
        pnlLogin.add(txtServer);
        pnlLogin.add(lblUserName);
        pnlLogin.add(txtUserName);
        pnlLogin.add(lblPassword);
        pnlLogin.add(pwdPassword);
        pnlLogin.add(btnLogin);
        pnlLogin.add(btnRegister);
        pnlLogin.add(btnExit);
        pnlLogin.add(tiptxt);

        // 设置背景图片
        Icon logo1 = new ImageIcon("images\\loginlogo.jpg");
        lblLogo = new JLabel(logo1);
        lblLogo.setBounds(0, 0, 340, 66);
        pnlLogin.add(lblLogo);
        // 设置登录窗口
        setResizable(false);
        setSize(340, 280);
        setVisible(true);
        scrnsize = DataBuffer.screenSize;
        setLocation(scrnsize.width / 2 - this.getWidth() / 2, scrnsize.height
                / 2 - this.getHeight() / 2);
        Image img = toolkit.getImage("images\\appico.jpg");
        setIconImage(img);

        // 关闭窗口按钮监听
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                Request req = new Request();
                req.setAction("exit");
                try {
                    ClientUtil.sendTextRequest(req);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }finally{
                    System.exit(0);
                }
            }
        });

        //注册
        btnRegister.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
                try {
                    DataBuffer.clientSeocket = new Socket(txtServer.getText(), port);
                    DataBuffer.oos = new ObjectOutputStream(DataBuffer.clientSeocket.getOutputStream());
                    DataBuffer.ois = new ObjectInputStream(DataBuffer.clientSeocket.getInputStream());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "注册前先填写需连接的IP地址,请检查IP!","服务器未连上", JOptionPane.ERROR_MESSAGE);//否则连接失败
//                    System.exit(0);
                    return;
                }
                new RegisterFrame();  //打开注册窗体
            }
        });

        //"登录"
        btnLogin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    /** 登录 */
    @SuppressWarnings("unchecked")
    private void login() {
        this.strServerIp = txtServer.getText();
        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
        try {
            DataBuffer.clientSeocket = new Socket(this.strServerIp, port);
            DataBuffer.oos = new ObjectOutputStream(DataBuffer.clientSeocket.getOutputStream());
            DataBuffer.ois = new ObjectInputStream(DataBuffer.clientSeocket.getInputStream());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "连接服务器失败,请检查ip!","服务器未连上", JOptionPane.ERROR_MESSAGE);//否则连接失败
//            System.exit(0);
            return;
        }

        if (txtUserName.getText().length() == 0
                || pwdPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "账号和密码是必填的",
                    "输入有误",JOptionPane.ERROR_MESSAGE);
            txtUserName.requestFocusInWindow();
            return;
        }

        if(!txtUserName.getText().matches("^\\d*$")){
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "账号必须是数字",
                    "输入有误",JOptionPane.ERROR_MESSAGE);
            txtUserName.requestFocusInWindow();
            return;
        }

        Request req = new Request();
        req.setAction("userLogin");
        req.setAttribute("id", txtUserName.getText());
        req.setAttribute("password", new String(pwdPassword.getPassword()));
        //获取响应
        Response response = null;
        try {
            response = ClientUtil.sendTextRequest(req);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if(response.getStatus() == ResponseStatus.OK){
            //获取当前用户
            User user2 = (User)response.getData("user");
            if(user2!= null){ //登录成功
                DataBuffer.currentUser = user2;

                //获取当前在线用户列表
                DataBuffer.onlineUsers = (List<User>)response.getData("onlineUsers");

                if(matesList.size() < 100) {
                    for (int i = 0; i < 100; i++) {
                        DataBuffer.matesList.add(new ArrayList<User>());
                        DataBuffer.matesListModels.add(new MatesListModel());
                    }
                }
                LoginFrame.this.dispose();
                DataBuffer.lblCount.setText(DataBuffer.onlineUsers.size()+"");
                new ChatFrame();  //打开聊天窗体
            }else{ //登录失败
                String str = (String)response.getData("msg");
                JOptionPane.showMessageDialog(LoginFrame.this,
                        str,
                        "登录失败",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(LoginFrame.this,
                    "服务器内部错误，请稍后再试！！！","登录失败",JOptionPane.ERROR_MESSAGE);
        }
    }
}
