package com.sportygroup.feednormalizer.adapters.inbound;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.feednormalizer.application.ports.IncomingMessageNormalizer;
import com.sportygroup.feednormalizer.domain.ValidationError;
import com.sportygroup.feednormalizer.domain.message.StandardMessage;

public abstract class AbstractMessageNormalizer<
        I extends IncomingMessage<S>, S extends StandardMessage>
    implements IncomingMessageNormalizer {

  private final ObjectMapper mapper;
  private final Class<I> rawType;

  protected AbstractMessageNormalizer(ObjectMapper mapper, Class<I> rawType) {
    this.mapper = mapper;
    this.rawType = rawType;
  }

  @Override
  public final S toStandardMessage(JsonNode payload) {
    I raw = mapper.convertValue(payload, rawType);
    S std = raw.toStandardMessage();
    std.validate()
        .ifPresent(
            it -> {
              String joined = it.stream().map(ValidationError::error).collect(joining(","));
              throw new MessageUnprocessableException(joined);
            });
    return std;
  }
}
