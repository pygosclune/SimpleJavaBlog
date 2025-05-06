package org.example.blog;

import org.example.blog.cli.BlogCLI;
import org.example.blog.config.DatabaseManager;
import org.example.blog.repository.PostRepository;
import org.example.blog.repository.SqlitePostRepository;
import org.example.blog.service.BlogService;

public class App {

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        PostRepository postRepository = new SqlitePostRepository();
        BlogService blogService = new BlogService(postRepository);
        BlogCLI blogCLI = new BlogCLI(blogService);
        blogCLI.run();
    }
}