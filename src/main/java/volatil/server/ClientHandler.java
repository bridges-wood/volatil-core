package volatil.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import volatil.utils.Clean;
import volatil.utils.Logger;
import volatil.utils.Message;

public class ClientHandler extends Thread implements Clean {
  private static final int SLEEP_TIME = 100;
  private Socket client;
  private BufferedReader in;
  private PrintWriter out;
  private Logger log;
  private Participants group;
  public int id;

  public ClientHandler(Socket client, int id, Participants group) {
    this.id = id;
    this.log = new Logger(Integer.toString(id));
    this.client = client;
    this.group = group;
    log.info("Connected to client!");
    try {
      InputStreamReader clientStream = new InputStreamReader(client.getInputStream());
      this.in = new BufferedReader(clientStream);
      this.out = new PrintWriter(client.getOutputStream(), true);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    group.addParticipant(this);
  }

  @Override
  public void run() {
    while (!interrupted()) {
      try {
        if (!in.ready()) {
          Thread.sleep(SLEEP_TIME);
          continue;
        }
        String message = in.readLine();
        if (Message.isPrivateMessage(message)) {
          group.dispatchMessage(message, id);
          continue;
        } else if (message.contains("EXIT")) {
          interrupt();
        } else {
          log.info(message);
          group.dispatchMessage(message, id);
        }
      } catch (IOException e) {
        log.error(e.getMessage());
      } catch (InterruptedException e) {
        break;
      }
    }
    cleanup();
  }

  public void cleanup() {
    try {
      in.close();
      out.close();
      client.close();
      log.info("Socket closed.");
      group.quit(this);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Sends a message to the client connected to this handler.
   * 
   * @param message
   */
  public synchronized void sendMessage(String message) {
    out.println(message);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ClientHandler))
      return false;

    ClientHandler c = (ClientHandler) obj;
    return this.id == c.id;
  }
}
