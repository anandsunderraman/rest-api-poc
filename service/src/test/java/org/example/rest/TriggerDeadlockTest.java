package org.example.rest;

import cc.api.model.v1.resource.DeadlockResource;
import org.example.service.DeadlockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TriggerDeadlockTest {

    DeadlockService deadlockService = mock(DeadlockService.class);
    TriggerDeadlock triggerDeadlock = new TriggerDeadlock(deadlockService);

    @Test
    public void triggerDeadLockReturns400ForInvalidData() throws Exception {
        //when timeout is negative REST endpoint must return 400
        DeadlockResource.GetDeadlockTriggerByTimeoutResponse response = triggerDeadlock.getDeadlockTriggerByTimeout(-1);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void triggerDeadLockReturns200ForValidInvocation() throws Exception {

        //given deadlock was triggered successfully
        when(deadlockService.triggerDeadLock(10L)).thenReturn(true);

        //when deadlock service returns true then return 200
        DeadlockResource.GetDeadlockTriggerByTimeoutResponse response = triggerDeadlock.getDeadlockTriggerByTimeout(10);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void triggerDeadLockReturns404ForInValidInvocation() throws Exception {

        //given deadlock was triggered successfully
        when(deadlockService.triggerDeadLock(10L)).thenReturn(false);

        //when deadlock service returns true then return 200
        DeadlockResource.GetDeadlockTriggerByTimeoutResponse response = triggerDeadlock.getDeadlockTriggerByTimeout(10);
        assertEquals(404, response.getStatus());
    }

}
