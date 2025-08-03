import java.io.*;
import java.net.*;
import java.util.Set;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Set<ClientHandler> clients;

    public ClientHandler(Socket socket, Set<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up streams.");
        }
    }

    public void run() {
        try {
            out.println("Welcome to the chat! Type 'exit' to leave.");

            String message;
            while ((message = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message)) break;
                broadcast(message);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) { }
            clients.remove(this);
            broadcast("A user has left the chat.");
        }
    }

    private void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.out.println("User: " + message);
                }
            }
        }
    }
}
