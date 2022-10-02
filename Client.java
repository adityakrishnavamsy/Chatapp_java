import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

import java.io.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    // Declare component
    private JLabel heading = new JLabel("client area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("sneding request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("connection done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            CreateGUI();
            handleEvents();
            startReading();
            // startWriting();
            // as handle events part is not handling the write part we are not using it

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // when we release enter then need to hadle the event
                // System.out.println("key released " + e.getKeyCode());

                if (e.getKeyCode() == 10) {
                    // System.out.println("pressed enter");
                    String contentToSend = messageInput.getText();
                    // if i want to send message then we text field
                    messageArea.append("Client :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText(" ");
                    messageInput.requestFocus();
                }

            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

        });
    }

    private void CreateGUI() {
        this.setTitle("Client Messager[end]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setIcon(new ImageIcon("naruto.jpg"));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // so that i can edit the message are which is on the screen
        messageArea.setEditable(false);
        // setting the fame layout
        this.setLayout(new BorderLayout());
        // adding the components to the frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {
        // thread will keep reading and giving the data
        Runnable r1 = () -> {
            System.out.println("reader started..");

            // as we need to keep reading
            try {
                while (true && !socket.isClosed()) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    // System.out.println("Server: " + msg);
                    messageArea.append("Server: " + msg + "\n");

                }

            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }

        };

        // start thread
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread - data user se lega and the send karega client tak
        Runnable r2 = () -> {
            System.out.println("Writer started..");
            try {
                while (true && !socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    // we need to tak the input from the user
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                }
            } catch (Exception e) {
                // e.printStackTrace();
                // to print the technical information
                System.out.println("Connection is closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }
}
