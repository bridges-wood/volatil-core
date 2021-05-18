package volatil.utils;

public class CommandParser {
  String[] args;
  Logger log;

  public CommandParser(String[] args, Logger logger) {
    this.args = args;
    log = logger;
  }

  /**
   * Extracts the integer value of a given flag from command line arguments.
   * 
   * @param flag
   * @return int
   */
  public int parseIntFlag(String flag) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals(flag)) {
        try {
          return Integer.parseInt(args[i + 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
          log.error("No value present for flag: " + flag + " .");
        } catch (NumberFormatException e) {
          log.error("Improper format for flag: " + flag + " .");
        }
      }
    }
    return -1;
  }

  /**
   * Extracts the string value of a given flag from command line arguments.
   * 
   * @param flag
   * @return String
   */
  public String parseStringFlag(String flag) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals(flag)) {
        try {
          return args[i + 1];
        } catch (ArrayIndexOutOfBoundsException e) {
          log.error("No value present for flag: " + flag + " .");
        }
      }
    }
    return null;
  }
}
