package com.sportygroup.feednormalizer.adapters.inbound;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.feednormalizer.application.ports.IncomingMessageNormalizer;
import com.sportygroup.feednormalizer.application.ports.IncomingMessageNormalizerFactory;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomingMessageNormalizerFactoryImpl implements IncomingMessageNormalizerFactory {
  private final List<IncomingMessageNormalizer> messageNormalizers;

  @Override
  public IncomingMessageNormalizer get(MessageOrigin messageOrigin, JsonNode payload) {
    return messageNormalizers.stream()
        .filter(it -> it.supports(messageOrigin, payload))
        .findFirst()
        .orElseThrow(
            () -> new MessageUnprocessableException("Can not convert incoming message payload"));
  }
}
