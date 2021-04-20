package server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class TCPServerThread implements Runnable {
    static final Object criticalSection = new Object();
    private final int port;
    private final String SIPAddress;
    private final JListEvent JListEvent;
    public Socket socket;
    List<JSONClient> JSONClients;
    List<JListClient> JListClients;
    JSONObject sendToClientJson = new JSONObject();
    JSONArray jsonArray;
    boolean isStopped = false;
    //ServerThread mThread;
    private ServerSocket welcomeSocket;

    public TCPServerThread(JListEvent JListEvent, String SIPAddress, int port) {

        this.port = port;
        this.SIPAddress = SIPAddress;
        this.JListEvent = JListEvent;

        JSONClients = new ArrayList<>();
        JListClients = new ArrayList<>();
        jsonArray = new JSONArray();

    }


    @Override
    public void run() {
        synchronized (criticalSection) {
            String clientSentence;
            JSONObject sendToClient = new JSONObject();

            try {
                welcomeSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                Socket connectionSocket = null;
                try {
                    connectionSocket = welcomeSocket.accept();

                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    clientSentence = inFromClient.readLine();
                    JSONObject jsonObject = (JSONObject) JSONValue.parse(clientSentence);

                      /*      if (jsonObject.containsKey("Select")) {
                                JSONArray jsonArray = new JSONArray();
                                jsonArray.addAll(JSONClients);
                                sendToClient.put("Clients", jsonArray);
                                // System.out.println(sendToClient);
                                JListEvent.onReceivedJlist(JListClients);
                                outToClient.writeBytes(sendToClient + "\n");
                            }
*/
                    if (jsonObject.containsKey("Login")) {
                        String userName = (String) jsonObject.get("UserName");
                        String userIP = (String) jsonObject.get("IPAddress");
                        long userPort = (long) jsonObject.get("Port");
                        if (!checkIfIPExist(JSONClients, userIP) ||
                                !checkIfPortSameIPExist(JSONClients, userIP, userPort) ||
                                !checkIfUsernameExist(JSONClients, userName)) {
                            JSONClient JSONClientJson = new JSONClient(userIP, userPort, userName);
                            JSONClients.add(JSONClientJson);
                            JListClients.add(new JListClient(userIP, userPort, userName));
                            jsonArray.add(JSONClientJson);
                            System.out.println("jsonArray : "+jsonArray);
                            //  welcomeSocket.close();
                            connectionSocket.close();
                            for (Object c : jsonArray) {
                                JSONClient mJSONClient = (JSONClient) c;
                                JSONObject clientObject = new JSONObject();
                                clientObject.put("NewUser", jsonArray);
                                for (int i =0 ;i<jsonArray.size();i++){
                                    System.out.println(jsonArray.get(i).toString());
                                }
                                //TODO send new client to other clients to add them to the connection
                                Socket socketNewUser = new Socket(mJSONClient.getIpAddress(), (int) mJSONClient.getPort());
                                DataOutputStream outToServer = new DataOutputStream(socketNewUser.getOutputStream());

                                outToServer.writeBytes(clientObject + "\n");
                                socketNewUser.close();
                            }
                            JListEvent.onReceivedJlist(JListClients);


                        } else {
                            System.out.println("Can't add user ");

                        }
                    }
                    if (jsonObject.containsKey("Logout")) {
                        String userName = (String) jsonObject.get("UserName");

                        if (checkIfUsernameExist(JSONClients, userName)) {

                            for (int i = 0; i < JSONClients.size(); i++) {
                                if (JSONClients.get(i).getUserName().equals(userName)) {
                                    JSONClients.remove(i);
                                    JListClients.remove(i);
                                    jsonArray.remove(i);
                                    //  welcomeSocket.close();

                                    System.out.println("Logout : "+jsonArray);
                                    connectionSocket.close();
                                    JListEvent.onReceivedJlist(JListClients);

                                }
                                 for (Object c : JSONClients) {
                                        JSONClient mJSONClient = (JSONClient) c;
                                        JSONObject clientObject = new JSONObject();
                                        clientObject.put("DeleteUser", jsonArray);
                                        //TODO send new client to other clients to add them to the connection
                                        Socket socketNewUser = new Socket(mJSONClient.getIpAddress(), (int) mJSONClient.getPort());
                                        DataOutputStream outToServer = new DataOutputStream(socketNewUser.getOutputStream());

                                        outToServer.writeBytes(clientObject + "\n");
                                        socketNewUser.close();
                                    }




                            }
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }


    public boolean checkIfIPExist(List<JSONClient> JSONClients, String IP) {
        for (JSONClient JSONClient : JSONClients) {
            if (JSONClient.getIpAddress().equals(IP)) {
                return true;

            }
        }
        return false;
    }

    public boolean checkIfPortSameIPExist(List<JSONClient> JSONClients, String IP, long Port) {
        for (JSONClient JSONClient : JSONClients) {
            if (JSONClient.getIpAddress().equals(IP) && JSONClient.getPort() == Port) {
                return true;

            }
        }
        return false;
    }

    public boolean checkIfPortDifferentIPExist(List<JSONClient> JSONClients, String IP, long Port) {
        return true;
    }

    public boolean checkIfUsernameExist(List<JSONClient> JSONClients, String Username) {
        for (JSONClient JSONClient : JSONClients) {
            if (JSONClient.getUserName().equals(Username)) {
                return true;
            }
        }
        return false;
    }

}
