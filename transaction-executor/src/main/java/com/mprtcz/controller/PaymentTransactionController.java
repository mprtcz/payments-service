package com.mprtcz.controller;

import com.mprtcz.controllers.dto.PaymentRequest;
import com.mprtcz.publishers.PublisherQueue;
import com.mprtcz.publishers.QueueMessage;
import com.mprtcz.service.PaymentTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentTransactionController {
    private final PublisherQueue<PaymentRequest> publisherQueue;
    private final PaymentTransactionService paymentTransactionService;

    @Scheduled(fixedDelay = 1000)
    public void processQueueMessages() {
        QueueMessage<PaymentRequest> message;
        do {
            message = publisherQueue.take();
            if (message == null) {
                // Seems like the queue is functionally empty.
                break;
            }
            paymentTransactionService.processTransaction(message.getMessageContent());
            publisherQueue.acknowledge(message);
        } while (message != null);
    }
}
