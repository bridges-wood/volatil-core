package volatil.client;

import java.io.IOException;
import java.net.Socket;

import volatil.utils.Clean;
import volatil.utils.CommandParser;
import volatil.utils.Logger;
import volatil.utils.Message;

public abstract class Client implements Clean {
  private final String DEFAULT_IP = "localhost";
  private final int DEFAULT_PORT = 14001;
  private final static String KILL_COMMAND = "EXIT";

  private Socket serverSocket;
  private Logger log = new Logger("Client");
  public Receiver incoming;
  public Transmitter outgoing;

  public Client(String[] args) {
    CommandParser cp = new CommandParser(args, log);
    String ip = getIP(cp);
    int port = getPort(cp);
    try {
      serverSocket = new Socket(ip, port);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Parses the IP address specified in the command line arguments. If there is no
   * address present, default to localhost.
   * 
   * @param cp CommandParser instance.
   * @return String The IP address from the command line arguments or localhost.
   */
  private String getIP(CommandParser cp) {
    String ip = cp.parseStringFlag("-cca");
    if (ip == null)
      ip = DEFAULT_IP;
    return ip;
  }

  /**
   * Parses the port specified in the command line arguments. If there is no port
   * present, default to 14001.
   * 
   * @param cp CommandParser instance.
   * @return int The port from the command line arguments or 14001.
   */
  private int getPort(CommandParser cp) {
    int port = cp.parseIntFlag("-ccp");
    if (port < 0 || port > 65535)
      port = DEFAULT_PORT;
    return port;
  }

  /**
   * Determines whether a message from the server is an instruction for the client
   * to kill itself.
   * 
   * @param message The message from the server.
   * @return boolean Whether or not the message is an instruction to die.
   */
  public static boolean isKillCommand(String message) {
    if (Message.extractOrigin(message).equals("Server") && message.contains(KILL_COMMAND)) {
      return true;
    }
    return false;
  }

  /**
   * Starts the client thread.
   */
  public abstract void start();

  /**
   * The socket which is connected to the chat server.
   * 
   * @return Socket
   */
  public Socket getServerSocket() {
    return serverSocket;
  }

  /**
   * The logger instance for this client.
   * 
   * @return Logger
   */
  public Logger log() {
    return log;
  }

  @Override
  public void cleanup() {
    try {
      serverSocket.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
