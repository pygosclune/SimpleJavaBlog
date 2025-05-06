package org.example.blog.repository;

import org.example.blog.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    /**
     * Saves a new post to the database.
     * @param post The post to save (without an ID).
     * @return The saved post with its generated ID and timestamps.
     */
    Post save(Post post);

    /**
     * Finds a post by its unique ID.
     * @param id The ID of the post.
     * @return An Optional containing the post if found, otherwise empty.
     */
    Optional<Post> findById(int id);

    /**
     * Retrieves all posts, typically ordered by creation date descending.
     * Might only fetch summary data (id, title, createdAt) for efficiency.
     * @return A list of all posts (or post summaries).
     */
    List<Post> findAllSummaries();

    // TODO Update/Delete
}