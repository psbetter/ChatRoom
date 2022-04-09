package server.view;

import common.model.entity.User;
import server.controller.RequestProcessor;
import server.model.DataBuffer;
import server.model.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerInfoFrame extends JFrame {
    private static final long serialVersionUID = 6274443611957724780L;
    private JTextField jta_msg;
    private JTable onlineUserTable ;
    private JTable registedUserTable ;
    private int WindowWidth = 1024;
    private int WindowHeight = 640;

    public ServerInfoFrame(InetAddress address){
        init(address);
        loadData();
        setVisible(true);
    }

    public void init(InetAddress address){
        //使用服务器缓存中的TableModel
        onlineUserTable = new JTable(DataBuffer.onlineUserTableModel);
        registedUserTable = new JTable(DataBuffer.registedUserTableModel);
        // 取得表格上的弹出菜单对象,加到表格上
        JPopupMenu pop = getTablePop();
        onlineUserTable.setComponentPopupMenu(pop);
        JPopupMenu popReg = getUpadteUserPop();
        registedUserTable.setComponentPopupMenu(popReg);

        //设置外观感觉， 设置JFrame禁用本地外观，使用下面自定义设置的外观；
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置背景颜色，记住一定要修改frame.getContentPane()的颜色，因为我们看到的都是这个的颜色而并不是frame的颜色
        this.setBackground(new Color(52, 130, 203));

        this.setTitle("聊天服务器");//设置聊天服务器标题
        // 设置窗口大小并使其处于屏幕中心
        this.setBounds((DataBuffer.screenSize.width - this.WindowWidth)/2,
                (DataBuffer.screenSize.height - this.WindowHeight)/2, this.WindowWidth, this.WindowHeight);

        JPanel pnlServer = new JPanel();
        pnlServer.setLayout(null);
        pnlServer.setBackground(new Color(52, 130, 203));

        JPanel pnlServerInfo = new JPanel(new GridLayout(14, 1));
        pnlServerInfo.setBackground(new Color(52, 130, 203));
        pnlServerInfo.setFont(new Font("宋体", 0, 12));
        pnlServerInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""), BorderFactory
                        .createEmptyBorder(1, 1, 1, 1)));

        JLabel lblStatus = new JLabel("当前状态:");
        lblStatus.setForeground(Color.YELLOW);
        lblStatus.setFont(new Font("宋体", 0, 12));
        JTextField txtStatus = new JTextField("已启动", 10);
        txtStatus.setBackground(Color.decode("#d6f4f2"));
        txtStatus.setFont(new Font("宋体", 0, 12));
        txtStatus.setEditable(false);

        JLabel lblNumber = new JLabel("当前在线人数:");
        lblNumber.setForeground(Color.YELLOW);
        lblNumber.setFont(new Font("宋体", 0, 12));
        JTextField txtNumber = DataBuffer.txtNumber;

        txtNumber.setBackground(Color.decode("#d6f4f2"));
        txtNumber.setFont(new Font("宋体", 0, 12));
        txtNumber.setEditable(false);

        JLabel lblMax = new JLabel("最多在线人数:");
        lblMax.setForeground(Color.YELLOW);
        lblMax.setFont(new Font("宋体", 0, 12));
        JTextField txtMax = new JTextField("50 人", 10);
        txtMax.setBackground(Color.decode("#d6f4f2"));
        txtMax.setFont(new Font("宋体", 0, 12));
        txtMax.setEditable(false);

        JLabel lblServerName = new JLabel("服务器名称:");
        lblServerName.setForeground(Color.YELLOW);
        lblServerName.setFont(new Font("宋体", 0, 12));
        JTextField txtServerName = new JTextField(address.getHostName(), 10);
        txtServerName.setBackground(Color.decode("#d6f4f2"));
        txtServerName.setFont(new Font("宋体", 0, 12));
        txtServerName.setEditable(false);

        JLabel lblProtocol = new JLabel("访问协议:");
        lblProtocol.setForeground(Color.YELLOW);
        lblProtocol.setFont(new Font("宋体", 0, 12));
        JTextField txtProtocol = new JTextField("HTTP", 10);
        txtProtocol.setBackground(Color.decode("#d6f4f2"));
        txtProtocol.setFont(new Font("宋体", 0, 12));
        txtProtocol.setEditable(false);

        JLabel lblIP = new JLabel("服务器IP:");
        lblIP.setForeground(Color.YELLOW);
        lblIP.setFont(new Font("宋体", 0, 12));
        JTextField txtIP = new JTextField(""+DataBuffer.ip, 10);
        txtIP.setBackground(Color.decode("#d6f4f2"));
        txtIP.setFont(new Font("宋体", 0, 12));
        txtIP.setEditable(false);

        JLabel lblPort = new JLabel("服务器端口:");
        lblPort.setForeground(Color.YELLOW);
        lblPort.setFont(new Font("宋体", 0, 12));
        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
        JTextField txtPort = new JTextField(""+port, 10);
        txtPort.setBackground(Color.decode("#d6f4f2"));
        txtPort.setFont(new Font("宋体", 0, 12));
        txtPort.setEditable(false);

        JButton btnStop = new JButton("关闭服务器(C)");
        /* 添加关闭服务器按钮事件处理方法 */
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                logout();
            }
        });
        //关闭窗口
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        btnStop.setBackground(Color.ORANGE);
        btnStop.setFont(new Font("宋体", 0, 12));

        JLabel lblLog = new JLabel("[服务器日志]");
        lblLog.setForeground(Color.YELLOW);
        lblLog.setFont(new Font("宋体", 0, 12));
        TextArea taLog = DataBuffer.serverLog;
        taLog.setFont(new Font("宋体", 0, 12));

        JButton btnSaveLog = new JButton("保存日志(S)");
        btnSaveLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveLog(taLog);
            }
        });
        btnSaveLog.setBackground(Color.ORANGE);
        btnSaveLog.setFont(new Font("宋体", 0, 12));

        pnlServerInfo.add(lblStatus);
        pnlServerInfo.add(txtStatus);
        pnlServerInfo.add(lblNumber);
        pnlServerInfo.add(txtNumber);
        pnlServerInfo.add(lblMax);
        pnlServerInfo.add(txtMax);
        pnlServerInfo.add(lblServerName);
        pnlServerInfo.add(txtServerName);
        pnlServerInfo.add(lblProtocol);
        pnlServerInfo.add(txtProtocol);
        pnlServerInfo.add(lblIP);
        pnlServerInfo.add(txtIP);
        pnlServerInfo.add(lblPort);
        pnlServerInfo.add(txtPort);

        pnlServerInfo.setBounds(10, 15, (int)(this.WindowWidth * 0.2), (int)(this.WindowHeight * 0.85));
        lblLog.setBounds((int)(this.WindowWidth * 0.2)+25, 10, (int)(this.WindowWidth * 0.2), 30);
        taLog.setBounds((int)(this.WindowWidth * 0.2)+25, 40, (int)(this.WindowWidth * 0.75), (int)(this.WindowHeight * 0.7));
        btnStop.setBounds((int)(this.WindowWidth * 0.2)+25, this.WindowHeight-135, 200, 50);
        btnSaveLog.setBounds((int)(this.WindowWidth * 0.2)+250, this.WindowHeight-135, 200, 50);
        pnlServer.add(pnlServerInfo);
        pnlServer.add(lblLog);
        pnlServer.add(taLog);
        pnlServer.add(btnStop);
        pnlServer.add(btnSaveLog);
        // ===========在线用户面板====================
        JPanel pnlUser = new JPanel();
        pnlUser.setLayout(null);
        pnlUser.setBackground(new Color(52, 130, 203));
        pnlUser.setFont(new Font("宋体", 0, 12));
        // 用户消息栏
        JLabel lblMessage = new JLabel("[用户消息]");
        lblMessage.setFont(new Font("宋体", 0, 12));
        lblMessage.setForeground(Color.YELLOW);
        TextArea taMessage = DataBuffer.tareMessage;
        taMessage.setFont(new Font("宋体", 0, 12));
        // 已注册用户列表栏
        JLabel registerMessage = new JLabel("[已注册用户列表]");
        registerMessage.setFont(new Font("宋体", 0, 12));
        registerMessage.setForeground(Color.YELLOW);
        JScrollPane rejpUser = new JScrollPane(registedUserTable);

        // 通知栏
        JLabel lblNotice = new JLabel("通知：");
        lblNotice.setFont(new Font("宋体", 0, 12));
        JTextField txtNotice = new JTextField(20);
        txtNotice.setFont(new Font("宋体", 0, 12));
        JButton btnSend = new JButton("发送");
        btnSend.setBackground(Color.ORANGE);
        btnSend.setFont(new Font("宋体", 0, 12));
        btnSend.setEnabled(true);
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    sendAllMsg(txtNotice);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JLabel lblUserCount = DataBuffer.lblUserCount;
        lblUserCount.setFont(new Font("宋体", 0, 12));

        JLabel lblUser = new JLabel("[在线用户列表]");
        lblUser.setFont(new Font("宋体", 0, 12));
        lblUser.setForeground(Color.YELLOW);
        lblUser.setPreferredSize(new Dimension((int)(this.WindowWidth * 0.35),(int)(this.WindowHeight * 0.7)));
        JScrollPane spUser = new JScrollPane(onlineUserTable);

        lblMessage.setBounds(10, 10, (int)(this.WindowWidth * 0.45), 25);
        taMessage.setBounds(10, 40, (int)(this.WindowWidth * 0.45), (int)(this.WindowHeight * 0.35));
        registerMessage.setBounds(10, (int)(this.WindowHeight * 0.4)+10, (int)(this.WindowWidth * 0.45), 25);
        rejpUser.setBounds(10, (int)(this.WindowHeight * 0.4)+35, (int)(this.WindowWidth * 0.45), (int)(this.WindowHeight * 0.3));
        lblUser.setBounds((int)(this.WindowWidth * 0.5), 10, (int)(this.WindowWidth * 0.45), 25);
        spUser.setBounds((int)(this.WindowWidth * 0.5), 40, (int)(this.WindowWidth * 0.45), (int)(this.WindowHeight * 0.7));
        lblNotice.setBounds(10, this.WindowHeight-140, 40, 50);
        txtNotice.setBounds(55, this.WindowHeight-140, (int)(this.WindowWidth * 0.25), 50);
        btnSend.setBounds((int)(this.WindowWidth * 0.25)+55, this.WindowHeight-140, 80, 50);
        lblUserCount.setBounds((int)(this.WindowWidth * 0.5), this.WindowHeight-140, 100, 50);

        pnlUser.add(lblMessage);
        pnlUser.add(taMessage);
        pnlUser.add(registerMessage);
        pnlUser.add(rejpUser);
        pnlUser.add(lblUser);
        pnlUser.add(spUser);
        pnlUser.add(lblNotice);
        pnlUser.add(txtNotice);
        pnlUser.add(btnSend);
        pnlUser.add(lblUserCount);

        // ============主标签面板========================

        JTabbedPane tpServer = new JTabbedPane(JTabbedPane.TOP);
        tpServer.setBackground(Color.decode("#d6f4f2"));
        tpServer.setFont(new Font("宋体", 0, 12));
        tpServer.add("服务器管理", pnlServer);
        tpServer.add("用户信息管理", pnlUser);
        this.getContentPane().add(tpServer);
        setVisible(true);
    }

    /** 关闭服务器 */
    private void logout() {
        int select = JOptionPane.showConfirmDialog(ServerInfoFrame.this,
                "确定关闭吗？\n\n关闭服务器将中断与所有客户端的连接!",
                "关闭服务器",
                JOptionPane.YES_NO_OPTION);
        //如果用户点击的是关闭服务器按钮时会提示是否确认关闭。
        if (select == JOptionPane.YES_OPTION) {
            System.exit(0);//退出系统
        }else{
            //覆盖默认的窗口关闭事件动作
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
    /*
     * 创建表格上的弹出菜单对象，实现发信，踢人功能
     */
    private JPopupMenu getTablePop() {
        JPopupMenu pop = new JPopupMenu();// 弹出菜单对象
        // 菜单项对象
        JMenuItem mi_send = new JMenuItem("发信");
        mi_send.setActionCommand("send");// 设定菜单命令关键字
        JMenuItem mi_del = new JMenuItem("踢掉");// 菜单项对象
        mi_del.setActionCommand("del");// 设定菜单命令关键字
        // 弹出菜单上的事件监听器对象
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = e.getActionCommand();
                // 哪个菜单项点击了，这个s就是其设定的ActionCommand
                popMenuAction(s);
            }
        };
        mi_send.addActionListener(al);
        mi_del.addActionListener(al);// 给菜单加上监听器
        pop.add(mi_send);
        pop.add(mi_del);
        return pop;
    }
    // 处理弹出菜单上的事件
    private void popMenuAction(String command) {
        // 得到在表格上选中的行
        final int selectIndex = onlineUserTable.getSelectedRow();
        String usr_id = (String)onlineUserTable.getValueAt(selectIndex,0);
//        System.out.println(usr_id);
        if (selectIndex == -1) {
            JOptionPane.showMessageDialog(this, "请选中一个用户");
            return;
        }

        if (command.equals("del")) {
            // 从线程中移除处理线程对象
            try {
                RequestProcessor.remove(DataBuffer.onlineUsersMap.get(Long.valueOf(usr_id)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command.equals("send")) {
            final JDialog jd = new JDialog(this, true);// 发送对话框
            jd.setLayout(new FlowLayout());
			jd.setTitle("发送系统信息给特定用户");
            jd.setLocation((int)(this.WindowWidth*0.6), (int)(this.WindowHeight*0.6));
            jd.setSize((int)(this.WindowWidth*0.3), (int)(this.WindowHeight*0.3));
            final JTextField jtd_m = new JTextField(20);
            JButton jb = new JButton("发送");
            jd.add(jtd_m);
            jd.add(jb);
            // 发送按钮的事件实现
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DataBuffer.serverLog.append("[Info] 系统发出消息");
                    String msg = jtd_m.getText();
                    try {
                        RequestProcessor.chat_sys(msg,DataBuffer.onlineUsersMap.get(Long.valueOf(usr_id)));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    jtd_m.setText("");// 清空输入框
                    jd.dispose();
                }
            });
            jd.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "请选择用户再点击菜单");
        }
        // 刷新表格
        SwingUtilities.updateComponentTreeUI(onlineUserTable);
    }
    /** 把所有已注册的用户信息加载到RegistedUserTableModel中 */
    private void loadData(){
        List<User> users = new UserService().loadAllUser();
        for (User user : users) {
            DataBuffer.registedUserTableModel.add(new String[]{
                    String.valueOf(user.getId()),
                    user.getPassword(),
                    user.getNickname(),
                    String.valueOf(user.getSex())
            });
        }
    }
    // 按下发送服务器消息的按钮，给所有在线用户发送消息
    private void sendAllMsg(JTextField txtNotice) throws IOException {
        RequestProcessor.board(txtNotice.getText());
        txtNotice.setText("");// 清空输入框
    }
    // 保存服务器日志
    protected void saveLog(TextArea taMessage) {
        try {
            FileOutputStream fileoutput = new FileOutputStream("ServerLog.txt",
                    true);
            String temp = taMessage.getText();
            fileoutput.write(temp.getBytes());
            fileoutput.close();
            JOptionPane.showMessageDialog(null, "记录保存在ServerLog.txt");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    // 修改用户信息
    private JPopupMenu getUpadteUserPop() {
        JPopupMenu pop = new JPopupMenu();// 弹出菜单对象
        // 菜单项对象
        JMenuItem mi_update = new JMenuItem("修改用户信息");
        mi_update.setActionCommand("update");// 设定菜单命令关键字
        JMenuItem mi_del = new JMenuItem("删除用户信息");// 菜单项对象
        mi_del.setActionCommand("del");
        // 弹出菜单上的事件监听器对象
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = e.getActionCommand();
                // 哪个菜单项点击了，这个s就是其设定的ActionCommand
                popUpdateUser(s);
            }
        };
        mi_update.addActionListener(al);// 给菜单加上监听器
        mi_del.addActionListener(al);
        pop.add(mi_update);
        pop.add(mi_del);
        return pop;
    }
    // 处理弹出菜单上的事件
    private void popUpdateUser(String command) {
        // 得到在表格上选中的行
        int selectIndex = registedUserTable.getSelectedRow();
        if (selectIndex == -1) {
            JOptionPane.showMessageDialog(this, "请选中一个用户");
            return;
        }

        String userId = (String)registedUserTable.getValueAt(selectIndex,0);
        String[] userRow = new String[registedUserTable.getColumnCount()];
        for(int i=0;i<userRow.length;i++){
            userRow[i] = (String)registedUserTable.getValueAt(selectIndex,i);
        }
        if (command.equals("del")) {
            UserService userService = new UserService();
            User delUser = userService.loadUser(Long.parseLong(userId));
            userService.delUser(delUser);
            DataBuffer.registedUserTableModel.clear();
            loadData();
        }else if (command.equals("update")) {
            final JDialog jd = new JDialog(this, true);// 更新对话框
            jd.setLayout(new FlowLayout());
            jd.setTitle("修改用户信息");
            jd.setLocation((DataBuffer.screenSize.width - 200)/2, (DataBuffer.screenSize.height - 200)/2);
            jd.setSize(200, 200);
            jd.setBackground(Color.white);

            JLabel userAccount = new JLabel("账号");
//            userAccount.setSize( 90, 30);
            JTextField accountTxt = new JTextField(20);
//            accountTxt.setSize(400, 30);
            accountTxt.setText(userId);
            accountTxt.setEditable(false);
            JLabel userPwd = new JLabel("密码");
//            userPwd.setSize(90, 30);
            JTextField pwdTxt = new JTextField(20);
//            pwdTxt.setSize(400, 30);
            pwdTxt.setText((String)registedUserTable.getValueAt(selectIndex,1));
            JLabel userNickname = new JLabel("昵称");
            JTextField nicknameTxt = new JTextField(20);
//            userNickname.setSize(90, 30);
//            nicknameTxt.setSize(400, 30);
            nicknameTxt.setText((String)registedUserTable.getValueAt(selectIndex,2));
            JLabel userSex = new JLabel("性别");
            JTextField sexTxt = new JTextField(20);
//            userSex.setSize(90, 30);
//            sexTxt.setSize(400, 30);
            sexTxt.setText((String)registedUserTable.getValueAt(selectIndex,3));
            JButton done = new JButton("确定");
//            done.setSize(90, 30);
            JButton cancel = new JButton("取消");
//            cancel.setSize(90, 30);

            jd.add(userAccount);
            jd.add(accountTxt);
            jd.add(userPwd);
            jd.add(pwdTxt);
            jd.add(userNickname);
            jd.add(nicknameTxt);
            jd.add(userSex);
            jd.add(sexTxt);
            jd.add(done);
            jd.add(cancel);
            // 确定按钮的事件实现
            done.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    User savedUser = new User(Long.parseLong(userId), pwdTxt.getText());
                    savedUser.setNickname(nicknameTxt.getText());
                    savedUser.setSex(sexTxt.getText().equals("m") ? 'm' : 'f');
                    UserService userService = new UserService();
                    userService.updateUser(savedUser);
                    DataBuffer.serverLog.append("[Info] 用户信息已更新");
                    DataBuffer.registedUserTableModel.clear();
                    loadData();
                    jd.dispose();
                }
            });
            // 确定按钮的事件实现
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DataBuffer.serverLog.append("[Info] 用户信息已取消更新");
                    jd.dispose();
                }
            });
            jd.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "操作用户信息出错");
        }
        // 刷新表格
        SwingUtilities.updateComponentTreeUI(registedUserTable);
    }
}
