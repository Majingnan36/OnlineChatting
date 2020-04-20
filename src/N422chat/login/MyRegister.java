package N422chat.login;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import N422chat.util.MyUtil;

import java.awt.Font;

public class MyRegister extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JLabel lblNewLabel;
    private JLabel label;
    private JLabel label_1;
    private JLabel label_2;
    private JLabel lblNewLabel_1;

    public MyRegister() {
        setTitle("N422\u6B22\u8FCE\u4F60\u7684\u52A0\u5165   ps:\u5C0F\u59D0\u59D0\u4F18\u5148\r\n");
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
        textField.setBounds(155, 76, 104, 21);
        textField.setOpaque(false);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setOpaque(false);
        passwordField.setBounds(155, 117, 104, 21);
        contentPane.add(passwordField);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(155, 166, 104, 21);
        passwordField_1.setOpaque(false);
        contentPane.add(passwordField_1);

        //注册按钮
        final JButton btnNewButton_1 = new JButton();
        btnNewButton_1.setText("\u6CE8\u518C");
        btnNewButton_1.setBounds(196, 218, 78, 21);
        getRootPane().setDefaultButton(btnNewButton_1);
        contentPane.add(btnNewButton_1);

        //返回按钮
        final JButton btnNewButton = new JButton("\u8FD4\u56DE");
        btnNewButton.setBounds(81, 218, 72, 21);
        contentPane.add(btnNewButton);

        //提示信息
        lblNewLabel = new JLabel();
        lblNewLabel.setBounds(287, 167, 117, 20);
        lblNewLabel.setForeground(Color.red);
        contentPane.add(lblNewLabel);

        label = new JLabel("\u7528\u6237\u540D\uFF1A");
        label.setBounds(55, 78, 72, 18);
        contentPane.add(label);

        label_1 = new JLabel("\u8F93\u5165\u5BC6\u7801\uFF1A");
        label_1.setBounds(55, 119, 86, 18);
        contentPane.add(label_1);

        label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
        label_2.setBounds(55, 169, 86, 18);
        contentPane.add(label_2);

        lblNewLabel_1 = new JLabel("N422 Chatroom");
        lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 30));
        lblNewLabel_1.setBounds(91, 13, 266, 38);
        contentPane.add(lblNewLabel_1);

        //返回按钮监听
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnNewButton.setEnabled(false);
                //返回登陆界面
                MyLogin frame = new MyLogin();
                frame.setVisible(true);
                setVisible(false);
            }
        });

        //注册按钮监听
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Properties userPro = new Properties();
                File file = new File("Users.properties");
                MyUtil.loadPro(userPro, file);

                String u_name = textField.getText();
                String u_pwd = new String(passwordField.getPassword());
                String u_pwd_ag = new String(passwordField_1.getPassword());

                // 判断用户名是否在普通用户中已存在
                if (u_name.length() != 0) {

                    if (userPro.containsKey(u_name)) {
                        lblNewLabel.setText("用户名已存在!");
                    } else {
                        writePassword(userPro, file, u_name, u_pwd, u_pwd_ag);
                    }
                } else {
                    lblNewLabel.setText("用户名不能为空！");
                }
            }

            private void writePassword(Properties userPro,
                                       File file, String u_name, String u_pwd, String u_pwd_ag) {
                if (u_pwd.equals(u_pwd_ag)) {
                    if (u_pwd.length() != 0) {
                        userPro.setProperty(u_name, u_pwd_ag);
                        try {
                            userPro.store(new FileOutputStream(file), "Author by MaJingnan");
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        btnNewButton_1.setEnabled(false);
                        //返回登陆界面
                        MyLogin frame = new MyLogin();
                        frame.setVisible(true);
                        setVisible(false);
                    } else {
                        lblNewLabel.setText("密码为空！");
                    }
                } else {
                    lblNewLabel.setText("密码不一致！");
                }
            }
        });
    }
}
