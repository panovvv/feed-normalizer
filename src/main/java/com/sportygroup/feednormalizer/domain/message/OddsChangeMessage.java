package com.sportygroup.feednormalizer.domain.message;

import com.sportygroup.feednormalizer.domain.ValidationError;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record OddsChangeMessage(String eventId, Map<Outcome, BigDecimal> odds)
    implements StandardMessage {
  @Override
  public Optional<List<ValidationError>> validate() {
    List<ValidationError> errors = new ArrayList<>();
    if (odds.isEmpty()) {
      errors.add(new ValidationError("Odds object is empty"));
    }
    // TBD other types of validation? Maybe we don't accept partial odds.
    if (!errors.isEmpty()) {
      return Optional.of(errors);
    }
    return Optional.empty();
  }
}
