package client.view;

import client.model.ClientThread;
import client.model.DataBuffer;
import client.model.entity.MatesListModel;
import client.model.entity.MyCellRenderer;
import client.model.entity.OnlineUserListModel;
import client.util.ClientUtil;
import client.util.JFrameShaker;
import common.model.entity.FileInfo;
import common.model.entity.Message;
import common.model.entity.Request;
import common.model.entity.User;
import server.model.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static client.model.DataBuffer.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 */

public class ChatFrame extends JFrame {
    private static final long serialVersionUID = -2310785591507878535L;
    JPanel pnlChat;

    JButton btnCls, btnExit, btnSend, btnClear, btnSave, addFri, shkWin, sendFileBtn, sendEmoj;

    JLabel lblUserList, lblUserMessage, lblSendMessage, lblChatUser, lblfriendList;

    JLabel lblUserTotal, lblCount, lblBack;

    JList onlineList;

    public static JTextField txtMessage;

    public static JPanel taUserMessage;

    public static JScrollPane jsp;

    public static String selectIcon;

    JComboBox cmbUser;

    JCheckBox chPrivateChat;

    final JLabel headLabel = new JLabel();

    Toolkit toolkit = Toolkit.getDefaultToolkit();

    // 用户好友列表
    public static JList mateList;
    /**
     * 发送添加好友和删除好友的申请
     */
    public static Message sendMix;
    public static Message sendDelMix;
    /**
     * 准备发送的文件
     */
    public static FileInfo sendFile;

    public static int width = 1024;
    public static int height = 640;
    public static int ChatAreaHeight = 440;

    public ChatFrame() {
        this.init();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void init() {
        Image img = toolkit.getImage("images\\appico.jpg");
        this.setIconImage(img);
        this.setBounds((DataBuffer.screenSize.width-width)/2,
                (DataBuffer.screenSize.height-height)/2, width, height);
        this.setResizable(false);
        User currentUser = DataBuffer.currentUser;

        this.setTitle("聊天室" + "[用户:" + currentUser.getNickname() + "]");
        pnlChat = new JPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(pnlChat);

        Font fntDisp1 = new Font("宋体", Font.PLAIN, 12);

        String list[] = { "所有人" };
        sendEmoj = new JButton("发送表情");
        addFri = new JButton("添加好友");
        shkWin = new JButton("窗口抖动");
        sendFileBtn = new JButton("发送文件");
        btnCls = new JButton("清屏");
        btnExit = new JButton("退出");
        btnSend = new JButton("发送");
        btnSave = new JButton("保存");
        lblUserList = new JLabel("【在线用户列表】");
        lblfriendList = new JLabel("【好友列表】");
        lblUserMessage = new JLabel("【聊天信息】");
        lblSendMessage = new JLabel("聊天内容:");
        lblChatUser = new JLabel("你对:");
        lblUserTotal = new JLabel("在线人数:");
        lblCount = DataBuffer.lblCount;
        txtMessage = new JTextField(170);
        cmbUser = new JComboBox(list);
        cmbUser.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                freshHead();
            }
        });
        chPrivateChat = new JCheckBox("私聊");

        taUserMessage = new JPanel(new FlowLayout(FlowLayout.LEFT));
        taUserMessage.setLocation(240, 50);
        taUserMessage.setPreferredSize(new Dimension(750,ChatAreaHeight));
        jsp=new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.doLayout();
        jsp.getViewport().add(taUserMessage);
        jsp.setBounds(240, 50, 750,ChatAreaHeight);


        pnlChat.setLayout(null);
        pnlChat.setBackground(new Color(52, 130, 203));

        // 聊天内容
        lblSendMessage.setBounds(width-400, height-130, 60, 25);
        txtMessage.setBounds(width-330, height-130, 200, 25);
        // 私聊
        chPrivateChat.setBounds(width-300, height-100, 80, 25);
        // 发送表情\添加好友\窗口抖动\发送文件\清屏
        sendEmoj.setBounds(width-570, height-130, 80, 25);
        addFri.setBounds(width-570, height-100, 80, 25);
        shkWin.setBounds(width-480, height-100, 80, 25);
        sendFileBtn.setBounds(width-390, height-100, 80, 25);
        btnCls.setBounds(width-210, height-100, 80, 25);
        // 发送、保存、退出
        btnSend.setBounds(width-110, height-130, 80, 25);
        btnSave.setBounds(width-110, height-100, 80, 25);
        btnExit.setBounds(width-110, height-70, 80, 25);
        // 在线用户列表
        lblUserList.setBounds(20, 10, 120, 40);
        lblUserTotal.setBounds(130, 10, 60, 40);
        lblCount.setBounds(200, 10, 60, 40);

        // 获取在线用户并缓存
        DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);
        // 在线用户列表
        JPanel lstUserList = new JPanel();
        lstUserList.setLayout(new BorderLayout());
        onlineList = new JList(DataBuffer.onlineUserListModel);
        onlineList.setCellRenderer(new MyCellRenderer());
        // 设置为单选模式
        onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstUserList.add(onlineList);
        lstUserList.setBounds(20, 50, 200, height/3);
        // 好友列表
        lblfriendList.setBounds(20, height/2-40, 200, 40);
