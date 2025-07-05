package com.sportygroup.feednormalizer.adapters.inbound;

import com.sportygroup.feednormalizer.application.ports.MessageMetrics;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MessageMetricsAspect {
  private static final String CONTROLLER_POINTCUT =
      """
        execution(public * com.sportygroup.feednormalizer.adapters.inbound..*(..))
        && (
          @within(org.springframework.stereotype.Controller)
          || @within(org.springframework.web.bind.annotation.RestController)
        )
        """;

  private final MessageMetrics metrics;

  @AfterReturning(pointcut = CONTROLLER_POINTCUT, returning = "result")
  public void onSuccess(JoinPoint jp, Object result) {
    String endpoint = jp.getSignature().toShortString();
    metrics.increment(endpoint, true);
  }

  @AfterThrowing(pointcut = CONTROLLER_POINTCUT, throwing = "ex")
  public void onError(JoinPoint jp, Throwable ex) {
    String endpoint = jp.getSignature().toShortString();
    metrics.increment(endpoint, false);
  }
}
