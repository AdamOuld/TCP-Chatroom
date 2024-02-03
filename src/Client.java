import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private static String username;
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your username?");
        username = scanner.nextLine();
        Socket socket = new Socket("127.0.0.1", 9000);
        Client client = new Client(socket, username);
        client.listenMessage();
        client.sendMessage();
    }

    Client(Socket socket, String username) {
        try{
            this.socket = socket;
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }
        catch (IOException e) {
            closeAll(socket, writer, reader);
        }
    }

    public void sendMessage () {
        String message;
        try {
            writer.println(username);
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                message = scanner.nextLine();
                writer.println(username + ": " + message);
            }
        }
        catch (Exception e){
            closeAll(socket, writer, reader);
        }
    }

    public void listenMessage () {
        new Thread (new Runnable() {
            @Override
            public void run() {
                String response;
                while(socket.isConnected()) {
                    try {
                        response = reader.readLine();
                        System.out.println(response);
                    }
                    catch (IOException e) {
                        closeAll(socket, writer, reader);
                    }
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, PrintWriter writer, BufferedReader reader) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}