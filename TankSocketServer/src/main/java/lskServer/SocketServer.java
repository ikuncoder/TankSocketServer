package lskServer;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Properties;

import tank.Tank;
import tank.TankWorld;
import wingman.game.PlayerShip;

public class SocketServer {
    /*协议格式
     * 1/2+playerId+Location+direction+weapon+live+strength+score
     * */

    private static volatile int ConnectCount;
    private static ArrayList<Writer> writerAll;

    public static void main(String[] args) throws IOException {
        //读取properties的配置信息
        Properties properties = new Properties();
        try {
            InputStream inputStream = TankWorld.class.getResource("Resources/Port.properties").openStream();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String PORT = properties.getProperty("PORT");
        int port = Integer.parseInt(PORT);
        TankWorld tankWorld = TankWorld.getInstance();
        writerAll = new ArrayList<>();
        //如果不等待客户端连接，就可以把这个注释去掉
        //tankWorld.tankWorkStart();
        // 开启监听
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("server start");
        while (true) {
            System.out.println("等待客户端连接。。。");
            Socket socket = serverSocket.accept();
            System.out.println("客户端" + socket.getPort() + "已连接！");
            ConnectCount++;
            System.out.println("客户端连接数是：" + ConnectCount);
            // 为每一个连接的客户端创建一个线程
            new Thread(new Task(socket, writerAll)).start();
        }
    }

    public void sendHander(String msg) {
        String[] str = msg.split("\\+");
        if (str[0].equals("playerId")) {
            Writer writer = writerAll.get(Integer.parseInt(str[1]));
            try {
                writer.write(msg);
                writer.write("eof\n");
                writer.flush();
            } catch (Exception e) {
                System.out.println(e.getCause());
            }
        }else{
            for (Writer writer2 : writerAll) {// 广播
                try {
                    writer2.write(msg);
                    writer2.write("eof\n");
                    writer2.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class Task implements Runnable {
    private Socket socket;
    TankWorld tankWorld = TankWorld.getInstance();
    private ArrayList<Writer> writerAll;
    int j;

    public Task(Socket socket, ArrayList<Writer> writerAll) {
        this.socket = socket;
        this.writerAll = writerAll;
    }

    @Override
    public void run() {
        // 处理客户端请求的方法
        try {
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            writerAll.add(writer);
            j++;
            if (writerAll.size() ==2& j == 1) {//保证tankWorkStart在两个客户端连接上才执行，且只执行一次
                tankWorld.tankWorkStart();
            }
            while (true) {
                handlerSocket(writer, tankWorld);// 一直接受消息和广播消息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlerSocket(Writer writer, TankWorld tankWorld) throws IOException, Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String temp;
        int index;
        // 接收客户端数据
        while ((temp = br.readLine()) != null) {// temp==lefteof
            if ((index = temp.indexOf("eof")) != -1) { // 遇到eof时就结束接收
                sb.append(temp.substring(0, index));
                break;
            }
            sb.append(temp);
        }
        ListIterator<PlayerShip> players = tankWorld.getPlayers();// 获得ListIterator数量
        char FirstChar = sb.charAt(0);
        String PlayerId = sb.substring(1, 2);
        String OtherString = sb.substring(2);

        // 处理从客户端接收的指令
        if (FirstChar == '1') {
            while (players.hasNext()) {
                Tank player = (Tank) players.next();
                if (player.getName().equals(PlayerId)) {
                    // 如果是left，right，up,down指令
                    if (OtherString.equals("left")) {
                        player.left = 1;
                    } else if (OtherString.equals("right")) {
                        player.right = 1;
                    } else if (OtherString.equals("up")) {
                        player.up = 1;
                    } else if (OtherString.equals("down")) {
                        player.down = 1;
                    }
                }
            }
        } else if (FirstChar == '0') {
            while (players.hasNext()) {
                Tank player = (Tank) players.next();
                if (player.getName().equals(PlayerId)) {
                    if (OtherString.equals("left")) {
                        player.left = 0;
                    } else if (OtherString.equals("right")) {
                        player.right = 0;
                    } else if (OtherString.equals("up")) {
                        player.up = 0;
                    } else if (OtherString.equals("down")) {
                        player.down = 0;
                    }
                }
            }
        } else if (FirstChar == '*') {// 处理*%指令
            while (players.hasNext()) {
                Tank player = (Tank) players.next();
                if (player.getName().equals(PlayerId)) {
                    // 如果是开火
                    if (OtherString.equals("isFiring")) {
                        player.startFiring();
                    }
                }
            }
        } else if (FirstChar == '%') {//stop fire
            while (players.hasNext()) {
                Tank player = (Tank) players.next();
                if (player.getName().equals(PlayerId)) {
                    if (OtherString.equals("isFiring")) {
                        player.stopFiring();
                    }
                }
            }
        }

        // 客户端传入q时才断开连接
        if (sb.toString().equals("q")) {
            br.close();
            socket.close();
            System.out.println("Socket-[port:" + socket.getPort() + "] close");
        }

    }
}

	
	