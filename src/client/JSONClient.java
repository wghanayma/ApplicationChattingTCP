package client;

public class JSONClient {
    private final String userName;
    private final String ipAddress;
    private final long port;


    public JSONClient(String ipAddress, long port, String userName) {

        this.userName = userName;
        this.ipAddress = ipAddress;
        this.port = port;

    }


    public long getPort() {
        return port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "{" +
                "\"UserName\": \"" + userName + '"' +
                ", \"IPAddress\": \"" + ipAddress + '"' +
                ", \"Port\":" + port +
                '}';

    }
}
