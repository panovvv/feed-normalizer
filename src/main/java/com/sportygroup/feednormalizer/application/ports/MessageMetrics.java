package com.sportygroup.feednormalizer.application.ports;

public interface MessageMetrics {
  void increment(String endpoint, boolean success);
}
