package volatil.server;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import volatil.utils.Logger;
import volatil.utils.Message;

public class Participants {
  private static LinkedList<ClientHandler> clients = new LinkedList<ClientHandler>();
  private Logger log = new Logger("Participants");

  /**
   * Determines the appropriate routing policy to use on a given message.
   * 
   * @param message The message to be routed.
   * @param origin  The id of the client that produced the message.
   */
  public synchronized void dispatchMessage(String message, int origin) {
    String recipient = extractDestination(message);

    if (recipient == Message.NON_EXISTENT) {
      broadcast(message, origin);
    } else {
      try {
        privateMessage(message, origin, Integer.parseInt(recipient));
      } catch (NumberFormatException e) {
        log.error(e.getMessage());
      }
    }

  }

  /**
   * Sends a given message to all clients connected.
   * 
   * @param message
   * @param origin  The client that sent the message.
   */
  public synchronized void broadcast(String message, int origin) {
    for (ClientHandler c : clients) {
      if (c.id != origin)
        c.sendMessage("[" + origin + "] " + message);
    }
  }

  /**
   * Sends a given message to one of all clients connected.
   * 
   * @param message
   * @param destination The intended recipient of the message.
   */
  public synchronized void privateMessage(String message, int origin, int destination) {
    for (ClientHandler c : clients) {
      if (c.id == destination)
        c.sendMessage("[" + origin + "] " + message);
    }
  }

  /**
   * Adds a new client to the list of clients connected.
   * 
   * @param newClient
   */
  public void addParticipant(ClientHandler newClient) {
    for (ClientHandler c : clients) {
      c.sendMessage("[Server] User " + newClient.id + " joined the chat.");
    }
    synchronized (clients) {
      clients.add(newClient);
    }
  }

  /**
   * Removes a given client from this list of clients connected.
   * 
   * @param leavingClient
   */
  public void quit(ClientHandler leavingClient) {
    synchronized (clients) {
      clients.remove(leavingClient);
    }
    for (ClientHandler c : clients) {
      c.sendMessage("[Server] User " + leavingClient.id + " left the chat.");
    }
  }

  /**
   * Causes all connected clients to exit, and all client handlers to die.
   */
  public synchronized void releaseAll() {
    for (ClientHandler c : clients) {
      c.sendMessage("[Server] EXIT");
      c.interrupt();
    }
  }

  /**
   * Determines the destination for a given message, if it has one.
   * 
   * @param message
   * @return String The intended destination of the message.
   */
  private String extractDestination(String message) {
    Pattern userPattern = Pattern.compile("\\[(.*?)\\]");
    // Regex selects all text between square brackets.
    Matcher patternMatcher = userPattern.matcher(message);
    if (patternMatcher.find()) {
      String group = patternMatcher.group();
      return group.substring(1, group.length() - 1);
    } else {
      return Message.NON_EXISTENT;
    }
  }
}
