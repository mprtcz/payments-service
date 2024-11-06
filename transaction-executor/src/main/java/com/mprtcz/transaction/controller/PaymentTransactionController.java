package com.mprtcz.transaction.controller;

import com.mprtcz.transaction.dto.TransactionRequest;
import com.mprtcz.transaction.publishers.PublisherQueue;
import com.mprtcz.transaction.publishers.QueueMessage;
import com.mprtcz.transaction.service.PaymentTransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentTransactionController {
    private final PublisherQueue<TransactionRequest> publisherQueue;
    private final PaymentTransactionService paymentTransactionService;

    @Scheduled(fixedDelay = 1000)
    public void processQueueMessages() {
        QueueMessage<TransactionRequest> message;
        do {
            message = publisherQueue.take();
            if (message == null) {
                // Seems like the queue is functionally empty.
                break;
            }
            log.info("Message to process: {}", message);
            paymentTransactionService.processTransaction(message.getMessageContent());
            publisherQueue.acknowledge(message);
        } while (true);
    }
}
