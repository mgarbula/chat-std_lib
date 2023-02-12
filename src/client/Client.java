package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {

    private final String name;
    private final String prefix;
    private Socket socket;
    private AtomicBoolean exit = new AtomicBoolean(false);
    private AtomicBoolean changed = new AtomicBoolean(false);

    public Client(String name) {
        this.name = name;
        prefix = name + ": ";
        try {
            socket = new Socket("localhost", 9999);
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Podaj swoje imie");
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();
        Client client = new Client(name);
        client.start();
    }

    public void start() throws InterruptedException, IOException {
        Receiver receiver = new Receiver();
        Thread receiveThread = new Thread(receiver);
        receiveThread.start();

        Sender sender = new Sender(receiveThread);
        Thread sendThread = new Thread(sender);
        sendThread.start();

        receiveThread.join();
        System.out.println("KONIEC startu");
        socket.close();
    }

    class Sender extends Thread {

        private Thread receive;

        public Sender(Thread receive) {
            this.receive = receive;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter message = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String s = null;
                while ((s = br.readLine()) != null) {
                    changed.compareAndSet(false, true);
                    if (s.equals("exit")) {
                        exit.compareAndSet(false, true);
                        break;
                    }
                    message.write(prefix + s + "\n");
                    message.flush();
                    changed.compareAndSet(true, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Receiver extends Thread {

        public void run() {
            System.out.println("Odbieram");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String s = null;
                while (!changed.get());
                while (!exit.get() && (s = br.readLine()) != null) {
                    if (exit.get()) {
                        break;
                    }
                    System.out.println(s);
                    while (!changed.get());
                    if (exit.get()) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
