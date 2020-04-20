package N422chat.login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import N422chat.client.MyChatroom;
import N422chat.bean.ClientBean;
import N422chat.util.MyUtil;

public class MyLogin extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    public static HashMap<String, ClientBean> onlines;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // ������½����
                    MyLogin frame = new MyLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyLogin() {
        setTitle("N422\u6B22\u8FCE\u4F60");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 250, 450, 300);
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        contentPane.setBackground(Color.LIGHT_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        textField = new JTextField();
        textField.setForeground(Color.BLACK);
        textField.setBounds(128, 107, 104, 24);
        textField.setOpaque(false);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setForeground(Color.BLACK);
        passwordField.setEchoChar('*');
        passwordField.setOpaque(false);
        passwordField.setBounds(128, 144, 104, 25);
        contentPane.add(passwordField);

        final JButton btnNewButton = new JButton();
        btnNewButton.setText("\u767B\u5F55");
        btnNewButton.setToolTipText("");
        btnNewButton.setBounds(106, 202, 72, 25);
        getRootPane().setDefaultButton(btnNewButton);
        contentPane.add(btnNewButton);

        final JButton btnNewButton_1 = new JButton();
        btnNewButton_1.setText("\u6CE8\u518C");
        btnNewButton_1.setBounds(205, 202, 72, 25);
        contentPane.add(btnNewButton_1);

        // ��ʾ��Ϣ
        final JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(246, 148, 151, 21);
        lblNewLabel.setForeground(Color.red);
        getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("\u7528\u6237\u540D\uFF1A");
        lblNewLabel_1.setBounds(42, 110, 72, 18);
        contentPane.add(lblNewLabel_1);

        JLabel label = new JLabel("\u5BC6\u7801\uFF1A");
        label.setBounds(42, 147, 72, 18);
        contentPane.add(label);

        JLabel lblNChatroom = new JLabel("N422 Chatroom");
        lblNChatroom.setFont(new java.awt.Font("����", java.awt.Font.BOLD, 25));
        lblNChatroom.setBounds(92, 31, 197, 45);
        contentPane.add(lblNChatroom);

        // ������½��ť
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                MyUtil.loadPro(userPro, file);
                String u_name = textField.getText();
                if (file.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        String u_pwd = new String(passwordField.getPassword());
                        if (u_pwd.equals(userPro.getProperty(u_name))) {

                            try {
                                Socket client = new Socket("localhost", 8888);

                                btnNewButton.setEnabled(false);
                                MyChatroom frame = new MyChatroom(u_name, client);
                                frame.setVisible(true);// ��ʾ�������
                                setVisible(false);// ���ص���½����

                            } catch (UnknownHostException e1) {
                                errorTip("ɧ���е��쳣�����µ�½����");
                            } catch (IOException e1) {
                                errorTip("�����쳣");
                            }

                        } else {
                            lblNewLabel.setText("���������������");
                            textField.setText("");
                            passwordField.setText("");
                            textField.requestFocus();
                        }
                    } else {
                        lblNewLabel.setText("�������ǳƲ����ڣ�");
                        textField.setText("");
                        passwordField.setText("");
                        textField.requestFocus();
                    }
                } else {
                    lblNewLabel.setText("�������ǳƲ����ڣ�");
                    textField.setText("");
                    passwordField.setText("");
                    textField.requestFocus();
                }
            }
        });

        //ע�ᰴť����
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton_1.setEnabled(false);
                MyRegister frame = new MyRegister();
                frame.setVisible(true);// ��ʾע�����
                setVisible(false);// ���ص���½����
            }
        });
    }

    protected void errorTip(String str) {
        JOptionPane.showMessageDialog(contentPane, str, "Error Message", JOptionPane.ERROR_MESSAGE);
        textField.setText("");
        passwordField.setText("");
        textField.requestFocus();
    }
}