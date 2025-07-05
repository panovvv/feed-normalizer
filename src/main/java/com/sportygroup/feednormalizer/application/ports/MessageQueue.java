package com.sportygroup.feednormalizer.application.ports;

import com.sportygroup.feednormalizer.domain.message.StandardMessage;

/** Queues or publishes a standardized message U. */
public interface MessageQueue<U extends StandardMessage> {
  Class<U> getMessageType();

  void queueMessage(U message);
}
