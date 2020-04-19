package N422chat.bean;

import java.net.Socket;

public class ClientBean {
    private String name;    // 用户的姓名
    private Socket socket;    // 该用户所分配的套接字

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
