package com.sportygroup.feednormalizer.application.ports;

/**
 * Contains full information about incoming message origin. Right now this is just Provider but
 * could be extended in the future.
 */
public record MessageOrigin(Provider provider) {}
