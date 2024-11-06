package com.mprtcz.transaction.publishers;

import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ToString
public class QueueMessage<T> implements Comparable<QueueMessage<T>> {
    private final T messageContent;
    private final Clock clock;
    private final AtomicInteger attempts = new AtomicInteger(0);
    private boolean isTaken = false;
    private Instant firstPollInstant;

    QueueMessage(T messageContent) {
        this.messageContent = messageContent;
        this.clock = Clock.systemUTC();
    }

    public QueueMessage(T messageContent, Clock clock) {
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

    void increaseAttempts() {
        attempts.incrementAndGet();
    }

    @Override
    public int compareTo(QueueMessage<T> o) {
        if (o == null) return 1;
        if (this.isTaken) return 1;
        return o.isTaken ? -1 : 0;
    }
}
