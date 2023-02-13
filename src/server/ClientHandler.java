package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> handlers = new ArrayList<>();
    private final BufferedReader incomeMessageStream;
    private final BufferedWriter outcomeMessageStream;
    private final String name;
    private final Socket socket;

    public ClientHandler(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        this.incomeMessageStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outcomeMessageStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        handlers.add(this);
        sendHello();
    }

    private void sendHello() throws IOException {
        for (ClientHandler handler : handlers) {
            if (!handler.name.equals(name)) {
                handler.outcomeMessageStream.write(name + " just logged in" + "\n");
                handler.outcomeMessageStream.flush();
            }
        }
    }

    public void run() {
        String message;
        try {
            while ((message = incomeMessageStream.readLine()) != null) {
                if (message.equals("Log out")) {
                    System.out.println("You just logged out");
                    handlers.remove(this);
                    for (ClientHandler handler : handlers) {
                        handler.outcomeMessageStream.write(name + " just logged out" + "\n");
                        handler.outcomeMessageStream.flush();
                    }
                    break;
                }
                for (ClientHandler handler : handlers) {
                    if (!handler.name.equals(name)) {
                        handler.outcomeMessageStream.write(message + "\n");
                        handler.outcomeMessageStream.flush();
                    }
                }
            }
            outcomeMessageStream.close();
            incomeMessageStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}