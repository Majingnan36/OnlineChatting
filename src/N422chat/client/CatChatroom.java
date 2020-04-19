package N422chat.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import N422chat.bean.PacketBean;
import N422chat.util.CatUtil;

class CellRenderer extends JLabel implements ListCellRenderer {
    CellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// ������Ϊ5�Ŀհױ߿�

        if (value != null) {
            setText(value.toString());
            setIcon(new ImageIcon("images//1.jpg"));
        }
        if (isSelected) {
            setBackground(new Color(255, 255, 153));// ���ñ���ɫ
            setForeground(Color.black);
        } else {
            // ����ѡȡ��ȡ��ѡȡ��ǰ���뱳����ɫ.
            setBackground(Color.white); // ���ñ���ɫ
            setForeground(Color.black);
        }
        setEnabled(list.isEnabled());
        setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
        setOpaque(true);
        return this;
    }
}

class UUListModel extends AbstractListModel {

    private static final long serialVersionUID = 1L;
    private Vector vs;

    public UUListModel(Vector vs) {
        this.vs = vs;
    }

    @Override
    public Object getElementAt(int index) {
        return vs.get(index);
    }

    @Override
    public int getSize() {
        return vs.size();
    }

}

public class CatChatroom extends JFrame {

    private static final long serialVersionUID = 6129126482250125466L;

    private static JPanel contentPane;
    private static Socket clientSocket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static String name;
    private static JTextArea textArea;
    private static AbstractListModel listmodel;
    private static JList list;
    private static String filePath;
    private static JLabel lblNewLabel;
    private static JProgressBar progressBar;
    private static Vector onlines;
    private static boolean isSendFile = false;
    private static boolean isReceiveFile = false;

    // ����
    private static File file, file2;
    private static URL cb, cb2;
    private static AudioClip aau, aau2;

    /**
     * ����������
     */

