package org.example.config;

import cc.api.model.v1.resource.FibonacciNumberResource;
import org.example.rest.ExternalPost;
import org.example.rest.HelloWorld;
import org.example.rest.TriggerDeadlock;
import org.example.rest.UserResourceImpl;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        //all classes in the package org.example.rest are registered to expose REST api
        register(HelloWorld.class);
        register(FibonacciNumberResource.class);
        register(ExternalPost.class);
        register(UserResourceImpl.class);
        register(TriggerDeadlock.class);
    }
}
