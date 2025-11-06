package edu.seg2105.edu.server.ui;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF; // reuse ChatIF
import edu.seg2105.edu.server.backend.EchoServer;

/**
 * Server-side console that mirrors ClientConsole behavior.
 * 
 *
 * Commands:
 *   #quit   - quit server process
 *   #stop   - stop listening for new clients
 *   #close  - stop listening and disconnect all clients
 *   #setport <port> - only allowed if server is closed (not listening & no clients)
 *   #start  - start listening (only if stopped)
 *   #getport - print current port
 *
 * Any other text is broadcast to all clients prefixed with "SERVER MESSAGE> "
 * and also echoed on the server console.
 */
public class ServerConsole implements ChatIF {

  private final EchoServer server;
  private final Scanner scanner = new Scanner(System.in);

  public ServerConsole(EchoServer server) {
    this.server = server;
  }

  public void accept() {
    try {
      while (true) {
        String line = scanner.nextLine().trim();
        if (line.startsWith("#")) {
          handleCommand(line);
        } else {
          // Echo on server console and to all clients with required prefix.
          display("SERVER MESSAGE> " + line);
          try {
            server.sendToAllClients("SERVER MESSAGE> " + line);
          } catch (Exception ignored) {}
        }
      }
    } catch (Exception e) {
      System.out.println("Unexpected error while reading from server console!");
    }
  }

  private void handleCommand(String cmdLine) {
    String[] parts = cmdLine.split("\\s+");
    String cmd = parts[0];

    switch (cmd) {
      case "#quit":
        try { server.close(); } catch (Exception ignored) {}
        System.exit(0);
        break;

      case "#stop":
        try { server.stopListening(); } catch (Exception ignored) {}
        break;

      case "#close":
        try { server.close(); } catch (Exception ignored) {}
        break;

      case "#setport":
        if (parts.length < 2) {
          display("Usage: #setport <port>");
          break;
        }
        if (server.isListening() || server.getNumberOfClients() > 0) {
          display("Error: Server must be closed to set port.");
          break;
        }
        try {
          int newPort = Integer.parseInt(parts[1]);
          server.setPort(newPort);
        } catch (NumberFormatException e) {
          display("Invalid port.");
        }
        break;

      case "#start":
        if (server.isListening()) {
          display("Error: Server already listening.");
          break;
        }
        try { server.listen(); } catch (Exception ignored) {}
        break;

      case "#getport":
        display(String.valueOf(server.getPort()));
        break;

      default:
        display("Error: Unknown command.");
    }
  }

  @Override
  public void display(String message) {
    System.out.println(message);
  }

  /** Boot entry for server with console. */
  public static void main(String[] args) {
    int port = EchoServer.DEFAULT_PORT;
    if (args.length > 0) {
      try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
    }
    EchoServer sv = new EchoServer(port);
    try { sv.listen(); } catch (Exception e) {
      System.out.println("ERROR - Could not listen for clients!");
    }

    // "The server console waits for user input." (Testcase 2001)
    ServerConsole console = new ServerConsole(sv);
    console.accept();
  }
}
