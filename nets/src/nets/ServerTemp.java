package nets;

import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 *
 * @author Brennan
 */


public class ServerTemp
{

	public static void main(String[] args) throws IOException
	{
	
		
		//creates a new server socket
		ServerSocket serverSocket = null;
		try
		{
		serverSocket = new ServerSocket(7315);
		
		}//try
		catch(IOException someException)
		{
			System.out.println("Could not create a socket on port 7315");
			System.exit(1);
		}//catch
		
		
		
		//creates the client socket and listens for a request
		Socket clientSocket = null;
		try
		{
			System.out.println("\nWaiting for connection from client\n");
			clientSocket = serverSocket.accept();
			System.out.println("Connected to client\n");
			System.out.println("Waiting on client request\n");
		}//try
		catch(IOException someException)
		{
			System.out.println("Could not accept the client request");
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
			switch(userChoice)
			{
			
				//Host current date and time
				case 1:
					System.out.println("Forking thread: Host date/time\n");
					//-----TIME
					
					Calendar cal = Calendar.getInstance();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			        String systemTime = sdf.format(cal.getTime());
			        
					out.println("System Time: " + systemTime);
					
					//-----/TIME
					
					
					break;
					
				//Host uptime	
				case 2:
					System.out.println("Forking thread: Host uptime\n");
					//-----UPTIME
					
					RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
					
					out.println("Host uptime: " + mxBean.getUptime());
					
				//----/UPTIME
				
					
					break;
					
				//Host memory use					
				case 3:
					System.out.println("Forking thread: Memory use\n");
					
					long total = Runtime.getRuntime().totalMemory();
					long used  = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					
					//sends the output to the client
					out.println("Total Memory: " + total + "Used Memory: " + used);
					
					break;
		
				//Host Netstat					
				case 4:
					/*
					 * //---NETSTAT
						
						      // Run the command
					
						      Process process = Runtime.getRuntime().exec("netstat");
						      BufferedReader bufferedReader = new BufferedReader(
						              new InputStreamReader(process.getInputStream()));
						
						      
						      // Grab the results
						      StringBuilder log = new StringBuilder();
						      String line;
						
						      if (bufferedReader.readLine() == null)
						    	  System.out.println("was null");
						      while ((line = bufferedReader.readLine()) != null) {
						          log.append(line + "\n");
						      }

						      // Update the view
						      System.out.println(log);
						      output.print(log);
						
						
					*/ 
					
					System.out.println("Forking thread: Netstat\n");
					
					//runs the process, gets the ouptut,and prints it on the client side
					Process process = Runtime.getRuntime().exec("netstat");
				      BufferedReader bufferedReader = new BufferedReader(
				              new InputStreamReader(process.getInputStream()));
					
					//stores the output into a string
					while ((line = bufferedReader.readLine()) != null) 
						output = output + line;
					
					//sends the output to the client
					out.println(output);
					
					break;
					
				//Host current users	
				case 5:
					System.out.println("Forking thread: server current users\n");
					
					//creates a process for current users
					someRuntime = Runtime.getRuntime();
					cmd = "w";
				
					//runs the process, gets the ouptut,and prints it on the client side
					someProcess = someRuntime.exec(cmd);
					stdInp = new BufferedReader(new InputStreamReader(someProcess.getInputStream()));
					
					//stores the output into a string
					while ((line = stdInp.readLine()) != null) 
						output = output + line;
					
					//sends the output to the client
					out.print(output);
					System.out.println(output);
					
					break;
					
				//Host running processes	
				case 6:
				
					System.out.println("Forking thread: server running processes\n");
					
					//creates a process for running processes
					someRuntime = Runtime.getRuntime();
					cmd = "ps -aux";
				
					//runs the process, gets the ouptut,and prints it on the client side
					someProcess = someRuntime.exec(cmd);
					stdInp = new BufferedReader(new InputStreamReader(someProcess.getInputStream()));
					
					//stores the output into a string
					while ((line = stdInp.readLine()) != null) 
						output = output + line;
					
					//sends the output to the client
					out.println(output);	
					
					break;
				}//switch
				
		}while(userChoice != 7);
	   
	   //prints to the server side that the connection is closed
	   System.out.println("Closing connection to client\n");
	   
	   //closes the streams and the sockets
	    out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
		
	}//main
	
}//Sockets_Server

