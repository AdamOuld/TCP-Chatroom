import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Server {

    private static ServerSocket server;

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(9000);
        Server serverS = new Server(server);
        serverS.ServerStarter();
    }
    public void ServerStarter() throws IOException {
        try {
            while(!server.isClosed()){
                System.out.println("<Server>: Waiting for incoming clients...");
                Socket client = server.accept();
                System.out.println("<Server>: Connected to a client!");
                ClientHander clientThread = new ClientHander(client);
                Thread thread = new Thread(clientThread);
                thread.start();
            }
        }
        catch (IOException e) {
            ServerCloser();
        }
    }
    public void ServerCloser() throws IOException {
        if (server != null) {
            server.close();
        }
    }
    Server(ServerSocket server){
        this.server = server;
    }

}
