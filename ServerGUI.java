/**
 * Created by Devon Deonarine
 */

package assignment_1;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServerGUI extends JFrame{

    private Server nServer;
    public static final String TITLE = "Server";
    private static ServerGUI instance;
    private JTextArea console;
    private JTextField input;
    private JButton send;

    //Date format used for prepending the data and time to logged messages
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd@HH:MM");

    public ServerGUI(){
        //Sets port for the Server, We just chose a random port
        nServer = new Server(7184);
        createView();
        setTitle(TITLE);
        setSize(720, 500);
        setResizable(true);
        setMinimumSize(new Dimension(720,500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //Logs messages into the textArea of the GUI
    public void log(String message){
        console.append(DATE_FORMAT.format(new Date()) + " - " + message + "\n");
    }

    public void createView(){
        //create panel
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        //set border layout of panel
        panel.setLayout(new BorderLayout());
        //init Text Pane
        console = new JTextArea();
        //Disable editing of the text area
        console.setEditable(false);
        //To get text pane to scroll down automatically on server
        ((DefaultCaret) console.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //add console to scroll pane
        JScrollPane consoleScrollPane = new JScrollPane(console);
        //
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Console"));
        //add scroll text pane to view
        panel.add(consoleScrollPane, BorderLayout.CENTER);
        //Sets word wrap for the messages logged
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
    }

    public static ServerGUI getInstance(){
        return instance;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Creates an instance of the GUI to be used by other classes
                instance = new ServerGUI();
                instance.setVisible(true);
                //Started the Actual connections with the nServer's
                //startServer() function
                instance.nServer.startServer();
            }
        });
    }
}
