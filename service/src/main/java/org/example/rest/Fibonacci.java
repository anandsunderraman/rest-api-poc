package org.example.rest;

import cc.api.model.v1.model.Apimessage;
import cc.api.model.v1.resource.FibonacciNumberResource;
import org.example.service.FibonacciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class Fibonacci implements FibonacciNumberResource {

    private final FibonacciService fibonacciService;

    @Autowired
    public Fibonacci(FibonacciService fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    @Override
    public GetFibonacciByNumberResponse getFibonacciByNumber(Integer number) throws Exception {

        if (number < 0) {
            Apimessage error = new Apimessage()
                    .withMessage("Cannot return Fibonacci series for negative numbers")
                    .withStatus(Apimessage.Status.BADREQUEST);
            return GetFibonacciByNumberResponse.withJsonBadRequest(error);
        }

        List<BigInteger> fibonacciSeries = fibonacciService.getFirstNFibonacciNumbers(number);
        return GetFibonacciByNumberResponse.withJsonOK(fibonacciSeries);
    }
}
