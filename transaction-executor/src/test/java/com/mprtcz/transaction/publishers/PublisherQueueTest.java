package com.mprtcz.transaction.publishers;

import com.mprtcz.statusholder.controller.PaymentStatusController;
import com.mprtcz.transaction.dto.Identifiable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublisherQueueTest {
    private static final int QUEUE_CAPACITY = 10;
    private static final int DLQ_QUEUE_CAPACITY = 10;
    private static final long MAX_RETRIES = 3;
    private static final long TTL = 30;
    public static final Clock clock = mock(Clock.class);
    private final PublisherQueue<IdentifiableString> underTest = new PublisherQueue<>(
            QUEUE_CAPACITY,
            DLQ_QUEUE_CAPACITY,
            MAX_RETRIES,
            TTL,
            clock,
            mock(PaymentStatusController.class));


    @BeforeEach
    void setUp() {
        when(clock.instant()).thenReturn(Instant.now());
    }

    @Test
    void shouldAddElementToQueue_success() {
        var message = new IdentifiableString("Hello");

        underTest.publish(message);

        var result = underTest.take();
        var expected = new QueueMessage<>(message);
        expected.setInProgress();
        assertEquals(expected, result);
    }

    @Test
    void shouldAddElementToQueue_successNoAcknowledge() {
        var message = new IdentifiableString("Hello");
        underTest.publish(message);

        var result = underTest.take();

        var expected = new QueueMessage<>(message);
        expected.setInProgress();
        assertEquals(expected, result);
        assertEquals(1, underTest.queueSize());
    }

    @Test
    void shouldTakeElementToQueue_successWithAcknowledge() {
        var message = new IdentifiableString("Hello");
        underTest.publish(message);

        var result = underTest.take();
        underTest.acknowledge(result);

        var expected = new QueueMessage<>(message);
        expected.setInProgress();
        assertEquals(expected, result);
        assertEquals(0, underTest.queueSize());
    }

    @Test
    void shouldTakeElementToQueue_multipleTakesNoAcknowledge_lessThanRetryLimit() {
        var message = new IdentifiableString("Hello");
        underTest.publish(message);

        var result = underTest.take();
        var result2 = underTest.take();
        var result3 = underTest.take();

        var expected = new QueueMessage<>(message);
        expected.setInProgress();
        assertEquals(expected, result);
        assertNull(result2);
        assertNull(result3);
        assertEquals(1, underTest.queueSize());
    }

    @Test
    void shouldTakeElementToQueue_multipleTakesNoAcknowledge_moreThanRetryLimit() {
        var message = new IdentifiableString("Hello");
        underTest.publish(message);

        Instant first = Instant.now();
        Instant second = first.plusSeconds(TTL + 5);

        when(clock.instant()).thenReturn(first, second);

        var result = underTest.take();
        var result2 = underTest.take();

        var expected = new QueueMessage<>(message);
        expected.increaseAttempts();
        expected.increaseAttempts();

        assertEquals(expected, result);
        assertNull(result2);
        assertEquals(1, underTest.queueSize());
    }

    @Test
    void shouldTakeElementToQueue_multipleTakesNoAcknowledge_exceedAttempts() {
        var message = new IdentifiableString("Hello");
        underTest.publish(message);
        Instant first = Instant.now();
        Instant second = first.plusSeconds(TTL + 5);
        Instant third = second.plusSeconds(TTL + 5);
        Instant fourthAndAfter = third.plusSeconds(TTL + 5);
        when(clock.instant()).thenReturn(first, second, third, fourthAndAfter);

        underTest.take();
        underTest.take();
        underTest.take();
        underTest.take();

        assertEquals(0, underTest.queueSize());
        assertEquals(1, underTest.getAllDlqMessages().size());
    }
    
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    private static class IdentifiableString implements Identifiable {
        private String content;
     
        @Override
        public String getId() {
            return "";
        }
    }
}