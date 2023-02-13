package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {

    private final String name;
    private final String prefix;
    private Socket socket;

    public Client(String name) {
        this.name = name;
        this.prefix = name + ": ";
        try {
            socket = new Socket("localhost", 9999);
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Podaj swoje imie");
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();
        Client client = new Client(name);
        client.start();
    }

    public void start() throws IOException {
        Receiver receiver = new Receiver();
        Thread receiveThread = new Thread(receiver);
        receiveThread.start();

        Sender sender = new Sender();
        sender.run();

        socket.close();
    }

    class Sender {

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter message = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String s = null;
                message.write(name + "\n");
                message.flush();
                while ((s = br.readLine()) != null) {
                    if (s.equals("exit")) {
                        message.write("Log out" + "\n");
                        message.flush();
                        break;
                    }
                    message.write(prefix + s + "\n");
                    message.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Receiver extends Thread {

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String s = null;
                while ((s = br.readLine()) != null) {
                    System.out.println(s);
                }
            } catch (SocketException e) {
                System.out.println("Log out");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
