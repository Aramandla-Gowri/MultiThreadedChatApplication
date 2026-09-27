import java.io.*;
import java.net.Socket;
import java.util.Set;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Set<ClientHandler> clients;

    private BufferedReader reader;
    private PrintWriter writer;

    private String username;

    public ClientHandler(Socket socket, Set<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {

        try {

            // Create input and output streams
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            // Ask client for username
            writer.println("Enter your username:");

            username = reader.readLine();

            if (username == null || username.trim().isEmpty()) {
                username = "Anonymous";
            }

            username = username.trim();

            // Welcome message
            writer.println(
                    "Welcome " + username + "!"
            );

            writer.println(
                    "You can now start chatting."
            );

            writer.println(
                    "Type /quit to leave the chat."
            );

            // Notify all other clients
            broadcast(
                    ">>> " + username + " joined the chat! <<<",
                    this
            );

            System.out.println(
                    username + " joined the chat."
            );

            String message;

            // Continuously read messages
            while ((message = reader.readLine()) != null) {

                // Check if client wants to leave
                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }

                if (!message.trim().isEmpty()) {

                    String formattedMessage =
                            username + ": " + message;

                    System.out.println(formattedMessage);

                    // Send message to all connected clients
                    broadcast(formattedMessage, null);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    username + " disconnected."
            );

        } finally {

            disconnectClient();
        }
    }

    // Broadcast message to all connected clients
    private void broadcast(
            String message,
            ClientHandler sender
    ) {

        for (ClientHandler client : clients) {

            // Don't send join message back to the same client
            if (client != sender) {

                client.sendMessage(message);
            }
        }
    }

    // Send a message to this client
    public void sendMessage(String message) {

        if (writer != null) {
            writer.println(message);
        }
    }

    // Remove client and close connection
    private void disconnectClient() {

        clients.remove(this);

        if (username != null) {

            broadcast(
                    "<<< " + username + " left the chat. >>>",
                    this
            );

            System.out.println(
                    username + " left the chat."
            );
        }

        try {

            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error closing connection: "
                            + e.getMessage()
            );
        }
    }
}