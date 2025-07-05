package com.sportygroup.feednormalizer.application.ports;

import com.sportygroup.feednormalizer.domain.message.StandardMessage;

public interface StandardMessageQueueFactory {
  <U extends StandardMessage> MessageQueue<U> getFor(U message);
}
