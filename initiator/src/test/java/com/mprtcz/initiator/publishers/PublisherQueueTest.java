package com.mprtcz.initiator.publishers;

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
    private final PublisherQueue<String> underTest = new PublisherQueue<>(
            QUEUE_CAPACITY,
            DLQ_QUEUE_CAPACITY,
            MAX_RETRIES,
            TTL,
            clock);


    @BeforeEach
    void setUp() {
        when(clock.instant()).thenReturn(Instant.now());
    }

    @Test
    void shouldAddElementToQueue_success() {
        String message = "Hello";

        underTest.publish(message);

        var result = underTest.take();
        var expected = new QueueMessage<>(message);
        expected.setInProgress();
        assertEquals(expected, result);
    }

    @Test
    void shouldAddElementToQueue_successNoAcknowledge() {
        String message = "Hello";
        underTest.publish(message);

        var result = underTest.take();

        var expected = new QueueMessage<>(message);
        expected.setInProgress();
        assertEquals(expected, result);
        assertEquals(1, underTest.queueSize());
    }

    @Test
    void shouldTakeElementToQueue_successWithAcknowledge() {
        String message = "Hello";
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
        String message = "Hello";
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
        String message = "Hello";
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
        String message = "Hello";
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
}