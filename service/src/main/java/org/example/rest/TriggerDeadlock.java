package org.example.rest;

import cc.api.model.v1.model.Apimessage;
import cc.api.model.v1.resource.DeadlockResource;
import org.example.service.DeadlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TriggerDeadlock implements DeadlockResource {

    private static final Logger log = LoggerFactory.getLogger(TriggerDeadlock.class);

    private final DeadlockService deadlockService;

    @Autowired
    public TriggerDeadlock(DeadlockService deadlockService) {
        this.deadlockService = deadlockService;
    }

    @Override
    public GetDeadlockTriggerByTimeoutResponse getDeadlockTriggerByTimeout(Integer timeout) throws Exception {

        //timeout must be between 1 and 60 seconds
        if (timeout  < 1 || timeout > 60) {
            String errorMessage = String.format("timeout: %d must be between 0 and 60", timeout);
            log.error(errorMessage);
            Apimessage badRequest = new Apimessage()
                    .withMessage(errorMessage)
                    .withStatus(Apimessage.Status.BADREQUEST);

            return GetDeadlockTriggerByTimeoutResponse.withJsonBadRequest(badRequest);
        }

        boolean wasTriggered = deadlockService.triggerDeadLock(new Long(timeout));

        if (wasTriggered) {
            Apimessage apimessage = new Apimessage()
                    .withMessage(String.format("Deadlock with timeout: %d was triggered successfully", timeout))
                    .withStatus(Apimessage.Status.SUCCESS);
            return GetDeadlockTriggerByTimeoutResponse.withJsonOK(apimessage);
        } else {
            String message = "Looks like the deadlock was already triggered";
            log.info(message);
            Apimessage notFound = new Apimessage()
                    .withMessage(message)
                    .withStatus(Apimessage.Status.NOTFOUND);
            return GetDeadlockTriggerByTimeoutResponse.withJsonNotFound(notFound);
        }
    }

    @Override
    public GetDeadlockDetectResponse getDeadlockDetect() throws Exception {
        Optional<List<String>> deadLockedThreads = deadlockService.detectDeadLock();

        if (deadLockedThreads.isPresent()) {
            return GetDeadlockDetectResponse.withJsonOK(deadLockedThreads.get());
        } else {
            String message = "No threads in deadlock were detected";
            log.info(message);
            Apimessage notFound = new Apimessage()
                    .withMessage(message)
                    .withStatus(Apimessage.Status.NOTFOUND);

            return GetDeadlockDetectResponse.withJsonNotFound(notFound);
        }
    }
}
