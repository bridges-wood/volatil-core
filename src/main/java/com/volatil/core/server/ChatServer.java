package com.volatil.core.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.volatil.core.utils.CommandParser;
import com.volatil.core.utils.ExitHandler;
import com.volatil.core.utils.Logger;

public class ChatServer {
  private static final int DEFAULT_PORT = 14001;
  private static int nextID = 0;
  private static final Logger log = new Logger("Server");
  private int port;
  private ServerSocket serverSocket;
  private ExitHandler ex = new ExitHandler();
  private static Participants group = new Participants();

  ChatServer(int port) {
    this.port = port;
    try {
      this.serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(200);
    } catch (IOException e) {
      log.error(e.getMessage());
      System.exit(1);
    }
  }

  private void start() {
    log.info("Started on port " + port);
    ex.start();

    while (ex.isAlive()) { // Until exit command is received...
      try {
        Socket client = serverSocket.accept();
        ClientHandler c = new ClientHandler(client, nextID, group);
        c.start(); // Create and start new clients
        nextID++;
        log.info("Ready to accept new connections.");
      } catch (SocketTimeoutException e) {
        continue;
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }
    cleanup();
  }

  private void cleanup() {
    group.releaseAll();
    try {
      serverSocket.close();
    } catch (IOException e) {
      log.error("Could not close server socket.");
    }
  }

  /**
   * Runs the server on the given port or 14001.
   * 
   * @param args Any command line args.
   */
  public static void main(String[] args) {
    CommandParser cp = new CommandParser(args, log);
    int port = cp.parseIntFlag("-csp");
    if (port == -1)
      port = DEFAULT_PORT;
    ChatServer s = new ChatServer(port);
    s.start();
  }
}
