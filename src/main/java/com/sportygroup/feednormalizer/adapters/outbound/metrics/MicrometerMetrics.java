package com.sportygroup.feednormalizer.adapters.outbound.metrics;

import com.sportygroup.feednormalizer.application.ports.MessageMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MicrometerMetrics implements MessageMetrics {
  private final MeterRegistry registry;

  @Override
  public void increment(String endpoint, boolean success) {
    registry
        .counter(
            "messages_processed", "endpoint", endpoint, "status", success ? "success" : "failure")
        .increment();
  }
}
