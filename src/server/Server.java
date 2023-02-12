package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            AcceptNewClient accept = new AcceptNewClient(socket);
            Thread thread = new Thread(accept);
            thread.start();
        }
    }

}

