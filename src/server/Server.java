package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            System.out.println("SERVER: new client joined");
            String name = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            ClientHandler accept = new ClientHandler(socket, name);
            Thread thread = new Thread(accept);
            thread.start();
        }
    }

}

