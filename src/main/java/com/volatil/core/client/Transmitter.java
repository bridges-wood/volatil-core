package com.volatil.core.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.volatil.core.utils.Clean;
import com.volatil.core.utils.Logger;

public abstract class Transmitter extends Thread implements Clean {
  private final int SLEEP_TIME = 100;
  private PrintWriter outWriter;
  Logger log = new Logger("Transmitter");

  public Transmitter(Socket serverSocket) {
    try {
      outWriter = new PrintWriter(serverSocket.getOutputStream(), true);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    log.info("Ready.");
  }

  /**
   * Runs the transmitter instance.
   */
  @Override
  public abstract void run();

  /**
   * @return int The number of milliseconds any sleep should take.
   */
  public int getSleepTime() {
    return SLEEP_TIME;
  }

  /**
   * @return PrintWriter The writer for outgoing messages to the server.
   */
  public PrintWriter getOutWriter() {
    return outWriter;
  }

  @Override
  public void cleanup() {
    outWriter.close();
  }
}