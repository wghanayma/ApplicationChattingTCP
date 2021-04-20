package server;

 import java.util.List;

public interface JListEvent {
    void onReceivedJlist(List< JListClient> message);

}
