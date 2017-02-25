package nets;

import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 *
 * @author Group4
 */
public class ServerTemp
{

    public static void main(String[] args) throws IOException
    {
        int portNum = 7315;

        //creating a new server socket
        ServerSocket serverSocket = null;

        //try catch block creates a new server socket and passes in the vm port number
        try
        {
            serverSocket = new ServerSocket(portNum);

        }
        //if the server socket creation is unsuccessful, it lets the user know
        catch (IOException someException)
        {
            System.out.printf("Creation of socket for Port Number $d was unsuccessful.%n", portNum);
            System.exit(1);
        }

        //creates the client socket and listens for a request
        Socket clientSocket = null;
        try
        {
            System.out.printf("Awaiting Client connection...%n"); //telling user what the program is doing
            clientSocket = serverSocket.accept(); //calls server socket's accept method to listen for connection being made on socket and accepts it
            System.out.printf("Client connection successful!%n"); //letting user know what is happening
            System.out.printf("Awaiting Client request...%n");
        }
        catch (IOException someException)
        {
            System.out.printf("Completion of Client's request was unsuccessful!%n");
            System.exit(2);
        }//catch

        //creates the input and output streams
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //the menu number sent over from the client
        int userChoice;

        //process and runtime 
        Process someProcess = null;
        Runtime someRuntime = null;
        BufferedReader stdInp = null;
        String cmd = "";
        String output = "";
        String line;

        do
        {
            //reads what the client sent and parses it
            userChoice = Integer.parseInt(in.readLine());

            //calls different methods depending on what the user chose
            switch (userChoice)
            {

                //Host current date and time
                case 1:
                    System.out.printf("Forking thread: Host's system's date and time%n");
                    //-----TIME

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String systemTime = sdf.format(cal.getTime());

                    out.println("System Time: " + systemTime);

                    //-----/TIME
                    break;

                //Host uptime	
                case 2:
                    System.out.printf("Forking thread: Host's uptime%n");
                    //-----UPTIME

                    RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();

                    out.println("Host uptime: " + mxBean.getUptime());

                    //----/UPTIME
                    break;

                //Host memory use					
                case 3:
                    System.out.printf("Forking thread: Server's memory usage%n");

                    long total = Runtime.getRuntime().totalMemory();
                    long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                    //sends the output to the client
                    out.println("Total Memory: " + total + "\nUsed Memory: " + used);

                    break;

                //Host Netstat					
                case 4:
                    System.out.printf("Forking thread: Netstat%n");

                    //runs the process, gets the ouptut,and prints it on the client side
                    Process process = Runtime.getRuntime().exec("netstat");
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    //stores the output into a string
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        output = output + line;
                    }

                    //sends the output to the client
                    out.println(output);

                    break;

                //Host's current users	
                case 5:
                    System.out.printf("Forking thread: Server's current users%n");

                    //creates a process for current users
                    someRuntime = Runtime.getRuntime();
                    cmd = "w";

                    //runs the process, gets the ouptut,and prints it on the client side
                    someProcess = someRuntime.exec(cmd);
                    stdInp = new BufferedReader(new InputStreamReader(someProcess.getInputStream()));

                    //stores the output into a string
                    while ((line = stdInp.readLine()) != null)
                    {
                        output = output + line;
                    }

                    //sends the output to the client
                    out.print(output);
                    System.out.println(output);

                    break;

                //Host's running processes	
                case 6:

                    System.out.printf("Forking thread: Server's running processes%n");

                    //creates a process for running processes
                    someRuntime = Runtime.getRuntime();
                    cmd = "ps -aux";

                    //runs the process, gets the ouptut,and prints it on the client side
                    someProcess = someRuntime.exec(cmd);
                    stdInp = new BufferedReader(new InputStreamReader(someProcess.getInputStream()));

                    //stores the output into a string
                    while ((line = stdInp.readLine()) != null)
                    {
                        output = output + line;
                    }

                    //sends the output to the client
                    out.println(output);

                    break;
            }

        }
        while (userChoice != 7);

        //prints to the server side that the connection is closed
        System.out.println("Closing connection to client\n");

        //closes the streams and the sockets
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();

    }

}

