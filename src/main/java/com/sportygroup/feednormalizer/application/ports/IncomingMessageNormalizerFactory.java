package com.sportygroup.feednormalizer.application.ports;

import com.fasterxml.jackson.databind.JsonNode;

public interface IncomingMessageNormalizerFactory {
  IncomingMessageNormalizer get(MessageOrigin messageOrigin, JsonNode payload);
}
