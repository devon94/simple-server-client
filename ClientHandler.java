/**
 * Created by Devon Deonarine
 *
 * Handles a client that is connected to the server for as long
 * as it is connected. Deals with reading requests and sending
 * responses. Allows multiple clients to be connected at the same
 * time.
 */

package assignment_1;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

public class ClientHandler implements Runnable {

    private Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //Informing that the thread started successfully
            System.out.println("Thread started with name: " + Thread.currentThread().getName());
            readResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Informing that the thread is closing which means the client disconnected
        System.out.println("Thread closing...");
    }

    private void readResponse() throws IOException, InterruptedException {
        boolean error = false;
        while(client.isConnected()) {
            try {
                //Create Datainput stream to read requests
                DataInputStream input = new DataInputStream(client.getInputStream());
                //Read request into string encoded in UTF-8
                String request = input.readUTF();
                send("Request received for " + request, client);
                createResponse(request);
                //Logs the Request from the client into the Server's GUI
                ServerGUI.getInstance().log("Request from client " + client + " Request: " + request);
            } catch (EOFException e) {
                //When there is an EOF Exception, it means the client has disconnected
                ServerGUI.getInstance().log("Client " + client.getRemoteSocketAddress() + " has disconnected");
                break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createResponse(String request){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        //Uses the Algorithm in The protocol Class to parse the message into an array
        String[] parsedRequest = new TestProtocol(request).parseMessage();
        //Based on the array, The second index will always contain the type of request
        //Switch the request string and then form response based on that
        switch (parsedRequest[1]){

            case "Date":
                //Sets date format to only send the Date
                df = new SimpleDateFormat("yyyy-MM-dd");
                df.setTimeZone(TimeZone.getTimeZone(parsedRequest[3]));
                break;

            case "Time":
                //Sets date format to only send the Time
                df = new SimpleDateFormat("HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone(parsedRequest[3]));
                break;

            //Default case is never used but can be used if the client is modified to send
            //unknown request
            default:
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }

        if (df != null){
            //Sets the response to thh client to "OK"
            parsedRequest[0] = "OK";
            //Adds the data into the fourth index of the array
            parsedRequest[3] = df.format(date).toString();
        } else {
            //The ERROR will most likely never be used unless the SimpleDateFormat fails
            parsedRequest[0] = "ERROR";
        }

        //Based on the Array, The algorithm in the protocol rebuilds the Protocol to send
        String response = new TestProtocol(request).rebuildParsedMessage(parsedRequest);
        //Send the response to the CLient
        send(response,client);
    }

    //This function sends a string to a socket
    public void send(String input, Socket socket) {
        try {
            //Creates a new Dataoutputstream based on the sockets outputstream
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            //Writes the input, in this case the Protocol, encdoded in UTF to the clients inputStream
            output.writeUTF(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
