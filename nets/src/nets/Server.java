/**
 * CNT 4504 - Project 1: Server
 * @author Group4
 * Paul Edwards
 * Brennan Hinck
 * Molly Johnson
 * Whitley Turner
 * Brandon Magaro
 */
package nets;

import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/*
* 
 */
public class Server
{

    public static void main(String[] args) throws IOException
    {
        int portNum = 9999;

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

                    System.out.printf("Forking thread: Server's uptime:%n");

                    Process uptimeProcess = Runtime.getRuntime().exec("uptime");

                    //runs the process, gets the ouptut,and prints it on the client side
                    //    someProcess = someRuntime.exec(cmd);
                    stdInp = new BufferedReader(new InputStreamReader(uptimeProcess.getInputStream()));

                    //stores the output into a string
                    while ((line = stdInp.readLine()) != null)
                    {
                        output = output + ";" + line;
                    }

                    //sends the output to the client
                    out.println(output);

                    output = "";	//reset output

                    break;

                //Host memory use					
                case 3:
                    System.out.printf("Forking thread: Server's memory usage%n");

                    Process memProcess = Runtime.getRuntime().exec("free");

                    //runs the process, gets the ouptut,and prints it on the client side
                    stdInp = new BufferedReader(new InputStreamReader(memProcess.getInputStream()));

                    //stores the output into a string
                    while ((line = stdInp.readLine()) != null)
                    {
                        output = output + ";" + line;
                    }

                    //sends the output to the client
                    out.println(output);

                    output = "";	//empties output string

                    break;

                //Find Host Netstat					
                case 4:
                    System.out.printf("Forking thread: Netstat%n");

                    //takes client request, sends command to server, sends output to client as string
                    Process process = Runtime.getRuntime().exec("netstat");
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    //stores the output into a string
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        output = output + ";" + line; //using ; as delimiter
                    }

                    //sends the output to the client
                    out.println(output);

                    output = "";	//reset output

                    break;

                //Host's current users	
                case 5:
                    System.out.printf("Forking thread: Server's current users%n");

                    Process userProcess = Runtime.getRuntime().exec("w");

                    //runs the process, gets the ouptut,and prints it on the client side
                    //    someProcess = someRuntime.exec(cmd);
                    stdInp = new BufferedReader(new InputStreamReader(userProcess.getInputStream()));

                    //stores the output into a string
                    while ((line = stdInp.readLine()) != null)
                    {
                        output = output + ";" + line;
                    }

                    //sends the output to the client
                    out.println(output);

                    output = "";	//reset output

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
                        output = output + ";" + line;
                    }

                    //sends the output to the client
                    out.println(output);

                    output = "";
                    break;
            }

        }
        while (userChoice != 7);

        //prints to the server side that the connection is closed
        System.out.println("Closing connection to client\n");

        //closing all of the needed sockets and streams
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();

    }

}
