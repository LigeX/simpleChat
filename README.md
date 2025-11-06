# Simple Chat Application (SEG2105 - Assignment 2)

This project is a client-server chat application built using the **OCSF (Object Client Server Framework)**.  
It demonstrates socket communication, message broadcasting, client connection management, and server control commands.

---

## 📌 Features

### Client
- Connects to a server using a hostname and port
- Sends and receives chat messages
- Supports multiple users simultaneously
- Includes command-based control

### Server
- Accepts client connections
- Broadcasts messages to all connected clients
- Allows server operator to send messages and control the server state
- Manages login and disconnection logic

---

## 🗂️ Project Structure
project/
├─ edu.seg2105.client.common/ChatIF.java
├─ edu.seg2105.client.backend/ChatClient.java
├─ edu.seg2105.client.ui/ClientConsole.java
├─ edu.seg2105.server.backend/EchoServer.java
└─ edu.seg2105.server.ui/ServerConsole.java

---

## ▶️ How to Run

### **1. Start the Server**
In your IDE, run:

Or start with a custom port:

ServerConsole 6666
Console should display:

Server listening for connections on port XXXX

### **2. Start a Client**
ClientConsole <loginID> <host> <port>
Example:

ClientConsole alice 127.0.0.1 5555


---

## 💬 Client Commands

| Command | Description |
|--------|-------------|
| `#login <id>` | Log in with a user name (only allowed if not connected) |
| `#logoff` | Disconnect from the server but keep program running |
| `#quit` | Disconnect and close the client |
| `#gethost` | Display the current host |
| `#getport` | Display the current port |
| `#sethost <host>` | Change host (only allowed when disconnected) |
| `#setport <port>` | Change port (only allowed when disconnected) |

**Sending any text that does *not* start with `#` will broadcast to all clients.**

---

## 🖥️ Server Console Commands

| Command | Description |
|--------|-------------|
| `#stop` | Stop accepting new clients (existing connections remain) |
| `#start` | Resume listening for new clients |
| `#close` | Disconnect all clients and stop server |
| `#quit` | Shutdown server completely |
| *(any message)* | Broadcast message to all connected clients |

---

## ✅ Test Case Summary (All Passed)

| Test Case ID | Status |
|-------------|--------|
| TC-2001 – Server Starts | ✅ PASS |
| TC-2002 – Send Message While Logged Off | ✅ PASS |
| TC-2003 – Duplicate Login Attempt | ✅ PASS |
| TC-2004 – Normal Chat Message | ✅ PASS |
| TC-2005 – Multi-Client Broadcast | ✅ PASS |
| TC-2006 – Server-side Broadcast | ✅ PASS |
| TC-2007 – `#quit` Handling | ✅ PASS |
| TC-2008 – `#stop` / `#close` | ✅ PASS |
| TC-2009 – `#start` Resume Listening | ✅ PASS |
| TC-2010 – Client `#quit` | ✅ PASS |
| TC-2011 – Logoff + Reconnect | ✅ PASS |
| TC-2012 – Custom Port Connection | ✅ PASS |
| TC-2013 – Host/Port Commands | ✅ PASS |

---

## 📝 Notes
- Ensure only **one server instance** is running at a time.
- If you get `Could not listen for clients!`, another process is using the port → restart server or change port.
- Works fully with both `localhost` and `127.0.0.1`.

---

Name: *Lige Xiao*  
Student ID: *300339746*  
Course: **SEG2105 – Software Engineering**

