package com.volatil.core.client.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import com.volatil.core.client.Receiver;
import com.volatil.core.utils.Logger;
import com.volatil.core.utils.MessageQueue;

public class BotReceiver extends Receiver {
  private int SLEEP_TIME = 100;
  private BufferedReader in;
  private MessageQueue<String> messages;
  private Logger log;

  public BotReceiver(Socket serverSocket, MessageQueue<String> output, Logger log, int sleepDuration) {
    super(serverSocket);
    in = getInReader();
    messages = output;
    this.log = log;
    SLEEP_TIME = sleepDuration;
  }

  public BotReceiver(Socket serverSocket, MessageQueue<String> output, Logger log) {
    super(serverSocket);
    in = getInReader();
    messages = output;
    this.log = log;
  }

  @Override
  public void run() {
    while (!isInterrupted()) {
      try {
        if (!in.ready()) {
          Thread.sleep(SLEEP_TIME);
          continue;
        }
        String message = in.readLine();
        messages.add(message);
      } catch (IOException e) {
        log.error(e.getMessage());
      } catch (InterruptedException e) {
        break;
      }
    }
    super.cleanup();
  }
}