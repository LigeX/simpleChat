package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  // *** ADDED: keep host/port/loginID for commands
  private String host;
  private int port;
  @SuppressWarnings("unused")
  private final String loginID;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    // *** CHANGED: default to anonymous when using original ctor
    this("anonymous", host, port);
  }

  // *** ADDED: ctor with mandatory loginID
  public ClientConsole(String loginID, String host, int port) 
  {
    this.loginID = loginID; // *** ADDED
    this.host = host;       // *** ADDED
    this.port = port;       // *** ADDED
    try 
    {
      client= new ChatClient(host, port, this, loginID); // *** CHANGED
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();

        // *** ADDED: commands starting with '#'
        if (message.startsWith("#")) {
        	  handleCommand(message.trim());
        } else {
        	 if (!client.isConnected()) {
        	   display("Not connected to server.");   // *** ADDED
        	 } else {
        	   client.handleMessageFromClientUI(message);
        	 }
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  // *** ADDED: minimal command handling for Exercise 2(a)
  private void handleCommand(String line) {
    String[] p = line.split("\\s+");
    String cmd = p[0];

    switch (cmd) {
      case "#quit":
        client.quit();
        break;
      case "#logoff":
        client.logoff();
        break;
      case "#sethost":
        if (p.length < 2) { display("Usage: #sethost <host>"); break; }
        if (client.isConnected()) display("Error: You are already connected.");
        else { host = p[1]; try { client.setHost(host); } catch (Exception ignored) {} }
        break;
      case "#setport":
        if (p.length < 2) { display("Usage: #setport <port>"); break; }
        if (client.isConnected()) display("Error: You are already connected.");
        else {
          try { port = Integer.parseInt(p[1]); client.setPort(port); }
          catch (Exception e) { display("Invalid port."); }
        }
        break;
      case "#login":
        if (client.isConnected()) display("Error: Already connected.");
        else {
          try { client.openConnection(); } 
          catch (IOException e) { display("ERROR - Can't setup connection! Terminating client."); client.quit(); }
        }
        break;
      case "#gethost":
        display(host);
        break;
      case "#getport":
        display(String.valueOf(port));
        break;
      default:
        display("Error: Unknown command.");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    // *** CHANGED: follow Exercise 3(a): args => <loginID> [host] [port]
    String loginID = null;
    int port = DEFAULT_PORT;

    try
    {
      // original code kept, but we now interpret arguments minimally
      // host = args[0];
      // *** ADDED:
      if (args.length < 1 || args[0].isEmpty()) {
        System.out.println("ERROR - No login ID specified.  Connection aborted.");
        System.exit(1);
      }
      loginID = args[0];
      host = (args.length > 1 && !args[1].isEmpty()) ? args[1] : "localhost";
      if (args.length > 2) {
        try { port = Integer.parseInt(args[2]); } catch (NumberFormatException e) { port = DEFAULT_PORT; }
      }
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ClientConsole chat= new ClientConsole(loginID, host, port); // *** CHANGED
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
