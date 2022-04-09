package client.view;

import client.model.DataBuffer;
import client.util.ClientUtil;
import common.model.entity.Request;
import common.model.entity.Response;
import common.model.entity.ResponseStatus;
import common.model.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 */

public class RegisterFrame extends JFrame {
    private static final long serialVersionUID = -768631070458723803L;

    private JComboBox comboBox;
    public JPanel  pnlRegister;
    public JLabel  lblUserName,lblGender,lblAge;
    public JLabel  lblPassword,lblConfirmPass,lblEmail,logoPosition;
    public JTextField  txtUserName,txtAge,txtEmail;
    public JPasswordField  pwdUserPassword,pwdConfirmPass;
    public JRadioButton  rbtnMale,rbtnFemale;
    public ButtonGroup  btngGender;
    public JButton  btnOk,btnCancel,btnClear;
    public String  strServerIp;
    final JLabel headLabel = new JLabel();
    //用于将窗口用于定位
    Dimension scrnsize;
    Toolkit toolkit= Toolkit.getDefaultToolkit();

    public RegisterFrame(){
        this.init();
        setVisible(true);
    }

    public void init(){
        this.setTitle("注册新账号");//设置标题
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize((int)(DataBuffer.screenSize.width * 0.3), (int)(DataBuffer.screenSize.height * 0.3));
        //设置默认窗体在屏幕中央
        int x = (int)toolkit.getScreenSize().getWidth();
        int y = (int)toolkit.getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);
        this.setResizable(false);

        pnlRegister = new JPanel();
        this.getContentPane().add(pnlRegister);
        pnlRegister.setLayout(null);    //组件用手动布局
        pnlRegister.setBackground(new Color(52,130,203));

        lblUserName=new JLabel("用户名:");
        lblGender=new JLabel("性别:");
        lblAge=new JLabel("年龄:");
        lblPassword=new JLabel("密码:");
        lblConfirmPass=new JLabel("确认密码:");
        lblEmail=new JLabel("电子邮件:");
        txtUserName=new JTextField(30);
        txtEmail=new JTextField(30);
        txtAge=new JTextField(10);
        pwdUserPassword=new JPasswordField(30);
        pwdConfirmPass=new JPasswordField(30);
        rbtnMale=new JRadioButton("男",true);
        rbtnFemale=new JRadioButton("女");
        btngGender=new ButtonGroup();
        btnOk=new JButton("确定(O)");
        btnOk.setMnemonic('O');
        btnOk.setToolTipText("保存注册信息");
        btnCancel=new JButton("返回");
        btnCancel.setMnemonic('B');
        btnCancel.setToolTipText("返回登录窗口");
        btnClear=new JButton("清空");
        btnClear.setMnemonic('L');
        btnClear.setToolTipText("清空注册信息");

        /*  该布局采用手动布局           *
         *  setOpaque将背景设置为透明    */

        lblUserName.setBounds(30,80,100,30);
        txtUserName.setBounds(110,85,120,20);
        lblPassword.setBounds(30,141,100,30);
        pwdUserPassword.setBounds(110,146,120,20);
        lblConfirmPass.setBounds(30,166,100,30);
        pwdConfirmPass.setBounds(110,171,120,20);
        lblGender.setBounds(30,191,100,30);
        rbtnMale.setBounds(110,196,60,20);
        rbtnFemale.setBounds(190,196,60,20);
        lblAge.setBounds(30,216,100,30);
        txtAge.setBounds(110,221,120,20);
        lblEmail.setBounds(30,241,100,30);
        txtEmail.setBounds(110,246,120,20);

        btnOk.setBounds(246,166,80,25);
        btnCancel.setBounds(246,201,80,25);
        btnClear.setBounds(246,241,80,25);

        Font fontstr=new Font("宋体",Font.PLAIN,12);
        lblUserName.setFont(fontstr);
        lblGender.setFont(fontstr);
        lblPassword.setFont(fontstr);
        lblConfirmPass.setFont(fontstr);
        lblAge.setFont(fontstr);
        lblEmail.setFont(fontstr);
        rbtnMale.setFont(fontstr);
        rbtnFemale.setFont(fontstr);
        txtUserName.setFont(fontstr);
        txtEmail.setFont(fontstr);
        btnOk.setFont(fontstr);
        btnCancel.setFont(fontstr);
        btnClear.setFont(fontstr);

