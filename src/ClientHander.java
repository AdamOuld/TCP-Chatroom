import java.net.*;
import java.io.*;
import java.util.*;


public class ClientHander implements Runnable{
    private Socket client;
    private BufferedReader reader;
    private PrintWriter writer;
    private String clientName;
    private static List<ClientHander> clients = new ArrayList<>();


    @Override
    public void run() {
        String clientMessage;
        while (client.isConnected()) {
            try {
                clientMessage = reader.readLine();
                brodcastMessage(clientMessage);
            }
            catch (IOException e){
                try {
                    closeAll(client, reader, writer);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }
        }
    }

    public ClientHander(Socket clientSocket) throws IOException {
        try{
            this.client = clientSocket;
            this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.writer = new PrintWriter(client.getOutputStream(), true);
            this.clientName = reader.readLine();;
            clients.add(this);
            brodcastMessage("<Server Message>: " + this.clientName + " has joined the chat!");
        }
        catch(IOException e) {
            closeAll(client, reader, writer);
        }

    }
    public void brodcastMessage(String brodcast) {
        for (ClientHander client : clients) {
            if (!Objects.equals(client.clientName, clientName)) {
                client.writer.println(brodcast);
            }
        }
    }

    public void outputMessage(){}

    public void closeAll(Socket socket, BufferedReader reader, PrintWriter writer) throws IOException {
        removeClient();
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

    public void removeClient() {
        clients.remove(this);
        brodcastMessage("<Server Message>: " + clientName + " has left the chat");
    }
}
