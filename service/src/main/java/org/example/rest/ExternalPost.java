package org.example.rest;

import cc.api.model.v1.model.Apimessage;
import cc.api.model.v1.model.Post;
import cc.api.model.v1.resource.ExternalPostsResource;
import org.example.external.service.PostsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalPost implements ExternalPostsResource {

    private static final Logger log = LoggerFactory.getLogger(ExternalPost.class);

    private final PostsService postsService;

    @Autowired
    public ExternalPost(PostsService postsService) {
        this.postsService = postsService;
    }

    @Override
    public GetExternalPostsResponse getExternalPosts() throws Exception {

        List<Post> postList;

        try {
            postList = postsService.getPosts();
        } catch (Exception e) {
            String errorMessage = "Error obtaining response from external posts service";
            log.error(errorMessage, e);
            Apimessage error = new Apimessage()
                    .withMessage(errorMessage)
                    .withStatus(Apimessage.Status.SERVERERROR);
            return GetExternalPostsResponse.withJsonInternalServerError(error);
        }

        return GetExternalPostsResponse.withJsonOK(postList);
    }
}
