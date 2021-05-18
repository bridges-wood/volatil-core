package volatil.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExitHandler extends Thread {
  BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
  private final int SLEEP_TIME = 100;
  Logger log = new Logger("Exit Handler");

  @Override
  public void run() {
    log.info("Ready.");
    while (!isInterrupted()) {
      try {
        if (!userInput.ready()) {
          Thread.sleep(SLEEP_TIME);
          continue;
        }
        String input = userInput.readLine();
        log.info(input);
        if (input.equals("EXIT")) {
          Thread.currentThread().interrupt();
        }
      } catch (IOException e) {
        log.error(e.getMessage());
      } catch (InterruptedException e) {
        log.info("Cleaning up...");
        cleanup();
        break;
      }
    }

  }

  public void cleanup() {
    try {
      userInput.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
