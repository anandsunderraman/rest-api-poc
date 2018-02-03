package org.example.rest;

import cc.api.model.v1.model.Error;
import cc.api.model.v1.model.Post;
import cc.api.model.v1.resource.ExternalPostsResource;
import org.example.external.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalPost implements ExternalPostsResource {

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
            Error error = new Error()
                    .withMessage("Error obtaining response from external posts service")
                    .withErrorType(Error.ErrorType.SERVERERROR);
            return GetExternalPostsResponse.withJsonInternalServerError(error);
        }

        return GetExternalPostsResponse.withJsonOK(postList);
    }
}
