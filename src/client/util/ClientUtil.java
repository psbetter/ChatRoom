package client.util;

import client.model.DataBuffer;
import client.view.ChatFrame;
import common.model.entity.Request;
import common.model.entity.Response;
import common.model.entity.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * 〈客户端发送请求到服务器的工具〉<br>
 * 〈〉
 *
 */

public class ClientUtil {

    /** 发送请求对象,主动接收响应 */
    public static Response sendTextRequest(Request request) throws IOException {
        Response response = null;
        try {
            // 发送请求
            DataBuffer.oos.writeObject(request);
            DataBuffer.oos.flush();
            System.out.println("[Info] 客户端发送了请求对象:" + request.getAction());

            if(!"exit".equals(request.getAction())){
                // 获取响应
                response = (Response) DataBuffer.ois.readObject();
                System.out.println("[Info] 客户端获取到了响应对象:" + response.getStatus());
            }else{
                System.out.println("[Info] 客户端断开连接了");
            }
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return response;
    }

    /** 发送请求对象,不主动接收响应 */
    public static void sendTextRequest2(Request request) throws IOException {
        try {
            DataBuffer.oos.writeObject(request); // 发送请求
            DataBuffer.oos.flush();
            System.out.println("[Info] 客户端发送了请求对象:" + request.getAction());
        } catch (IOException e) {
            throw e;
        }
    }

    /** 把指定文本添加到消息列表文本域中 */
    public static void appendTxt2MsgListArea(String txt) {
        JLabel txtArea = new JLabel();
        txtArea.setPreferredSize(new Dimension(750, 30));
        txtArea.setText(txt);
        ChatFrame.taUserMessage.add(txtArea);
        int count = ChatFrame.taUserMessage.getComponentCount();
        if(count > 10){
            ChatFrame.ChatAreaHeight += 30;
            ChatFrame.taUserMessage.setPreferredSize(new Dimension(750,ChatFrame.ChatAreaHeight));
            JScrollBar jscrollBar = ChatFrame.jsp.getVerticalScrollBar();
            if (jscrollBar != null)
                jscrollBar.setValue(jscrollBar.getMaximum());
            ChatFrame.jsp.revalidate();
        }
        ChatFrame.taUserMessage.revalidate();
//        SwingUtilities.updateComponentTreeUI(ChatFrame.jsp);
//        ChatFrame.taUserMessage.updateUI();
    }

    public static void appendTxt2MsgListArea(String txt, User user) {
        user.addRecord(txt);
        JTextArea txtArea = new JTextArea();
        txtArea.setLineWrap(true);        //激活自动换行功能
        txtArea.setWrapStyleWord(true);            // 激活断行不断字功能
        txtArea.setText(txt);
        int l = txt.length() / 55;
//        System.out.println("txt:"+ txt);
//        System.out.println(txt.length());
        txtArea.setPreferredSize(new Dimension(700, 30 + l*30));

        ChatFrame.taUserMessage.add(txtArea);
        DataBuffer.msgAreaHeight = DataBuffer.msgAreaHeight + 30 + l*30;
        if(DataBuffer.msgAreaHeight >= 360){
            ChatFrame.ChatAreaHeight += 30 + l*30;
            ChatFrame.taUserMessage.setPreferredSize(new Dimension(750,ChatFrame.ChatAreaHeight));
            JScrollBar jscrollBar = ChatFrame.jsp.getVerticalScrollBar();
            if (jscrollBar != null)
                jscrollBar.setValue(jscrollBar.getMaximum());
            ChatFrame.jsp.revalidate();
        }
        ChatFrame.taUserMessage.revalidate();
//        SwingUtilities.updateComponentTreeUI(ChatFrame.jsp);
//        ChatFrame.taUserMessage.updateUI();
    }
    public static void appendEmo2MsgListArea(String txt, String emo, User user){
        user.addRecord(txt);
//        System.out.println("[Info] 聊天记录更新："+user.getChatRecords());
        JTextArea txtArea = new JTextArea();
        txtArea.setPreferredSize(new Dimension(360, 30));
        txtArea.setText(txt);
        JLabel jl = new JLabel();
        jl.setPreferredSize(new Dimension(50, 30));
        jl.setIcon(new ImageIcon(emo));
        jl.setBorder(BorderFactory.createEmptyBorder());
        ChatFrame.taUserMessage.add(txtArea);
        ChatFrame.taUserMessage.add(jl);
        DataBuffer.msgAreaHeight = DataBuffer.msgAreaHeight + 30;
        if(DataBuffer.msgAreaHeight >= 360){ // 大于消息栏高就扩充消息栏面板
            ChatFrame.ChatAreaHeight += 30;
            ChatFrame.taUserMessage.setPreferredSize(new Dimension(750,ChatFrame.ChatAreaHeight));
            JScrollBar jscrollBar = ChatFrame.jsp.getVerticalScrollBar();
            if (jscrollBar != null)
                jscrollBar.setValue(jscrollBar.getMaximum());
            ChatFrame.jsp.revalidate();
        }
        ChatFrame.taUserMessage.revalidate();
    }
}
