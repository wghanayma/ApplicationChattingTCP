package server;

public class JSONClient implements Comparable<JSONClient> {
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

    public String list() {
        return
                userName + "-" + ipAddress + "-" + port;
    }

    public int compareTo(JSONClient o) {
        if (this.port == o.port && this.ipAddress.equals(o.ipAddress) && this.userName.equals(o.ipAddress)) {
            return 0;
        }
        return 1;
    }
}
