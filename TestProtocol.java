/**
 * Created by Devon Deonarine

package assignment_1;

public class TestProtocol {
    String type;
    String zone;
    String data = null;
    String[] parsedMessage = new String[4];
    String rawData;
    String stat;

    //This constructor is used by the Client when sending data to the server because the client has no data;
    public TestProtocol(String stat, String type, String zone) {
        this.zone = zone;
        this.type = type;
        this.stat = stat;
    }
    //This is used by either server or client to take the raw data of the protocol (The string)
    //and saves it to the rawData variable to later be parsed and used
    public TestProtocol(String rawData){
        this.rawData = rawData;
    }

    //Creates the String/actual protocol message that will be sent between client and server
    public String createMessage() {
        return stat + ";" + type + ";" + zone + ";" + data + ";";
    }

    //Parses the protocol string into an Array, The array values can be used by teh server and client's
    //Algorithms to decipher what the message was.
    public String[] parseMessage() {
        parsedMessage = rawData.split(";");
        return parsedMessage;
    }

    //This is used by the Server's Deciphering algorithm. It used the array from the client and changes the
    //values of the protocol field's to match the request and rebuilds it into the protocol message.
    public String rebuildParsedMessage(String[] msg){
        String rebuiltMessage = "";
        for(String s : msg){
            rebuiltMessage += s;
            System.out.println(s);
            rebuiltMessage += ";";
        }
        return rebuiltMessage;
    }

}
