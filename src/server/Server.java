package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        while (true) {
            AcceptNewClient accept = new AcceptNewClient(serverSocket);
            Thread thread = new Thread(accept);
            thread.start();
        }
    }

}

