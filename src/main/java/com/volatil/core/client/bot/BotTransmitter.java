package com.volatil.core.client.bot;

import java.io.PrintWriter;
import java.net.Socket;

import com.volatil.core.client.Client;
import com.volatil.core.client.Transmitter;
import com.volatil.core.utils.MessageQueue;

public class BotTransmitter extends Transmitter {
  private int SLEEP_TIME = 100;
  protected PrintWriter out;
  private MessageQueue<String> messages;

  public BotTransmitter(Socket serverSocket, MessageQueue<String> input) {
    super(serverSocket);
    out = getOutWriter();
    messages = input;
  }

  public BotTransmitter(Socket serverSocket, MessageQueue<String> input, int sleepDuration) {
    super(serverSocket);
    out = getOutWriter();
    messages = input;
    SLEEP_TIME = sleepDuration;
  }

  @Override
  public void run() {
    while (!interrupted()) {
      try {
        if (messages.isEmpty()) {
          Thread.sleep(SLEEP_TIME);
          continue;
        }
        String message = messages.poll();
        if (Client.isKillCommand(message))
          interrupt();

        String response = generateResponse(message);
        if (response != null)
          out.println(response);

      } catch (InterruptedException e) {
        break;
      }
    }
    cleanup();
  }

  /**
   * Default response for bots is echoing the incoming message.
   * 
   * @param message The incoming message.
   * @return String The same message.
   */
  protected String generateResponse(String message) {
    return message;
  }

  @Override
  public void cleanup() {
    out.println("EXIT");
    super.cleanup();
  }

}
