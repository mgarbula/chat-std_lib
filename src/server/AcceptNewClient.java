package server;

import java.net.Socket;

class AcceptNewClient extends Thread {
    private final Socket socket;

    public AcceptNewClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("SERVER: New client connected");
        ClientHandler clientHandler = new ClientHandler(socket);
        clientHandler.start();
    }
}