package com.sportygroup.feednormalizer.application.ports;

import com.fasterxml.jackson.databind.JsonNode;
import com.sportygroup.feednormalizer.domain.message.StandardMessage;

/** Converts a incoming Json message from 3rd party provider to a standardized message U. */
public interface IncomingMessageNormalizer {
  boolean supports(MessageOrigin messageOrigin, JsonNode payload);

  StandardMessage toStandardMessage(JsonNode payload);
}
