/**
 * Created by Devon Deonarine
 *
 * This layer starts the server sockets that listens for clients.
 * When a client connects, it gives them their own thread that will
 * handle it.
 */

package assignment_1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;



public class Server {

    private ServerSocket socket;
    private int port;
    private boolean running = true;

    //Sets the port number which was set in the ServerGUI Class
    public Server(int port){
        this.port = port;
    }

    public void startServer(){
        try{
            //Create server socket
            socket = new ServerSocket(port);
            //Log message on Server GUI that the Server socket had been initialized
            ServerGUI.getInstance().log("Server socket initialized on port " + port);
        }
        //For error to break out and quit
        catch(IOException e){
            e.printStackTrace();
            return;
        }

        //Create new thread to listen for connections. Threads are used so that the GUI/other classes don't lag
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        //
                        final Socket client = socket.accept();
                        //Sends a message to the console of who connected
                        ServerGUI.getInstance().log("Client has connected " + client +" " + client.getRemoteSocketAddress());
                        //send("Connected", client);
                        //Create New Thread to Read Client Data
                        Thread thread = new Thread(new ClientHandler(client));
                        thread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void stopServer(){
        running = false;
    }

}
