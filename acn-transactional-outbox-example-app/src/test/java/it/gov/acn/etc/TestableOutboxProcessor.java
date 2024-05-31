package it.gov.acn.etc;

import it.gov.acn.outbox.core.configuration.OutboxConfiguration;
import it.gov.acn.outbox.core.processor.OutboxProcessor;
import it.gov.acn.outbox.core.processor.OutboxProcessorFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class TestableOutboxProcessor extends OutboxProcessor {
  public TestableOutboxProcessor(OutboxConfiguration outboxConfiguration) {
    super(
        outboxConfiguration.getBackoffBase(),
        outboxConfiguration.getMaxAttempts(),
        outboxConfiguration.getDataProvider(),
        outboxConfiguration.getOutboxItemHandlerProvider(),
        outboxConfiguration.getLockingProvider()
    );
  }
  public void doProcess() {
    super.doProcess();
  }
}
