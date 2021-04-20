package client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TCPClientThreadDelete implements Runnable {
    private final JListEvent JListEvent;
    private final int port;
    private final String SIPAddress;
    private final ArrayList<JListClient> clients  ;
    ServerSocket welcomeSocket;
    public TCPClientThreadDelete(JListEvent JListEvent, String SIPAddress, int port ) {
        if (JListEvent == null) {
            throw new RuntimeException("JList Event can't be null");
        }
        this.JListEvent = JListEvent;
        this.port = port;
        this.SIPAddress = SIPAddress;
        System.out.println("TCPClientThreadDelete");
        clients = new ArrayList< >();

    }

            @Override
            public void run() {
                try {
                      welcomeSocket = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                    while (true) {
                        Socket connectionSocket = null;

                        try {
                          connectionSocket = welcomeSocket.accept();
                            DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                            System.out.println("BufferedReader");


                            JSONObject jsonObjectDeleteUser = new JSONObject();
                            jsonObjectDeleteUser.put("DeleteUser", Boolean.TRUE);
                            outToServer.writeBytes(jsonObjectDeleteUser + "\n");
                            String modifiedSentenceDeleteUser = inFromServer.readLine();
                            JSONParser parserDeleteUser = new JSONParser();
                            JSONObject returnJsonDeleteUser = (JSONObject) parserDeleteUser.parse(modifiedSentenceDeleteUser);
                            clients.clear();

                            JSONArray clientsJSONArrayDeleteUser= (JSONArray) returnJsonDeleteUser.get("DeleteUser");
                            if (clientsJSONArrayDeleteUser != null) {
                                for (Object o : clientsJSONArrayDeleteUser) {
                                    if (o != null) {
                                        JSONObject client = (JSONObject) o;
                                        System.out.println(  (String) client.get("IPAddress"));
                                        clients.remove(new JListClient((String) client.get("IPAddress"), (long) client.get("Port"), (String) client.get("UserName")));
                                        JListEvent.onReceivedJlist(clients);

                                    }
                                }
                            }


                            connectionSocket.close();
                    }
                 catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }



}
}