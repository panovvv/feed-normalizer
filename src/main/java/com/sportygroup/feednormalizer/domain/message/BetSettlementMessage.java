package com.sportygroup.feednormalizer.domain.message;

import com.sportygroup.feednormalizer.domain.ValidationError;
import java.util.List;
import java.util.Optional;

public record BetSettlementMessage(String eventId, Outcome outcome) implements StandardMessage {
  @Override
  public Optional<List<ValidationError>> validate() {
    return Optional.empty();
  }
}
