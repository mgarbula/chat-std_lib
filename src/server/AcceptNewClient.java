package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class AcceptNewClient extends Thread {
    private final ServerSocket serverSocket;

    public AcceptNewClient(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            Socket socket = serverSocket.accept();
            System.out.println("SERVER: New client connected");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}