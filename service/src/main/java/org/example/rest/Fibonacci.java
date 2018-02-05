package org.example.rest;

import cc.api.model.v1.model.Apimessage;
import cc.api.model.v1.resource.FibonacciNumberResource;
import org.example.service.FibonacciService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class Fibonacci implements FibonacciNumberResource {

    private static final Logger log = LoggerFactory.getLogger(Fibonacci.class);

    private final FibonacciService fibonacciService;

    @Autowired
    public Fibonacci(FibonacciService fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    @Override
    public GetFibonacciByNumberResponse getFibonacciByNumber(Integer number) throws Exception {

        if (number < 0) {
            String errorMessage = String.format("Cannot return Fibonacci series for negative number: %d", number);
            log.error(errorMessage);
            Apimessage error = new Apimessage()
                    .withMessage(errorMessage)
                    .withStatus(Apimessage.Status.BADREQUEST);
            return GetFibonacciByNumberResponse.withJsonBadRequest(error);
        }

        List<BigInteger> fibonacciSeries = fibonacciService.getFirstNFibonacciNumbers(number);
        return GetFibonacciByNumberResponse.withJsonOK(fibonacciSeries);
    }
}
