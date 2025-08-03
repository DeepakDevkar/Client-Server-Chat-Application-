import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("🟢 Connected to chat server.");

            // Read messages from server
            Thread readerThread = new Thread(() -> {
                String serverMsg;
                try {
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            readerThread.start();

            // Send user messages
            while (true) {
                String userMsg = scanner.nextLine();
                out.println(userMsg);
                if ("exit".equalsIgnoreCase(userMsg)) break;
            }

        } catch (IOException e) {
            System.out.println("❌ Could not connect to server.");
        }
    }
}
