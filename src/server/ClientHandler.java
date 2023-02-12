package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void start() {
        try {
            BufferedReader incomeMessageStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String incomeMessage = null;
            BufferedWriter sendMessageStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while ((incomeMessage = incomeMessageStream.readLine()) != null) {
                System.out.println(incomeMessage);
                sendMessageStream.write(incomeMessage);
                sendMessageStream.flush();
            }
            incomeMessageStream.close();
            sendMessageStream.close();
        } catch (SocketException e) {
            System.out.println("SERVER: one of clients logged out");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
