/**
 * Created by Devon Deonarine
 */

package assignment_1;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.List;

public class ClientGUI extends JFrame{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd@HH:MM");
    public static final String TITLE = "Client";

    private Client nClient;
    private static ClientGUI instance;
    private JTextArea console;
    private JTextField input;
    private JButton Date;
    private JButton Time;
    String[] timeStrings;

    public ClientGUI(){
        //Initializes the ClientGUI with the IP address (localhost) and port to connect to (Same port as server)
        nClient = new Client("127.0.0.1",7184);
        //runs the required code to connect to the server located in networkServer.java.
        //Creates the view
        createView();
        //Sets the title of the application
        setTitle(TITLE);
        //Sets the dimension of the Application
        setSize(500, 400);
        setResizable(true);
        //Set minimum size becuase it looks weird if you shrink it too much
        setMinimumSize(new Dimension(500, 400));
        //
        setLocationRelativeTo(null);
        //Set the window to exit when the close button is clicked
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Setup Times Zones Array to make the list look nicer
        setTimeArray();
    }

    //Shows popup with the Response from the server
    public void showResponse(String response){
        JOptionPane.showMessageDialog(null, response);
    }

    public void createView(){
        //Create new panel
        JPanel panel = new JPanel();
        //
        getContentPane().add(panel);
        //set BorderLayout of panel
        panel.setLayout(new BorderLayout());
        //init Text Pane
        console = new JTextArea();
        //Set line wrap
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        //Disable text area from being edited by user
        console.setEditable(false);
        ((DefaultCaret) console.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //add Text pane to scroll pane
        JScrollPane consoleScrollPane = new JScrollPane(console);
        //Creates a border and Title for the CHat area
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Console"));
        //add scroll text pane to view
        panel.add(consoleScrollPane, BorderLayout.CENTER);
        //Create new panel for input to add to the view. This helps with positioning the buttons
        JPanel panelInput = new JPanel(new BorderLayout());
        //Add the new panel to the bottom of the already existing one.
        panel.add(panelInput, BorderLayout.SOUTH);
        //Create new date button
        Date = new JButton("Date");
        //Create new time buttom
        Time = new JButton("Time");
        //Set Layout for buttons
        panelInput.setLayout(new FlowLayout(FlowLayout.CENTER));
        //Add buttons to the bottom panel
        panelInput.add(Date);
        panelInput.add(Time);

        //Add action listener to the Date button to send request
        Date.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRequest("Date");
            }
        });

        //Add action listener to the Time button to send request
        Time.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRequest("Time");
            }
        });
    }


    //Creates the request to send to the server. Asks for a time zone.
    //I know we didnt need that but
    public void createRequest(String type){
        //Time Zone String
        String zone = new String();
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please select a Time Zone:"));
        JComboBox comboBox = new JComboBox(timeStrings);
        comboBox.setSelectedItem("Canada/Eastern");
        panel.add(comboBox);
        int result = JOptionPane.showConfirmDialog(null, panel, "Time Zone", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        //Retrieves time zone
        switch (result) {
            case JOptionPane.OK_OPTION:
                zone =  (String) comboBox.getSelectedItem();
                System.out.println("You selected " + comboBox.getSelectedItem());
                nClient.send(zone,type);
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
        }
    }

    //Logs information of communication between server and client
    public void log(String message)
    {
        console.append(DATE_FORMAT.format(new Date()) + " - " + message + "\n");
    }

    //To remove weird time zones. Just so the dropdown looks nicer.
    private void setTimeArray() {
        //Create ArrayList from Timezone Array
        List<String> list = new ArrayList<String>(Arrays.asList(TimeZone.getAvailableIDs()));
        //Iterator to iterate through list
        ListIterator<String> iter = list.listIterator();
        //Loop Through Iterator
        while (iter.hasNext()){
            //Next element
            String curr = iter.next();
            //Check for the things we don't want
            if(curr.contains("Etc") || curr.contains("Universal")){
                iter.remove();
            }
        }
        //Convert list back to an array
        timeStrings = new String[list.size()];
        timeStrings = list.toArray(timeStrings);
    }


    public static ClientGUI getInstance(){
        return instance;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                instance = new ClientGUI();
                instance.setVisible(true);
                instance.nClient.connectToServer();
            }
        });
    }
}
