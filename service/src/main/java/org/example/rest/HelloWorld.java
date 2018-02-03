package org.example.rest;

import cc.api.model.v1.resource.HelloWorldResource;
import org.springframework.stereotype.Component;

@Component
public class HelloWorld implements HelloWorldResource {

    @Override
    public GetHelloWorldResponse getHelloWorld() throws Exception {
        return GetHelloWorldResponse.withPlainOK("Hello World");
    }
}
