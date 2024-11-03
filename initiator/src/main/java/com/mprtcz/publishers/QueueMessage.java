package com.mprtcz.publishers;

import lombok.Getter;
import lombok.ToString;
import org.assertj.core.util.VisibleForTesting;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ToString
public class QueueMessage<T> {
    private final T messageContent;
    private final Clock clock;
    private final AtomicInteger attempts = new AtomicInteger(0);
    private boolean isTaken = false;
    private Instant firstPollInstant;

    @VisibleForTesting
    QueueMessage(T messageContent) {
        this.messageContent = messageContent;
        this.clock = Clock.systemUTC();
    }

    QueueMessage(T messageContent, Clock clock) {
        this.messageContent = messageContent;
        this.clock = clock;
    }

    public void setInProgress() {
        attempts.incrementAndGet();
        isTaken = true;
        firstPollInstant = clock.instant();
    }

    public void setReturnToQueue() {
        attempts.incrementAndGet();
        isTaken = false;
        firstPollInstant = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueueMessage<?> that = (QueueMessage<?>) o;
        return isTaken == that.isTaken && Objects.equals(messageContent,
                that.messageContent) && Objects.equals(attempts.get(),
                that.attempts.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageContent, attempts, isTaken);
    }

    @VisibleForTesting
    void increaseAttempts() {
        attempts.incrementAndGet();
    }
}
