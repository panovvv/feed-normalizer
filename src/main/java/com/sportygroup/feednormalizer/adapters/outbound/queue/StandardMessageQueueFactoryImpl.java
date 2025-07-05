package com.sportygroup.feednormalizer.adapters.outbound.queue;

import com.sportygroup.feednormalizer.application.ports.MessageQueue;
import com.sportygroup.feednormalizer.application.ports.StandardMessageQueueFactory;
import com.sportygroup.feednormalizer.domain.message.StandardMessage;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StandardMessageQueueFactoryImpl implements StandardMessageQueueFactory {
  private final Map<Class<? extends StandardMessage>, MessageQueue<?>> lookup;

  public StandardMessageQueueFactoryImpl(List<MessageQueue<? extends StandardMessage>> queues) {
    this.lookup =
        queues.stream()
            .collect(Collectors.toMap(MessageQueue::getMessageType, Function.identity()));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <U extends StandardMessage> MessageQueue<U> getFor(U message) {
    MessageQueue<?> queue = lookup.get(message.getClass());
    if (queue == null) {
      throw new IllegalArgumentException("No queue for " + message.getClass());
    }
    return (MessageQueue<U>) queue;
  }
}
