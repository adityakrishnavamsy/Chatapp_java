import java.net.*;
import java.io.*;

class Server {
    // constructor
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept the connection");
            System.out.println("Waiting....");
            socket = server.accept();
            // as we will be accepting the request from the client we will store it in
            // socket
            // from the socket we can have i/o stream
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            // i/o stream handling
            // start the reading and writing
            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        // thread will keep reading and giving the data
        Runnable r1 = () -> {
            System.out.println("reader started..");

            // as we need to keep reading
            try {
                while (true && !socket.isClosed()) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("Client: " + msg);

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };

        // start thread
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread - data user se lega and the send karega client tak
        Runnable r2 = () -> {
            System.out.println("Writer started..");
            try {
                while (true && !socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    // we need to tak the input from the user
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                }
            } catch (Exception e) {
                // e.printStackTrace();
                // to print the technical information
                System.out.println("Connection is closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is server");
        new Server();
    }
}