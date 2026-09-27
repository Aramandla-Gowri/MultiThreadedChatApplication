import java.io.*;
import java.net.Socket;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("      MULTITHREADED CHAT CLIENT");
        System.out.println("=================================");

        try {

            // Connect to the server
            Socket socket =
                    new Socket(SERVER_ADDRESS, SERVER_PORT);

            System.out.println(
                    "Connected to the chat server!"
            );

            System.out.println();

            // Input from server
            BufferedReader serverReader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()
                            )
                    );

            // Output to server
            PrintWriter serverWriter =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true
                    );

            // Input from keyboard
            BufferedReader keyboardReader =
                    new BufferedReader(
                            new InputStreamReader(
                                    System.in
                            )
                    );

            // Thread for receiving messages
            Thread receiveThread = new Thread(() -> {

                try {

                    String message;

                    while ((message =
                            serverReader.readLine()) != null) {

                        System.out.println(message);
                    }

                } catch (IOException e) {

                    System.out.println(
                            "Disconnected from server."
                    );
                }
            });

            receiveThread.start();

            // Read messages from keyboard
            String userInput;

            while ((userInput =
                    keyboardReader.readLine()) != null) {

                // Send message to server
                serverWriter.println(userInput);

                // Exit client
                if (userInput.equalsIgnoreCase("/quit")) {
                    break;
                }
            }

            // Close resources
            socket.close();

            System.out.println(
                    "Disconnected from chat server."
            );

        } catch (IOException e) {

            System.out.println(
                    "Unable to connect to the server."
            );

            System.out.println(
                    "Make sure the server is running on port "
                            + SERVER_PORT
            );
        }
    }
}