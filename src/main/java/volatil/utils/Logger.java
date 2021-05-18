package volatil.utils;

public class Logger {
  private String source;

  public Logger(String name) {
    this.source = "[" + name + "]" + " ";
  }

  /**
   * Logs the given data to standard out, reflecting its origin.
   * 
   * @param data
   */
  public void info(Object data) {
    System.out.println(source + data.toString());
  }

  /**
   * Logs the given data to error out, reflecting its origin.
   * 
   * @param data
   */
  public void error(Object data) {
    System.err.println(source + data.toString());
  }
}
