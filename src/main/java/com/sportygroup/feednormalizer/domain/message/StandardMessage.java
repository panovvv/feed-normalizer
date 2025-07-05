package com.sportygroup.feednormalizer.domain.message;

import com.sportygroup.feednormalizer.domain.ValidationError;
import java.util.List;
import java.util.Optional;

/**
 * Marker class we use to detect all standardized message classes (Odds change, bet settlement etc.)
 * Those messages are converted from incoming 3rd party provider messages and can be processed
 * internally.
 */
public interface StandardMessage {
  Optional<List<ValidationError>> validate();
}
