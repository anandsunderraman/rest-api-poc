package org.example.external.service;

import cc.api.model.v1.model.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PostsService {

    @Value("${external.posts.url}")
    private String externalPostsURL;

    public List<Post> getPosts() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Post>> postResponse = restTemplate.exchange(externalPostsURL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>(){});
        return postResponse.getBody();
    }
}