//        lstfriendList.setBounds(20, height/2+10, width/2-100, height/3);
        // mateslist弹出菜单
        JPopupMenu pop1 = getListPop();
        onlineList.setComponentPopupMenu(pop1);
        int currentId = (int) DataBuffer.currentUser.getId();

        // mateslist初始化
        JPanel matesPane = new JPanel();
        matesPane.setLayout(new BorderLayout());
        matesPane.setBounds(20, height/2, 200, 270);
        MatesListModel matesListModel;
        matesListModel = new MatesListModel(DataBuffer.currentUser.getMateList());
        matesListModels.set(currentId-1, matesListModel);
        mateList = new JList(matesListModels.get(currentId-1));
        mateList.setCellRenderer(new MyCellRenderer());
        mateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matesPane.add(new JScrollPane(mateList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        // mateslist弹出菜单
        JPopupMenu pop2 = delListPop();
        mateList.setComponentPopupMenu(pop2);

        //聊天信息
        lblUserMessage.setBounds(240, 10, 180, 40);

        // 你对
        lblChatUser.setBounds(240, height-130, 40, 25);
        cmbUser.setBounds(300, height-130, 80, 25);

        // 头像
        headLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headLabel.setIcon(new ImageIcon("face//1.JPG"));
        headLabel.setBounds(240, height-100, 70, 60);

        sendEmoj.setFont(fntDisp1);
        addFri.setFont(fntDisp1);
        shkWin.setFont(fntDisp1);
        sendFileBtn.setFont(fntDisp1);
        btnCls.setFont(fntDisp1);
        btnExit.setFont(fntDisp1);
        btnSend.setFont(fntDisp1);
        btnSave.setFont(fntDisp1);
        lblUserList.setFont(fntDisp1);
        lblUserMessage.setFont(fntDisp1);
        lblChatUser.setFont(fntDisp1);
        lblSendMessage.setFont(fntDisp1);
        lblUserTotal.setFont(fntDisp1);
        lblCount.setFont(fntDisp1);
        cmbUser.setFont(fntDisp1);
        chPrivateChat.setFont(fntDisp1);
        matesPane.setFont(fntDisp1);
        lblfriendList.setFont(fntDisp1);

        lblUserList.setForeground(Color.YELLOW);
        lblUserMessage.setForeground(Color.YELLOW);
        lblSendMessage.setForeground(Color.black);
        lblChatUser.setForeground(Color.black);
        lblSendMessage.setForeground(Color.black);
        lblUserTotal.setForeground(Color.YELLOW);
        lblCount.setForeground(Color.YELLOW);
        cmbUser.setForeground(Color.black);
        chPrivateChat.setForeground(Color.black);
        lstUserList.setBackground(Color.white);
        taUserMessage.setBackground(Color.white);
        sendEmoj.setBackground(Color.ORANGE);
        addFri.setBackground(Color.ORANGE);
        shkWin.setBackground(Color.ORANGE);
        sendFileBtn.setBackground(Color.ORANGE);
        btnCls.setBackground(Color.ORANGE);
        btnExit.setBackground(Color.ORANGE);
        btnSend.setBackground(Color.PINK);
        btnSave.setBackground(Color.ORANGE);
        matesPane.setBackground(Color.white);
        lblfriendList.setForeground(Color.YELLOW);

        pnlChat.add(sendEmoj);
        pnlChat.add(addFri);
        pnlChat.add(shkWin);
        pnlChat.add(sendFileBtn);
        pnlChat.add(btnCls);
        pnlChat.add(btnExit);
        pnlChat.add(btnSend);
        pnlChat.add(btnSave);
        pnlChat.add(lblUserList);
        pnlChat.add(lblUserMessage);
        pnlChat.add(lblSendMessage);
        pnlChat.add(lblChatUser);
        pnlChat.add(lblUserTotal);
        pnlChat.add(lblCount);
        pnlChat.add(lstUserList);
        pnlChat.add(jsp);
//        pnlChat.add(taUserMessage);
        pnlChat.add(txtMessage);
        pnlChat.add(cmbUser);
        pnlChat.add(chPrivateChat);
        pnlChat.add(lblfriendList);
//        pnlChat.add(lstfriendList);
        pnlChat.add(headLabel);
        pnlChat.add(matesPane);

        ///////////////////////注册事件监听器/////////////////////////
        //关闭窗口
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        //关闭按钮的事件
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                logout();
            }
        });
        // 清屏
        btnCls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                cancel();
            }
        });

        //发送文本消息
        txtMessage.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == Event.ENTER) {
                    sendTxtMsg();
                }
            }
        });
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendTxtMsg();
            }
        });
        // 发送表情消息
        sendEmoj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                sendEmojiMsg();
            }
        });

        //发送振动
        shkWin.addActionListener(event -> sendShakeMsg());
        //发送文件
        sendFileBtn.addActionListener(event -> sendFile());
        //发送添加好友请求
        addFri.addActionListener(e -> toMix());
        //保存聊天信息
        btnSave.addActionListener(e -> saveChat(taUserMessage));

        this.loadData();  //加载初始数据
    }

    // 保存聊天信息
    protected void saveChat(JPanel taMessage) {
        try {
            FileOutputStream fileoutput = new FileOutputStream("ChatLog.txt",
                    true);
            String temp = "";
            int count = taMessage.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component comp = taMessage.getComponent(i);
                if(comp instanceof JTextArea){
                    temp += ((JTextArea) comp).getText() + "\n";
                }
            }
            fileoutput.write(temp.getBytes());
            fileoutput.close();
            JOptionPane.showMessageDialog(null, "聊天记录保存在ChatLog.txt");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 创建列表上的弹出菜单对象，实现请求添加好友功能192 168 116 66
     */
    public JPopupMenu getListPop() {
        JPopupMenu pop1 = new JPopupMenu();
        JMenuItem to_mix = new JMenuItem("申请添加好友");
        to_mix.setActionCommand("toMix");
        to_mix.addActionListener(e -> toMix());
        pop1.add(to_mix);
        return pop1;
    }

    /**
     * 创建好友列表上的弹出菜单对象，实现删除好友功能
     */
    public JPopupMenu delListPop() {
        JPopupMenu pop2 = new JPopupMenu();
        JMenuItem del_mix = new JMenuItem("删除好友");
        del_mix.setActionCommand("delMix");
        del_mix.addActionListener(e -> delMix());
        pop2.add(del_mix);
        return pop2;
    }

    /**
     *申请添加好友
     */
    public void toMix() {
        User selectedUser = (User) onlineList.getSelectedValue();
        if (null != selectedUser) {
            if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                JOptionPane.showMessageDialog(ChatFrame.this, "不能将自己添加为好友!",
                        "请求失败", JOptionPane.ERROR_MESSAGE);
            } else if(DataBuffer.currentUser.testMateId(selectedUser)) {
                JOptionPane.showMessageDialog(ChatFrame.this, "对方已在您的好友列表中!",
                        "请求失败", JOptionPane.ERROR_MESSAGE);
            } else{
                sendMix = new Message();
                sendMix.setFromUser(DataBuffer.currentUser);
                sendMix.setToUser(selectedUser);
                sendMix.setSendTime(new Date());
                Request request = new Request();
                request.setAction("toMix");
                request.setAttribute("Message", sendMix);
                try {
                    ClientUtil.sendTextRequest2(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClientUtil.appendTxt2MsgListArea("【系统消息】已向用户 "
                        + selectedUser.getNickname() + "("
                        + selectedUser.getId() + ") 发送添加好友申请，等待对方同意...\n", currentUser);
            }
        } else {
            JOptionPane.showMessageDialog(ChatFrame.this, "请选中要添加好友的对象！",
                    "添加好友失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除好友
     */
    public void delMix() {
        User selectedUser = (User) mateList.getSelectedValue();
        if (null != selectedUser) {
            int select = JOptionPane.showConfirmDialog(ChatFrame.this,
                    "确定要删除" + selectedUser.getNickname() + "吗？",
                    "删除好友", JOptionPane.YES_NO_OPTION);
            if(select == JOptionPane.YES_OPTION) {
                sendDelMix = new Message();
                sendDelMix.setFromUser(DataBuffer.currentUser);
                sendDelMix.setToUser(selectedUser);
                sendDelMix.setSendTime(new Date());
                Request request = new Request();
                request.setAction("toDelMix");
                request.setAttribute("sendDelMix", sendDelMix);
                try {
                    ClientUtil.sendTextRequest2(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClientUtil.appendTxt2MsgListArea("【系统消息】您已将用户 "
                        + selectedUser.getNickname() + "("
                        + selectedUser.getId() + ") 从好友列表中删除\n", currentUser);
            }
        }else {
            JOptionPane.showMessageDialog(ChatFrame.this, "请选中要删除的好友对象!",
                    "操作失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 加载数据
     */
    public void loadData() {
        // 加载当前用户数据
        if (null != DataBuffer.currentUser) {
            headLabel.setIcon(
                    new ImageIcon("face/" + DataBuffer.currentUser.getHead() + ".JPG"));
        }
//         启动监听服务器消息的线程
        new ClientThread(this).start();
    }

    /**
     * 清空聊天记录
     */
    public void cancel() {
        taUserMessage.removeAll();
        ChatAreaHeight = height-200;
        DataBuffer.msgAreaHeight = 0;
        taUserMessage.setPreferredSize(new Dimension(750,ChatAreaHeight));
        taUserMessage.revalidate();
        currentUser.clearChatRecords();
        UserService userService = new UserService();
        userService.saveUser(currentUser);
    }

    /**
     * 发送振动
     */
    public void sendShakeMsg() {
        User selectedUser = (User) onlineList.getSelectedValue();
        if (null != selectedUser) {
            if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送振动!",
                        "不能发送", JOptionPane.ERROR_MESSAGE);
            } else {
                Message msg = new Message();
                msg.setFromUser(DataBuffer.currentUser);
                msg.setToUser(selectedUser);
                msg.setSendTime(new Date());

                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                StringBuffer sb = new StringBuffer();
                sb.append(" ").append(msg.getFromUser().getNickname())
                        .append("(").append(msg.getFromUser().getId()).append(") ")
                        .append(df.format(msg.getSendTime()))
                        .append("给").append(msg.getToUser().getNickname())
                        .append("(").append(msg.getToUser().getId()).append(") ")
                        .append("发送了一个窗口抖动");
                msg.setMessage(sb.toString());

                Request request = new Request();
                request.setAction("shake");
                request.setAttribute("msg", msg);
                try {
                    ClientUtil.sendTextRequest2(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClientUtil.appendTxt2MsgListArea(msg.getMessage(), currentUser);
                new JFrameShaker(ChatFrame.this).startShake();
            }
        } else {
            JOptionPane.showMessageDialog(ChatFrame.this, "不能群发送振动!",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        }
//        SwingUtilities.updateComponentTreeUI(taUserMessage);
    }

    /**
     * 发送文本消息
     */
    public void sendTxtMsg() {
        String content = txtMessage.getText();
        if ("".equals(content)) { //无内容
            JOptionPane.showMessageDialog(ChatFrame.this, "不能发送空消息!",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        } else { //发送
            User selectedUser = (User) onlineList.getSelectedValue();

            //如果设置了ToUser表示私聊，否则群聊
            Message msg = new Message();
            if (chPrivateChat.isSelected()) {  //私聊
                if (null == selectedUser) {
                    JOptionPane.showMessageDialog(ChatFrame.this, "没有选择私聊对象!",
                            "不能发送", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                    JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送消息!",
                            "不能发送", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    msg.setToUser(selectedUser);
                }
            }
            msg.setFromUser(DataBuffer.currentUser);
            msg.setSendTime(new Date());

            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            StringBuffer sb = new StringBuffer();
            sb.append(" ").append(df.format(msg.getSendTime())).append(" ")
                    .append(msg.getFromUser().getNickname())
                    .append("(").append(msg.getFromUser().getId()).append(") ");
            if (!this.chPrivateChat.isSelected()) { //群聊
                sb.append("对大家说");
            }
            sb.append("  ").append(content);
            msg.setMessage(sb.toString());

            Request request = new Request();
            request.setAction("chat");
            request.setAttribute("msg", msg);
            try {
                ClientUtil.sendTextRequest2(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //JTextArea中按“Enter”时，清空内容并回到首行
            InputMap inputMap = txtMessage.getInputMap();
            ActionMap actionMap = txtMessage.getActionMap();
            Object transferTextActionKey = "TRANSFER_TEXT";
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), transferTextActionKey);
            actionMap.put(transferTextActionKey, new AbstractAction() {
                private static final long serialVersionUID = 7041841945830590229L;
                public void actionPerformed(ActionEvent e) {
                    txtMessage.setText("");
                    txtMessage.requestFocus();
                }
            });
            txtMessage.setText("");
//            SwingUtilities.updateComponentTreeUI(taUserMessage);
        }
    }

    private void sendEmojiMsg(){
        final JDialog jd = new JDialog(this, true);// 更新对话框
        jd.setLayout(new FlowLayout());
        jd.setTitle("发送表情");
        jd.setLocation((DataBuffer.screenSize.width - 450)/2, (DataBuffer.screenSize.height - 410)/2);
        jd.setSize(450, 410);
        jd.setBackground(Color.white);

        JPanel jpanel = new JPanel();
        jpanel.setLayout(new GridLayout(10,10));
        jpanel.setSize(400, 400);

        for(int i=0;i<96;i++){
            JButton emo = new JButton();
            ImageIcon icon = new ImageIcon("emoji/"+ i + ".gif", "emoji/"+ i + ".gif");
            emo.setIcon(icon);
            emo.setSize(40, 40);
            emo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ImageIcon temp = (ImageIcon)emo.getIcon();
                    selectIcon = temp.getDescription();
                }
            });
            jpanel.add(emo);
        }
        JPanel jpanel2 = new JPanel();
        jpanel2.setLayout(new FlowLayout());
        jpanel2.setSize(400,40);
        JButton done = new JButton("确定");
        done.setPreferredSize(new Dimension(80, 40));
        jpanel2.add(done);
        JButton cancel = new JButton("取消");
        cancel.setPreferredSize(new Dimension(80, 40));
        jpanel2.add(cancel);

        jd.add(jpanel);
        jd.add(jpanel2);
        // 确定按钮的事件实现
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(selectIcon == null){
                    JOptionPane.showMessageDialog(ChatFrame.this, "请先选中表情!",
                            "不能发送", JOptionPane.ERROR_MESSAGE);
                }else{
                    comfirmEmoji(selectIcon);
                }
                jd.dispose();
            }
        });
        // 取消按钮的事件实现
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jd.dispose();
            }
        });
        jd.setVisible(true);
    }
    private void comfirmEmoji(String selectIcon){
        User selectedUser = (User) onlineList.getSelectedValue();
        Message msg = new Message();
        if (chPrivateChat.isSelected()) {  //私聊
            if (null == selectedUser) {
                JOptionPane.showMessageDialog(ChatFrame.this, "没有选择私聊对象!",
                        "不能发送", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送消息!",
                        "不能发送", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                msg.setToUser(selectedUser);
            }
        }
        msg.setFromUser(DataBuffer.currentUser);
        msg.setSendTime(new Date());

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        sb.append(" ").append(df.format(msg.getSendTime())).append(" ")
                .append(msg.getFromUser().getNickname())
                .append("(").append(msg.getFromUser().getId()).append(") ");
        if (!this.chPrivateChat.isSelected()) { //群聊
            sb.append("对大家发送了一个表情");
        }
        msg.setMessage(sb.toString());
        msg.setEmoji(selectIcon);

        Request request = new Request();
        request.setAction("chatWithEmoji");
        request.setAttribute("msg", msg);
        try {
            ClientUtil.sendTextRequest2(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送文件
     */
    private void sendFile() {
        User selectedUser = (User) onlineList.getSelectedValue();
        if (null != selectedUser) {
            if (DataBuffer.currentUser.getId() == selectedUser.getId()) {
                JOptionPane.showMessageDialog(ChatFrame.this, "不能给自己发送文件!",
                        "不能发送", JOptionPane.ERROR_MESSAGE);
            } else {
                JFileChooser jfc = new JFileChooser();
                if (jfc.showOpenDialog(ChatFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file = jfc.getSelectedFile();
                    sendFile = new FileInfo();
                    sendFile.setFromUser(DataBuffer.currentUser);
                    sendFile.setToUser(selectedUser);
                    try {
                        sendFile.setSrcName(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    sendFile.setSendTime(new Date());

                    Request request = new Request();
                    request.setAction("toSendFile");
                    request.setAttribute("file", sendFile);
                    try {
                        ClientUtil.sendTextRequest2(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ClientUtil.appendTxt2MsgListArea("【文件消息】向 "
                            + selectedUser.getNickname() + "("
                            + selectedUser.getId() + ") 发送文件 ["
                            + file.getName() + "]，等待对方接收...\n", currentUser);
//                    SwingUtilities.updateComponentTreeUI(taUserMessage);
                }
            }
        } else {
            JOptionPane.showMessageDialog(ChatFrame.this, "不能给所有在线用户发送文件!",
                    "不能发送", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 关闭客户端
     */
    private void logout() {
        int select = JOptionPane.showConfirmDialog(ChatFrame.this,
                "确定退出吗？\n\n退出程序将中断与服务器的连接!", "退出聊天室",
                JOptionPane.YES_NO_OPTION);
        if (select == JOptionPane.YES_OPTION) {
            Request req = new Request();
            req.setAction("exit");
            req.setAttribute("user", DataBuffer.currentUser);
            try {
                ClientUtil.sendTextRequest(req);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                System.exit(0);
            }
        } else {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    /*踢除*/
    public static void remove() {
        int select = JOptionPane.showConfirmDialog(txtMessage,
                "您已被踢除？\n\n", "系统通知",
                JOptionPane.YES_NO_OPTION);

        Request req = new Request();
        req.setAction("exit");
        req.setAttribute("user", DataBuffer.currentUser);
        try {
            ClientUtil.sendTextRequest(req);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    protected void freshHead() {
        int head = currentUser.getHead();
        headLabel.setIcon(new ImageIcon("face//" + head + ".JPG"));
    }
}
