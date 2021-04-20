package client;

import java.util.ArrayList;

public interface JListEvent {
    void onReceivedJlist(ArrayList<JListClient> message);

}