package N422chat.bean;

import java.net.Socket;

public class ClientBean {
    private String name;    // �û�������
    private Socket socket;    // ���û���������׽���

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
