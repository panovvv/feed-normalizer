package com.sportygroup.feednormalizer.adapters.inbound;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.feednormalizer.application.ports.IncomingMessageNormalizer;
import com.sportygroup.feednormalizer.application.ports.IncomingMessageNormalizerFactory;
import com.sportygroup.feednormalizer.application.ports.MessageOrigin;
import com.sportygroup.feednormalizer.application.ports.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IncomingMessageNormalizerFactoryTest {

  @Autowired private IncomingMessageNormalizerFactory normalizerFactory;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void validAlphaOddsChangeNormalizerFound() throws Exception {
    // given: a valid Odds change JSON payload for Provider.ALPHA
    String json =
        """
    {
      "msg_type": "odds_update",
      "event_id": "ev123",
      "values": {
        "1": 2.0,
        "X": 3.1,
        "2": 3.8
      }
    }
    """;
    JsonNode payload = objectMapper.readTree(json);

    // when: asking the factory for a normalizer
    IncomingMessageNormalizer normalizer =
        normalizerFactory.get(new MessageOrigin(Provider.ALPHA), payload);

    // then: a non-null normalizer is returned that supports the payload.
    assertNotNull(normalizer, "Expected a normalizer for typeA messages");
    assertTrue(
        normalizer.supports(new MessageOrigin(Provider.ALPHA), payload),
        "Normalizer should support this payload");
  }

  // TBD other messages

  /** Given when asking the factory for a normalizer, */
  @Test
  void missingMsgTypeThrows() throws Exception {
    // given: a JSON payload missing the msg_type field
    String json =
        """
    {
      "event_id": "ev123",
      "values": {
        "1": 2.0,
        "X": 3.1,
        "2": 3.8
      }
    }
    """;

    // when: asking the factory for a normalizer
    JsonNode payload = objectMapper.readTree(json);

    // then: a UnifierNotFoundException is thrown.
    assertThrows(
        MessageUnprocessableException.class,
        () -> normalizerFactory.get(new MessageOrigin(Provider.ALPHA), payload));
  }
}
