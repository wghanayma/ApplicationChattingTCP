package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class TCPServerGUI {
    boolean RunServer = true;
    private JTextField textFieldPortServer;
    private JTextField textFieldIPServer;
    private JButton buttonRunServer;
    private JPanel jPanelMain;
    JList listClient;

    TCPServerGUI() {
        textFieldIPServer.setText("127.0.0.1");
        textFieldPortServer.setText("8888");
        buttonRunServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textFieldIPServer.getText().isEmpty()) {
                    if (!textFieldPortServer.getText().isEmpty()) {
                        TCPServerThread mTCPServerThread;
                        CJListEvent messageEvent = new CJListEvent();

                        mTCPServerThread = new TCPServerThread(messageEvent, textFieldIPServer.getText(), Integer.parseInt(textFieldPortServer.getText()));
                        new Thread(mTCPServerThread).start();

                        RunServer = false;
                        System.out.println("Run Server ");

                    } else {
                        JOptionPane.showMessageDialog(jPanelMain, "Please put  Server Port", "Server Port", JOptionPane.ERROR_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(jPanelMain, "Please put IP Server", "Server IP", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        textFieldIPServer.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyChar() == '.') {
                    textFieldIPServer.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(jPanelMain, "Enter only numeric digits(0-9) and '.'", "Server IP", JOptionPane.ERROR_MESSAGE);
                    textFieldIPServer.setText(null);

                }
            }
        });
        textFieldPortServer.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
                    textFieldPortServer.setEditable(true);


                } else {
                    JOptionPane.showMessageDialog(jPanelMain, "Enter only numeric digits(0-9)", "Server Port", JOptionPane.ERROR_MESSAGE);
                    textFieldPortServer.setText(null);

                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("TCP Server");

        jFrame.setContentPane(new TCPServerGUI().jPanelMain);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        //jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);

}
     class CJListEvent implements JListEvent {


         @Override
         public void onReceivedJlist(List<JListClient> message) {
             listClient.setListData(message.toArray());

         }
     }
}
