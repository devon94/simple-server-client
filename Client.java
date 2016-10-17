/**
 * Created by Devon Deonarine
 */

package assignment_1;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private String ipAddress;
    private int serverPort;

    public Client(String ipAddress, int serverPort){
        //IP Address to connect to
        this.ipAddress = ipAddress;
        //Port on the IP to connect to
        this.serverPort = serverPort;
    }

    public void connectToServer(){
        try {
            socket = new Socket(ipAddress,serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(socket.isConnected()){
                    ClientGUI.getInstance().log("Connected to Server");
                }
                while(socket.isConnected()) {
                    try {
                        //Create DataInput stream to read requests
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        //Read response into string encoded in UTF-8
                        String response = input.readUTF();
                        System.out.println(response);
                        if(!response.contains("Request")){
                            parseResponse(response);
                        } else {
                            //Logs the response into the textArea
                            ClientGUI.getInstance().log(response);
                        }
                    } catch (EOFException e) {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    public void parseResponse(String rawData){
        String[] parsedResponse = new TestProtocol(rawData).parseMessage();
        //Makes sure there isn't an error.
        if (!(parsedResponse[0].equals("ERROR"))) {
            ClientGUI.getInstance().showResponse(parsedResponse[1] + " in "+ parsedResponse[2] + " time zone is " + parsedResponse[3]);
        }
        else{
            ClientGUI.getInstance().showResponse("There was an error with your request :(");
        }
    }

    //Sends the Request To The server with a timezone and the type of request
    public void send(String tz, String type) {
        //Uses the protocol class's function to build the protocol string
        //A GET is always sent, This allows for other requests to be implemented in future.
        TestProtocol t = new TestProtocol("GET",type,tz);
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(t.createMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
