package server;

import server.controller.RequestProcessor;
import server.model.DataBuffer;
import server.view.ServerInfoFrame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String[] args){
        int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
        InetAddress address;
        //初始化服务器套节字
        try {
            DataBuffer.serverSocket = new ServerSocket(port);
            address = InetAddress.getLocalHost();
            DataBuffer.ip = address.getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
            address = null;
        }

        new Thread(new Runnable() {//启动新线程进行客户端连接监听
            public void run() {
                try {
                    while (true) {
                        // 监听客户端的连接
                        Socket socket = DataBuffer.serverSocket.accept();
                        DataBuffer.serverLog.append("[Info] "
                                + socket.getInetAddress().getHostAddress()
                                + ":" + socket.getPort() + "请求连接\n");

                        //针对每个客户端启动一个线程，在线程中调用请求处理器来处理每个客户端的请求
                        new Thread(new RequestProcessor(socket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //启动服务器监控窗体
        new ServerInfoFrame(address);
    }
}
