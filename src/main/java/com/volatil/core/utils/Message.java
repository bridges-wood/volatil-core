package com.volatil.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
  public static final String NON_EXISTENT = "ERROR";

  /**
   * Determines which user sent a given message.
   * 
   * @param message
   * @return String The user that sent the message.
   */
  public static String extractOrigin(String message) {
    Pattern userPattern = Pattern.compile("\\[(.*?)\\]");
    // Regex selects all text between square brackets.
    Matcher patternMatcher = userPattern.matcher(message);
    if (patternMatcher.find()) {
      String group = patternMatcher.group();
      return group.substring(1, group.length() - 1);
    } else {
      return NON_EXISTENT;
    }
  }

  /**
   * Determines which user was the intended recipient for a private message.
   * 
   * @param message
   * @return String The intended recipient of the message.
   */
  public static String extractDestination(String message) {
    Pattern userPattern = Pattern.compile("\\[(.*?)\\]");
    // Regex selects all text between square brackets.
    Matcher patternMatcher = userPattern.matcher(message);
    if (patternMatcher.find()) {
      String group = patternMatcher.group(2);
      return group.substring(1, group.length() - 1);
    } else {
      return NON_EXISTENT;
    }
  }

  /**
   * Generates a private message in the format the server would understand.
   * 
   * @param message     The actual content of the message.
   * @param destination The id of the client the message is going to.
   * @return String The formatted private message to be consumed by the server and
   *         appropriately directed.
   */
  public static String generatePrivateMessage(String message, String destination) {
    return "[" + destination + "] " + message;
  }

  /**
   * Removes all routing information from a private message, leaving only the
   * content behind.
   * 
   * @param message The private message with all routing information.
   * @return String The content of the private message.
   */
  public static String parsePrivateMessage(String message) {
    String parsed = message.replaceAll("\\[(.*?)\\]", "");
    return parsed.substring(2);
  }

  /**
   * Determines whether or not a given string is likely to be a private message.
   * This is used on the server to determine which routing policy to use.
   * 
   * @param message
   * @return boolean
   */
  public static boolean isPrivateMessage(String message) {
    Pattern messagePattern = Pattern.compile("\\[(.*?)\\]");
    Matcher patternMatcher = messagePattern.matcher(message);
    return patternMatcher.results().count() == 1;
  }
}
