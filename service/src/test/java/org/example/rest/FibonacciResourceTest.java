package org.example.rest;

import cc.api.model.v1.resource.FibonacciNumberResource;
import org.example.service.FibonacciService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FibonacciResourceTest {

    FibonacciService fibonacciService = mock(FibonacciService.class);
    Fibonacci fibonacciRESTResource = new Fibonacci(fibonacciService);

    @Test
    public void resourceThrows400ForNegativeNumber() throws Exception {

        //when REST resource is invoked with negative number
        FibonacciNumberResource.GetFibonacciByNumberResponse fibonacciByNumberResponse = fibonacciRESTResource.getFibonacciByNumber(-1);

        //then verify REST resource returns 404
        assertEquals(fibonacciByNumberResponse.getStatus(), 400);
    }
}
