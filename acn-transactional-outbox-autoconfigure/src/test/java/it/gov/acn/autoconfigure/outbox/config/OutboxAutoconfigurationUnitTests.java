package it.gov.acn.autoconfigure.outbox.config;

import it.gov.acn.autoconfigure.outbox.ContextRunnerDecorator;
import it.gov.acn.autoconfigure.outbox.condition.requirement.ContextRequirementsValidator;
import it.gov.acn.outbox.scheduler.OutboxScheduler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;

public class OutboxAutoconfigurationUnitTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(BulkheadAutoConfiguration.class, OutboxAutoconfiguration.class));


    @BeforeEach
    void beforeEach() {

        ReflectionTestUtils.setField(ContextRequirementsValidator.class, "instance", null);
    }

    private final Class<?>[] autoConfigurations =
            {BulkheadAutoConfiguration.class, OutboxAutoconfiguration.class};

    @Test
    void should_provide_ThreadPoolTaskScheduler_when_no_other_TaskScheduler_present() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withDatasource()
                .withOutboxItemHandlerProvider()
                .withTransactionManager()
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).hasSingleBean(TaskScheduler.class);
                    Assertions.assertThat(context).hasBean("threadPoolTaskScheduler");
                    Assertions.assertThat(context.getBean("threadPoolTaskScheduler"))
                            .isInstanceOf(ThreadPoolTaskScheduler.class);
                });
    }

    @Test
    void should_not_provide_ThreadPoolTaskScheduler_when_other_TaskScheduler_present() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withDatasource()
                .withTransactionManager()
                .withOutboxItemHandlerProvider()
                .withTaskScheduler()
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).hasSingleBean(TaskScheduler.class);
                    Assertions.assertThat(context).hasBean("testTaskScheduler");
                    Assertions.assertThat(context.getBean("testTaskScheduler"))
                            .isInstanceOf(ThreadPoolTaskScheduler.class);
                });
    }

    @Test
    void should_provide_transactionalOutboxScheduler() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withDatasource()
                .withTransactionManager()
                .withOutboxItemHandlerProvider()
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).hasSingleBean(OutboxScheduler.class);
                    Assertions.assertThat(context).hasBean("transactionalOutboxScheduler");
                });
    }

    @Test
    void should_not_provide_transactionalOutboxScheduler_when_properties_not_configured() {
        ContextRunnerDecorator.create(contextRunner)
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).doesNotHaveBean(OutboxScheduler.class);
                    Assertions.assertThat(context).doesNotHaveBean("transactionalOutboxScheduler");
                });
    }

    @Test
    void should_not_provide_transactionalOutboxScheduler_when_context_fixedDelay_invalid() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withFixedDelay(-1)
                .withDatasource()
                .withTransactionManager()
                .withOutboxItemHandlerProvider()
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).doesNotHaveBean(OutboxScheduler.class);
                    Assertions.assertThat(context).doesNotHaveBean("transactionalOutboxScheduler");
                });
    }

    @Test
    void should_not_provide_transactionalOutboxScheduler_when_datasource_is_not_present() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withTransactionManager()
                .withOutboxItemHandlerProvider()
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).doesNotHaveBean(OutboxScheduler.class);
                    Assertions.assertThat(context).doesNotHaveBean("transactionalOutboxScheduler");
                });
    }

    @Test
    void should_not_provide_transactionalOutboxScheduler_when_item_handler_is_not_present() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withTransactionManager()
                .withDatasource()
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).doesNotHaveBean(OutboxScheduler.class);
                    Assertions.assertThat(context).doesNotHaveBean("transactionalOutboxScheduler");
                });
    }


    @Test
    void should_not_provide_transactionalOutboxScheduler_when_scheduler_not_enabled() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(false)
                .withDatasource()
                .withTransactionManager()
                .withOutboxItemHandlerProvider()
                .claim()
                .run(context -> {
                    Assertions.assertThat(context).doesNotHaveBean(OutboxScheduler.class);
                    Assertions.assertThat(context).doesNotHaveBean("transactionalOutboxScheduler");
                });
    }

}