        lblUserName.setForeground(Color.BLACK);
        lblGender.setForeground(Color.BLACK);
        lblPassword.setForeground(Color.BLACK);
        lblAge.setForeground(Color.BLACK);
        lblConfirmPass .setForeground(Color.BLACK);
        lblEmail.setForeground(Color.BLACK);
        rbtnMale.setForeground(Color.BLACK);
        rbtnFemale.setForeground(Color.BLACK);
        rbtnMale.setBackground(Color.white);
        rbtnFemale.setBackground(Color.white);
        btnOk.setBackground(Color.ORANGE);
        btnCancel.setBackground(Color.ORANGE);
        btnClear.setBackground(Color.ORANGE);
        rbtnMale.setOpaque(false);
        rbtnFemale.setOpaque(false);

        pnlRegister.add(lblUserName);
        pnlRegister.add(lblGender);
        pnlRegister.add(lblPassword);
        pnlRegister.add(lblConfirmPass);
        pnlRegister.add(lblEmail);
        pnlRegister.add(lblAge);
        pnlRegister.add(txtAge);
        pnlRegister.add(txtUserName);
        pnlRegister.add(txtEmail);
        pnlRegister.add(pwdUserPassword);
        pnlRegister.add(pwdConfirmPass);
        pnlRegister.add(btnOk);
        pnlRegister.add(btnCancel);
        pnlRegister.add(btnClear);
        pnlRegister.add(rbtnMale);
        pnlRegister.add(rbtnFemale);
        btngGender.add(rbtnMale);
        btngGender.add(rbtnFemale);

        //设置背景图片
        Icon logo = new ImageIcon("images\\registerlogo.jpg");
        logoPosition = new JLabel(logo);
        logoPosition.setBounds(0, 0, 360,78);
        pnlRegister.add(logoPosition);

        this.setSize(360,313);
        this.setVisible(true);
        this.setResizable(false);
        //将窗口定位在屏幕中央
        scrnsize=toolkit.getScreenSize();
        this.setLocation(scrnsize.width/2-this.getWidth()/2,
                scrnsize.height/2-this.getHeight()/2);
        Image img=toolkit.getImage("images\\appico.jpg");
        this.setIconImage(img);

        final JLabel label = new JLabel();
        label.setFont(fontstr);
        label.setText("头像:");
        label.setBounds(30, 116, 100,30);
        pnlRegister.add(label);

        comboBox = new JComboBox();
        comboBox.setAutoscrolls(true);
        comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6"}));
        comboBox.setBounds(110, 116, 47, 23);
        comboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {
                Icon logo = new ImageIcon("face\\"+comboBox.getSelectedItem().toString()+".jpg");
                headLabel.setIcon(logo);
            }
        });
        pnlRegister.add(comboBox);

        headLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headLabel.setIcon(new ImageIcon("face//1.JPG"));
        headLabel.setBounds(247, 88, 74, 72);
        pnlRegister.add(headLabel);

        //////////////////////注册事件监听器////////////////////////
        //取消按钮监听事件处理
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                RegisterFrame.this.dispose();
            }
        });
        //关闭窗口
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                RegisterFrame.this.dispose();
            }
        });

        // 重置按钮监听事件处理
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                txtUserName.setText("");
                pwdUserPassword.setText("");
                pwdConfirmPass.setText("");
                txtUserName.requestFocusInWindow();//用户名获得焦点
                txtAge.setText("");
                txtEmail.setText("");
            }
        });

        //确认按钮监听事件处理
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (pwdUserPassword.getPassword().length==0 || pwdUserPassword.getPassword().length==0) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "带 “ * ” 为必填内容!");
                    //判断用户名和密码是否为空
                } else if (!new String(pwdUserPassword.getPassword()).equals(new String(pwdUserPassword.getPassword()))) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "两次输入密码不一致!");
                    pwdUserPassword.setText("");
                    pwdConfirmPass.setText("");
                    pwdUserPassword.requestFocusInWindow();
                    //判断两次密码是否一致
                } else {
                    User user = new User(new String(pwdUserPassword.getPassword()),
                            txtUserName.getText(),
                            rbtnMale.isSelected() ? 'm' : 'f',
                            comboBox.getSelectedIndex());
                    try {
                        RegisterFrame.this.register(user);
                    } catch (IOException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
    //注册方法
    private void register(User user) throws IOException, ClassNotFoundException{
        Request request = new Request();
        request.setAction("userRegister");
        request.setAttribute("user", user);

        //获取响应
        Response response = ClientUtil.sendTextRequest(request);

        ResponseStatus status = response.getStatus();
        if (status == ResponseStatus.OK) {
            User user2 = (User) response.getData("user");
            JOptionPane.showMessageDialog(RegisterFrame.this,
                    "恭喜您，您的账号:" + user2.getId() + ",请牢记!!!",
                    "注册成功", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(RegisterFrame.this,
                    "注册失败，请稍后再试！！！", "服务器内部错误！", JOptionPane.ERROR_MESSAGE);
        }
    }
}