    @SuppressWarnings("deprecation")
    public CatChatroom(String u_name, Socket client) {
        // ��ֵ
        name = u_name;
        clientSocket = client;
        onlines = new Vector();

        SwingUtilities.updateComponentTreeUI(this);

        try {
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }

        setTitle(name);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(200, 100, 688, 510);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }

        };
        contentPane.setBackground(Color.LIGHT_GRAY);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // ������Ϣ��ʾ����
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 410, 300);
        getContentPane().add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);// �����Զ����й���
        textArea.setWrapStyleWord(true);// ������в����ֹ���
        textArea.setFont(new Font("sdf", Font.BOLD, 13));
        scrollPane.setViewportView(textArea);

        // ��������
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 347, 411, 97);
        getContentPane().add(scrollPane_1);

        final JTextArea textArea_1 = new JTextArea();
        textArea_1.setLineWrap(true);// �����Զ����й���
        textArea_1.setWrapStyleWord(true);// ������в����ֹ���
        scrollPane_1.setViewportView(textArea_1);

        // �رհ�ť
        final JButton btnNewButton = new JButton("\u5173\u95ED");
        btnNewButton.setBounds(572, 432, 85, 30);
        getContentPane().add(btnNewButton);

        // ���Ͱ�ť
        JButton btnNewButton_1 = new JButton("\u53D1\u9001");
        btnNewButton_1.setBounds(452, 432, 85, 30);
        getRootPane().setDefaultButton(btnNewButton_1);
        getContentPane().add(btnNewButton_1);

        // ���߿ͻ��б�
        listmodel = new UUListModel(onlines);
        list = new JList(listmodel);
        list.setCellRenderer(new CellRenderer());
        list.setOpaque(false);
        Border etch = BorderFactory.createEtchedBorder();
        list.setBorder(BorderFactory.createTitledBorder(etch, "���ߵ���:", TitledBorder.LEADING, TitledBorder.TOP,
                new Font("sdf", Font.BOLD, 18), Color.red));

        JScrollPane scrollPane_2 = new JScrollPane(list);
        scrollPane_2.setBounds(430, 10, 245, 375);
        scrollPane_2.setOpaque(false);
        scrollPane_2.getViewport().setOpaque(false);
        getContentPane().add(scrollPane_2);

        // �ļ�������
        progressBar = new JProgressBar();
        progressBar.setBounds(430, 390, 245, 15);
        progressBar.setMinimum(1);
        progressBar.setMaximum(100);
        getContentPane().add(progressBar);

        // �ļ�������ʾ
        lblNewLabel = new JLabel("");
        lblNewLabel.setFont(new Font("SimSun", Font.PLAIN, 12));
        lblNewLabel.setBackground(Color.WHITE);
        lblNewLabel.setBounds(430, 410, 245, 15);
        getContentPane().add(lblNewLabel);

        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            // ��¼���߿ͻ�����Ϣ��catbean�У������͸�������
            PacketBean bean = new PacketBean();
            bean.setType(0);
            bean.setName(name);
            bean.setTimer(CatUtil.getTimer());
            oos.writeObject(bean);
            oos.flush();

            // ��Ϣ��ʾ����
            file = new File("sounds\\dong.wav");
            cb = file.toURL();
            aau = Applet.newAudioClip(cb);
            // ������ʾ����
            file2 = new File("sounds\\ding.wav");
            cb2 = file2.toURL();
            aau2 = Applet.newAudioClip(cb2);

            // �����ͻ������߳�
            new ClientInputThread().start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // ���Ͱ�ť
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String info = textArea_1.getText();
                List to = list.getSelectedValuesList();

                if (to.size() < 1) {
                    PacketBean clientBean = new PacketBean();
                    clientBean.setType(5);
                    clientBean.setName(name);
                    String time = CatUtil.getTimer();
                    clientBean.setTimer(time);
                    clientBean.setInfo(info);
                    HashSet set = new HashSet();
                    set.addAll(onlines);
                    clientBean.setClients(set);
                    sendMessage(clientBean);
                    textArea_1.setText(null);
                    textArea_1.requestFocus();
                } else if (info.equals("")) {
                    JOptionPane.showMessageDialog(getContentPane(), "���ܷ��Ϳ���Ϣ");
                    return;
                } else if (to.toString().contains(name + "(��)")) {
                    JOptionPane.showMessageDialog(getContentPane(), "���ܸ��Լ�������Ϣ");
                    return;
                } else if (!to.toString().contains(name + "(��)")) {
                    PacketBean clientBean = new PacketBean();
                    clientBean.setType(1);
                    clientBean.setName(name);
                    String time = CatUtil.getTimer();
                    clientBean.setTimer(time);
                    clientBean.setInfo(info);
                    HashSet set = new HashSet();
                    set.addAll(to);
                    clientBean.setClients(set);

                    // �Լ���������ҲҪ��ʵ���Լ�����Ļ����
                    textArea.append(time + " �Ҷ�" + to + "˵:\r\n" + info + "\r\n");

                    sendMessage(clientBean);
                    textArea_1.setText(null);
                    textArea_1.requestFocus();
                }

            }
        });

        // �رհ�ť
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSendFile || isReceiveFile) {
                    JOptionPane.showMessageDialog(contentPane,
                            "�ļ������У����Ե�...", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    btnNewButton.setEnabled(false);
                    PacketBean clientBean = new PacketBean();
                    clientBean.setType(-1);
                    clientBean.setName(name);
                    clientBean.setTimer(CatUtil.getTimer());
                    sendMessage(clientBean);
                }
            }
        });

        // �뿪
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isSendFile || isReceiveFile) {
                    JOptionPane.showMessageDialog(contentPane, "�ļ������У����Ե�...", "Error Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    int result = JOptionPane.showConfirmDialog(getContentPane(), "��ȷ��Ҫ�뿪������");
                    if (result == 0) {
                        PacketBean clientBean = new PacketBean();
                        clientBean.setType(-1);
                        clientBean.setName(name);
                        clientBean.setTimer(CatUtil.getTimer());
                        sendMessage(clientBean);
                    }
                }
            }
        });

        // �б����
        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                List to = list.getSelectedValuesList();
                if (e.getClickCount() == 2) {

                    if (to.toString().contains(name + "(��)")) {
                        JOptionPane.showMessageDialog(getContentPane(), "�������Լ������ļ�");
                        return;
                    }

                    // ˫�����ļ��ļ�ѡ���
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("ѡ���ļ���");
                    chooser.showDialog(getContentPane(), "ѡ��");

                    // �ж��Ƿ�ѡ�����ļ�
                    if (chooser.getSelectedFile() != null) {
                        // ��ȡ·��
                        filePath = chooser.getSelectedFile().getPath();
                        File file = new File(filePath);
                        // �ļ�Ϊ��
                        if (file.length() == 0) {
                            JOptionPane.showMessageDialog(getContentPane(), filePath + "�ļ�Ϊ��,��������.");
                            return;
                        }

                        PacketBean clientBean = new PacketBean();
                        clientBean.setType(2);// �������ļ�
                        clientBean.setSize(new Long(file.length()).intValue());
                        clientBean.setName(name);
                        clientBean.setTimer(CatUtil.getTimer());
                        clientBean.setFileName(file.getName()); // ��¼�ļ�������
                        clientBean.setInfo("�������ļ�");

                        // �ж�Ҫ���͸�˭
                        HashSet<String> set = new HashSet<String>();
                        set.addAll(list.getSelectedValuesList());
                        clientBean.setClients(set);
                        sendMessage(clientBean);
                    }
                }
            }
        });

    }

    class ClientInputThread extends Thread {

        @Override
        public void run() {
            try {
                // ��ͣ�Ĵӷ�����������Ϣ
                while (true) {
                    ois = new ObjectInputStream(clientSocket.getInputStream());
                    final PacketBean bean = (PacketBean) ois.readObject();
                    switch (bean.getType()) {
                        case -1: {

                            return;
                        }
                        case 0: {
                            // �����б�
                            onlines.clear();
                            HashSet<String> clients = bean.getClients();
                            Iterator<String> it = clients.iterator();
                            while (it.hasNext()) {
                                String ele = it.next();
                                if (name.equals(ele)) {
                                    onlines.add(ele + "(��)");
                                } else {
                                    onlines.add(ele);
                                }
                            }

                            listmodel = new UUListModel(onlines);
                            list.setModel(listmodel);
                            aau2.play();
                            textArea.append(bean.getInfo() + "\r\n");
                            textArea.selectAll();
                            break;
                        }

                        case 1: {

                            String info = bean.getTimer() + "  " + bean.getName() + " �� " + bean.getClients() + "˵:\r\n";
                            if (info.contains(name)) {
                                info = info.replace(name, "��");
                            }
                            aau.play();
                            textArea.append(info + bean.getInfo() + "\r\n");
                            textArea.selectAll();
                            break;
                        }

                        case 2: {
                            // ���ڵȴ�Ŀ��ͻ�ȷ���Ƿ�����ļ��Ǹ�����״̬�������������̴߳���
                            new Thread() {
                                public void run() {
                                    // ��ʾ�Ƿ�����ļ��Ի���
                                    int result = JOptionPane.showConfirmDialog(getContentPane(), bean.getInfo());
                                    switch (result) {
                                        case 0: { // �����ļ�
                                            JFileChooser chooser = new JFileChooser();
                                            chooser.setDialogTitle("�����ļ���"); // ����
                                            // Ĭ���ļ����ƻ��з��ڵ�ǰĿ¼��
                                            chooser.setSelectedFile(new File(bean.getFileName()));
                                            chooser.showDialog(getContentPane(), "����"); // ��ť������
                                            // ����·��
                                            String saveFilePath = chooser.getSelectedFile().toString();

                                            // �����ͻ�CatBean
                                            PacketBean clientBean = new PacketBean();
                                            clientBean.setType(3);
                                            clientBean.setName(name); // �����ļ��Ŀͻ�����
                                            clientBean.setTimer(CatUtil.getTimer());
                                            clientBean.setFileName(saveFilePath);
                                            clientBean.setInfo("ȷ�������ļ�");

                                            // �ж�Ҫ���͸�˭
                                            HashSet<String> set = new HashSet<String>();
                                            set.add(bean.getName());
                                            clientBean.setClients(set); // �ļ���Դ
                                            clientBean.setTo(bean.getClients());// ����Щ�ͻ������ļ�

                                            // �����µ�socket ��������
                                            try {
                                                ServerSocket ss = new ServerSocket(0); // 0���Ի�ȡ���еĶ˿ں�

                                                clientBean.setIp(clientSocket.getInetAddress().getHostAddress());
                                                clientBean.setPort(ss.getLocalPort());
                                                sendMessage(clientBean); // ��ͨ�����������߷��ͷ�,
                                                // �����ֱ�ӷ����ļ�����������...

                                                isReceiveFile = true;
                                                // �ȴ��ļ���Դ�Ŀͻ��������ļ�....Ŀ��ͻ��������϶�ȡ�ļ�����д�ڱ�����
                                                Socket sk = ss.accept();
                                                textArea.append(CatUtil.getTimer() + "  " + bean.getFileName() + "�ļ�������.\r\n");
                                                DataInputStream dis = new DataInputStream( // �������϶�ȡ�ļ�
                                                        new BufferedInputStream(sk.getInputStream()));
                                                DataOutputStream dos = new DataOutputStream( // д�ڱ�����
                                                        new BufferedOutputStream(new FileOutputStream(saveFilePath)));

                                                int count = 0;
                                                int num = bean.getSize() / 100;
                                                int index = 0;
                                                while (count < bean.getSize()) {
                                                    int t = dis.read();
                                                    dos.write(t);
                                                    count++;

                                                    if (num > 0) {
                                                        if (count % num == 0 && index < 100) {
                                                            progressBar.setValue(++index);
                                                        }
                                                        lblNewLabel.setText("���ؽ���:" + count + "/" + bean.getSize() + "  ����" + index + "%");
                                                    } else {
                                                        lblNewLabel.setText("���ؽ���:" + count + "/" + bean.getSize() + "  ����:" +
                                                                new Double(new Double(count).doubleValue() / new Double(bean.getSize()).doubleValue() * 100).intValue() + "%");
                                                        if (count == bean.getSize()) {
                                                            progressBar.setValue(100);
                                                        }
                                                    }

                                                }

                                                // ���ļ���Դ�ͻ�������ʾ���ļ��������
                                                PrintWriter out = new PrintWriter(sk.getOutputStream(), true);
                                                out.println(CatUtil.getTimer() + " ���͸�" + name + "���ļ�[" + bean.getFileName() + "]" + "�ļ��������.\r\n");
                                                out.flush();
                                                dos.flush();
                                                dos.close();
                                                out.close();
                                                dis.close();
                                                sk.close();
                                                ss.close();
                                                textArea.append(CatUtil.getTimer() + "  " + bean.getFileName() + "�ļ��������.���λ��Ϊ:" + saveFilePath + "\r\n");
                                                isReceiveFile = false;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            break;
                                        }
                                        default: { // ȡ�������ļ�
                                            PacketBean clientBean = new PacketBean();
                                            clientBean.setType(4);
                                            clientBean.setName(name); // �����ļ��Ŀͻ�����
                                            clientBean.setTimer(CatUtil.getTimer());
                                            clientBean.setFileName(bean.getFileName());
                                            clientBean.setInfo(CatUtil.getTimer() + "  " + name + "ȡ�������ļ�[" + bean.getFileName() + "]");

                                            // �ж�Ҫ���͸�˭
                                            HashSet<String> set = new HashSet<String>();
                                            set.add(bean.getName());
                                            clientBean.setClients(set); // �ļ���Դ
                                            clientBean.setTo(bean.getClients());// ����Щ�ͻ������ļ�

                                            sendMessage(clientBean);

                                            break;

                                        }
                                    }
                                }

                                ;
                            }.start();
                            break;
                        }
                        case 3: { // Ŀ��ͻ�Ը������ļ���Դ�ͻ���ʼ��ȡ�����ļ������͵�������
                            textArea.append(bean.getTimer() + "  " + bean.getName() + "ȷ�������ļ�" + ",�ļ�������..\r\n");
                            new Thread() {
                                public void run() {

                                    try {
                                        isSendFile = true;
                                        // ����Ҫ�����ļ��Ŀͻ��׽���
                                        Socket s = new Socket(bean.getIp(), bean.getPort());
                                        DataInputStream dis = new DataInputStream(new FileInputStream(filePath)); // ���ض�ȡ�ÿͻ��ղ�ѡ�е��ļ�
                                        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream())); // ����д���ļ�

                                        int size = dis.available();
                                        System.out.println("size = " + size);

                                        int count = 0; // ��ȡ����
                                        int num = size / 100;
                                        int index = 0;
                                        while (count < size) {

                                            int t = dis.read();
                                            dos.write(t);
                                            count++; // ÿ��ֻ��ȡһ���ֽ�

                                            if (num > 0) {
                                                if (count % num == 0 && index < 100) {
                                                    progressBar.setValue(++index);

                                                }
                                                lblNewLabel.setText("�ϴ�����:" + count + "/" + size + "  ����" + index + "%");
                                            } else {
                                                lblNewLabel.setText("�ϴ�����:" + count + "/" + size + "  ����:" + new Double(new Double(count).doubleValue() / new Double(size).doubleValue() * 100).intValue() + "%");
                                                if (count == size) {
                                                    progressBar.setValue(100);
                                                }
                                            }
                                        }
                                        dos.flush();
                                        dis.close();
                                        // ��ȡĿ��ͻ�����ʾ������ϵ���Ϣ...
                                        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                        textArea.append(br.readLine() + "\r\n");
                                        isSendFile = false;
                                        br.close();
                                        s.close();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }

                                ;
                            }.start();
                            break;
                        }
                        case 4: {
                            textArea.append(bean.getInfo() + "\r\n");
                            break;
                        }
                        case 5: {

                            String info = bean.getTimer() + "  " + bean.getName() + " �� " + "ȫ���Ա " + "˵:\r\n";
                            if (info.contains(name)) {
                                info = info.replace(name, "��");
                            }
                            aau.play();
                            textArea.append(info + bean.getInfo() + "\r\n");
                            textArea.selectAll();
                            break;
                        }
                        default: {
                            break;
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        }
    }

    private void sendMessage(PacketBean clientBean) {
        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(clientBean);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
