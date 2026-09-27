import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final int PORT = 5000;

    // Stores all connected clients
    private static final Set<ClientHandler> clients =
            ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("   MULTITHREADED CHAT SERVER");
        System.out.println("=================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started on port: " + PORT);
            System.out.println("Waiting for clients...");
            System.out.println();

            while (true) {

                // Wait for a new client
                Socket socket = serverSocket.accept();

                System.out.println(
                        "New client connected: "
                                + socket.getInetAddress()
                );

                // Create a separate handler for each client
                ClientHandler clientHandler = new ClientHandler(socket, clients);

                // Add client to the connected clients list
                clients.add(clientHandler);

                // Start a separate thread
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {

            System.out.println("Server error: " + e.getMessage());
        }
    }
}