package client;

import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientTCPGUI {
    static boolean logout = false;
    static boolean login = false;
    public ArrayList<JListClient> clients;
    private JList listUsers;
    private JTextField textFieldUsername;
    private JButton buttonLogin;
    private JButton buttonLogout;
    private JTextArea textAreaSend;
    private JTextArea textAreaMessage;
    private JButton sendButton;
    private JTextField textFieldServerIP;
    private JTextField textFieldServerPort;
    private JTextField textFieldLocalIP;
    private JPanel jPanelMain;
    private JTextField textFieldLocalPort;
    private JTextField textFieldRemoteIP;
    private JTextField textFieldRemotePort;

    ClientTCPGUI() {
        clients = new ArrayList<>();

        textFieldServerIP.setText("127.0.0.1");
        textFieldLocalIP.setText("127.0.0.1");
        textFieldServerPort.setText("8888");
        textFieldLocalPort.setText("3333");
        textFieldUsername.setText("wasim");




        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!logout) {
                    logout = true;
                    login = false;
                    try {
                        System.out.println("Logout");

                        Socket clientSocket = new Socket(textFieldServerIP.getText(), Integer.parseInt(textFieldServerPort.getText()));
                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Logout", Boolean.TRUE);
                        jsonObject.put("DeleteUser", Boolean.TRUE);
                        jsonObject.put("UserName", textFieldUsername.getText());
                        jsonObject.put("IPAddress", textFieldLocalIP.getText());
                        jsonObject.put("Port", Integer.parseInt(textFieldLocalPort.getText()));
                        outToServer.writeBytes(jsonObject + "\n");
                        clientSocket.close();
                        clients.remove(new JListClient(textFieldLocalIP.getText(),Long.parseLong(textFieldLocalPort.getText()),textFieldUsername.getText()));
                        for (JListClient client : clients) {
                            System.out.println("buttonLogout : "+client.getUserName());
                        }
                        listUsers.setModel(new DefaultListModel());
                    } catch (IOException | NumberFormatException ignored) {
                    }
                }
            }
        });
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!login) {
                    try {
                        logout = false;
                        login = true;

                        if (!textFieldServerIP.getText().isEmpty() && !textFieldServerPort.getText().isEmpty()) {
                            Socket clientSocket = new Socket(textFieldServerIP.getText(), Integer.parseInt(textFieldServerPort.getText()));
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Login", Boolean.TRUE);
                            jsonObject.put("UserName", textFieldUsername.getText());
                            jsonObject.put("IPAddress", textFieldLocalIP.getText());
                            jsonObject.put("Port", Integer.parseInt(textFieldLocalPort.getText()));
                            outToServer.writeBytes(jsonObject + "\n");

                            clientSocket.close();


                            CJListEvent cJListEvent = new CJListEvent();

                            cJListEvent.onReceivedJlist(clients);
                            TCPClientThread tcpClientThread= new TCPClientThread(cJListEvent,textFieldServerPort.getText(),Integer.parseInt(textFieldLocalPort.getText()));
                             new Thread(tcpClientThread).start();
                            /*TCPClientThreadDelete tcpClientThreadDelete= new TCPClientThreadDelete(cJListEvent,textFieldLocalIP.getText(),Integer.parseInt(textFieldLocalPort.getText()));
                            new Thread(tcpClientThreadDelete).start();*/

                            listUsers.addListSelectionListener(new ListSelectionListener() {
                                @Override
                                public void valueChanged(ListSelectionEvent e) {
                                    if (!e.getValueIsAdjusting()) {
                                        String dataUserName = listUsers.getSelectedValue().toString();
                                        String[] arrOfStr = dataUserName.split("-");
                                        if (!(arrOfStr[0].equals(textFieldUsername.getText()))) {
                                            textFieldRemoteIP.setText(arrOfStr[1]);
                                            textFieldRemotePort.setText(arrOfStr[2]);
                                        }

                                    }


                                }
                            });

                        }
                    } catch (UnknownHostException unknownHostException) {
                        unknownHostException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                }
            }
        });


    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Client Chat");
        jFrame.setMinimumSize(new Dimension(850, 300));
        //  jFrame.setMaximumSize(new Dimension(600, 300));
        //  jFrame.setPreferredSize(new Dimension(600, 300));
        try {
            jFrame.setContentPane(new ClientTCPGUI().jPanelMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        /*jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!logout) {
                    try {

                        System.out.println("windowClosing");

                        Socket clientSocket = new Socket("127.0.0.1", 8888);
                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Logout", Boolean.TRUE);
                        jsonObject.put("UserName", "wasim");
                        jsonObject.put("IPAddress", "127.0.0.1");
                        jsonObject.put("Port", 5555);
                        outToServer.writeBytes(jsonObject + "\n");
                        String modifiedSentence = inFromServer.readLine();
                        System.out.println(modifiedSentence);
                        clientSocket.close();
                    } catch (IOException | NumberFormatException ignored) {
                    }
                }
                super.windowClosing(e);
            }
        });*/
    }

 /*   private List<JListClient> getUsers() {
        new Thread(() -> {
            try {


                Socket clientSocket = new Socket(textFieldServerIP.getText(), Integer.parseInt(textFieldServerPort.getText()));
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (true) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Select", Boolean.TRUE);
                    outToServer.writeBytes(jsonObject + "\n");
                    String modifiedSentence = inFromServer.readLine();
                    JSONParser parser = new JSONParser();
                    JSONObject returnJson = (JSONObject) parser.parse(modifiedSentence);
                    JSONArray clientsJSONArray = (JSONArray) returnJson.get("Clients");
                    clients = new ArrayList<>();
                    if (clientsJSONArray != null) {
                        for (Object o : clientsJSONArray) {
                            if (o != null) {
                                JSONObject client = (JSONObject) o;
                                clients.add(new JListClient((String) client.get("IPAddress"), (long) client.get("Port"), (String) client.get("UserName")));
                                //                              System.out.println(new listClient((String) client.get("IPAddress"), (long) client.get("Port"), (String) client.get("UserName")));
                                CJListEvent cJListEvent = new CJListEvent();
                                cJListEvent.onReceivedJlist(clients);
                            }
                        }
                    }
                    clientSocket.close();

                }
            } catch (IOException | NullPointerException | ParseException exception) {
                exception.printStackTrace();
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "Ports Number should" +
                        "be a number between (1024 - 65536)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
        return clients;
    }*/

    void Close() {

    }

    class CJListEvent implements JListEvent {

        @Override
        public void onReceivedJlist(ArrayList<JListClient> message) {

            listUsers.setListData(message.toArray());
        }
    }
}