package com.sportygroup.feednormalizer.adapters.inbound;

import com.sportygroup.feednormalizer.domain.message.StandardMessage;

/**
 * Marker class we use to detect all incoming message classes (Odds change from Provider Alpha, bet
 * settlement from Provider Beta etc.)
 */
public interface IncomingMessage<U extends StandardMessage> {

  U toStandardMessage();
}
