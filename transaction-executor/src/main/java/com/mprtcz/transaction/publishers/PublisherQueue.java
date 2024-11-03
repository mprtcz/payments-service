package com.mprtcz.transaction.publishers;

import com.mprtcz.statusholder.controller.PaymentStatusController;
import com.mprtcz.transaction.dto.Identifiable;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class PublisherQueue<T extends Identifiable> {
    private final PaymentStatusController paymentStatusController;

    private final long maxRetries;
    private final Duration TTL;
    private final Clock clock;

    private final BlockingQueue<QueueMessage<T>> queue;
    private final BlockingQueue<QueueMessage<T>> deadLetterQueue;

    public PublisherQueue(@Value("${application.config.queue.capacity}") int queueCapacity,
            @Value("${application.config.queue.dlq-capacity}") int dlqQueueCapacity,
            @Value("${application.config.queue.max-retries}") long maxRetries,
            @Value("${application.config.queue.ttl-seconds}") long ttl,
            Clock clock,
            PaymentStatusController paymentStatusController) {
        this.maxRetries = maxRetries;
        TTL = Duration.ofSeconds(ttl);
        this.clock = clock;
        this.paymentStatusController = paymentStatusController;

        queue = new ArrayBlockingQueue<>(queueCapacity);
        deadLetterQueue = new ArrayBlockingQueue<>(dlqQueueCapacity);
    }

    public boolean publish(T message) {
        return queue.offer(new QueueMessage<>(message, clock));
    }

    public QueueMessage<T> take() {
        var queueElement = queue.peek();
        if (Objects.isNull(queueElement)) {
            return null;
        }

        if (queueElement.getAttempts().get() >= maxRetries) {
            queue.remove(queueElement);
            deadLetterQueue.offer(queueElement);
            paymentStatusController.markTransactionAsFailed(queueElement.getMessageContent()
                    .getId());
            return null;
        }

        if (queueElement.isTaken() && isMessageTtlExceeded(queueElement)) {
            queueElement.setReturnToQueue();
            return null;
        }

        if (queueElement.isTaken()) {
            return null;
        }

        queueElement.setInProgress();
        return queueElement;
    }

    private boolean isMessageTtlExceeded(QueueMessage<T> queueMessage) {
        return Duration.between(queueMessage.getFirstPollInstant(),
                clock.instant()).compareTo(TTL) >= 0;
    }

    public void acknowledge(QueueMessage<T> message) {
        queue.remove(message);
    }

    public List<QueueMessage<T>> getAllDlqMessages() {
        List<QueueMessage<T>> messages = new ArrayList<>();
        while (deadLetterQueue.size() > messages.size()) {
            var queueElement = deadLetterQueue.element();
            messages.add(queueElement);
        }
        return messages;
    }

    @VisibleForTesting
    int queueSize() {
        return queue.size();
    }

}
