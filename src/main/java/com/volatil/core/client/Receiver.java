package com.volatil.core.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.volatil.core.utils.Clean;
import com.volatil.core.utils.Logger;

public abstract class Receiver extends Thread implements Clean {
  private final int SLEEP_TIME = 100;
  private BufferedReader inReader;
  Logger log = new Logger("Receiver");

  public Receiver(Socket serverSocket) {
    try {
      inReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    log.info("Ready.");
  }

  /**
   * Runs the receiver instance.
   */
  @Override
  public abstract void run();

  @Override
  public void cleanup() {
    try {
      inReader.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * @return int The number of milliseconds any sleep call should take.
   */
  public int getSleepTime() {
    return SLEEP_TIME;
  }

  /**
   * @return BufferedReader The reader for incoming messages from the server.
   */
  public BufferedReader getInReader() {
    return inReader;
  }
}
