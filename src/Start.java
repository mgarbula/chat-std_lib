import server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Start {

    public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(9999));
        server.startServer();
    }

}
