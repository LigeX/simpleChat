// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  // *** ADDED: login id to satisfy Exercise 3(a)(b)
  private final String loginID;

  // *** ADDED: flag to distinguish user logoff vs server shutdown
  private volatile boolean closingFromClient = false;

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    // *** CHANGED: keep original constructor for backward compatibility
    this(host, port, clientUI, "anonymous"); // default id when old code path used
  }

  // *** ADDED: overloaded ctor with loginID (Exercise 3)
  public ChatClient(String host, int port, ChatIF clientUI, String loginID)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  // *** ADDED: upon establishing connection, auto-send #login <loginID>
  @Override
  protected void connectionEstablished() {
    try {
      sendToServer("#login " + loginID);
    } catch (IOException e) {
      clientUI.display("ERROR - Can't setup connection! Terminating client.");
      quit();
    }
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  // *** ADDED: clean message when server closed (Exercise 1a, tests)
  @Override
  protected void connectionClosed() {
    if (!closingFromClient) {
      clientUI.display("The server has shut down.");
      System.exit(0);
    }
  }

  // *** ADDED: also handle abnormal exceptions as shutdown
  @Override
  protected void connectionException(Exception exception) {
    if (!closingFromClient) {
      clientUI.display("The server has shut down.");
      System.exit(0);
    }
  }

  // *** ADDED: public logoff used by client console #logoff
  public void logoff() {
    try {
      closingFromClient = true;
      closeConnection();
      clientUI.display("Connection closed.");
    } catch (IOException e) {
      // ignore
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closingFromClient = true; // *** ADDED
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
