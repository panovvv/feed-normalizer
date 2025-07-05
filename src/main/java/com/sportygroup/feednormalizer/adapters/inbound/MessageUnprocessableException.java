package com.sportygroup.feednormalizer.adapters.inbound;

/**
 * Thrown when an incoming message from a 3rd party provider does not correspond to the expected
 * format, barring plain syntax exceptions (malformed or empty JSON)
 *
 * @see <a href="https://beeceptor.com/docs/concepts/400-vs-422/">400 vs 422</a>
 */
public class MessageUnprocessableException extends RuntimeException {
  public MessageUnprocessableException(String message) {
    super(message);
  }
}
