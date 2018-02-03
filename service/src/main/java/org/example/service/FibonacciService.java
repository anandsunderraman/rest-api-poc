package org.example.service;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class FibonacciService {

    public List<BigInteger> getFirstNFibonacciNumbers(int expectedSize) {

        List<BigInteger> fibonacciSeries = new ArrayList<>();

        if (expectedSize == 1) {
            fibonacciSeries.add(BigInteger.ZERO);
        } else if (expectedSize == 2) {
            fibonacciSeries.add(BigInteger.ZERO);
            fibonacciSeries.add(BigInteger.ONE);
        } else if (expectedSize > 2) {
            fibonacciSeries.add(BigInteger.ZERO);
            fibonacciSeries.add(BigInteger.ONE);
            computeFibonacci(expectedSize, fibonacciSeries);
        }

        return fibonacciSeries;
    }

    private void computeFibonacci(int expectedSize, List<BigInteger> fibonacciSeries) {
        int seriesSize = fibonacciSeries.size();
        BigInteger lastNumber = fibonacciSeries.get(seriesSize - 1);
        BigInteger penultimateNumber = fibonacciSeries.get(seriesSize - 2);
        fibonacciSeries.add(lastNumber.add(penultimateNumber));

        if (fibonacciSeries.size() < expectedSize) {
            computeFibonacci(expectedSize, fibonacciSeries);
        }
    }



}
