package com.dilaverdemirel.spring.outbox.listener;

import com.dilaverdemirel.spring.outbox.dto.OutboxMessageEventMetaData;
import com.dilaverdemirel.spring.outbox.service.OutboxMessagePublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author dilaverdemirel
 * @since 17.05.2020
 */
@Slf4j
@Component
public class OutboxMessagePublisher {

    private final OutboxMessagePublisherService outboxMessagePublisherService;

    public OutboxMessagePublisher(OutboxMessagePublisherService outboxMessagePublisherService) {
        this.outboxMessagePublisherService = outboxMessagePublisherService;
    }

    @TransactionalEventListener(classes = {OutboxMessageEventMetaData.class}, phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOutboxMessageSave(OutboxMessageEventMetaData outboxMessageEventMetaData) {
        log.debug("Outbox message is publishing, meta data is {}", outboxMessageEventMetaData);
        outboxMessagePublisherService.publishById(outboxMessageEventMetaData.getMessageId());
    }
}
