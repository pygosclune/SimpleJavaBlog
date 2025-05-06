package org.example.blog.service;

import org.example.blog.model.Post;
import org.example.blog.repository.PostRepository;

import java.util.List;
import java.util.Optional;

public class BlogService {

    private final PostRepository postRepository;

    public BlogService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(String title, String content) {
        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Title and content cannot be empty.");
        }
        Post newPost = new Post(title, content);
        return postRepository.save(newPost);
    }

    public List<Post> getAllPostSummaries() {
        return postRepository.findAllSummaries();
    }

    public Optional<Post> getPostDetails(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Post ID must be positive.");
        }
        return postRepository.findById(id);
    }

    // TODO update/delete
}