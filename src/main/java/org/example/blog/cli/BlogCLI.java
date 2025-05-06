package org.example.blog.cli;

import org.example.blog.model.Post;
import org.example.blog.service.BlogService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BlogCLI {

    private final BlogService blogService;
    private final Scanner scanner;

    public BlogCLI(BlogService blogService) {
        this.blogService = blogService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to Simple Blog!");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readIntInput("Enter choice: ");

            switch (choice) {
                case 1:
                    createPost();
                    break;
                case 2:
                    listPosts();
                    break;
                case 3:
                    viewPost();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Exiting Simple Blog. Goodbye!");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("--- Blog Menu ---");
        System.out.println("1. Create New Post");
        System.out.println("2. List All Posts");
        System.out.println("3. View Post by ID");
        System.out.println("0. Exit");
        System.out.println("-----------------");
    }

    private void createPost() {
        System.out.println("\n--- Create New Post ---");
        String title = readStringInput("Enter post title: ");
        String content = readStringInput("Enter post content (end with empty line): ");
        // Multi-line input reading
        StringBuilder contentBuilder = new StringBuilder(content);
        String line;
        while(scanner.hasNextLine() && !(line = scanner.nextLine()).isEmpty()) {
            contentBuilder.append("\n").append(line);
        }

        try {
            Post createdPost = blogService.createPost(title, contentBuilder.toString());
            System.out.println("Post created successfully! ID: " + createdPost.getId());
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating post: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("An unexpected database error occurred: " + e.getMessage());
        }
    }

    private void listPosts() {
        System.out.println("\n--- All Blog Posts ---");
        List<Post> posts = blogService.getAllPostSummaries();
        if (posts.isEmpty()) {
            System.out.println("No posts found.");
        } else {
            posts.forEach(post ->
                    System.out.printf("ID: %d | Title: %s | Created: %s%n",
                            post.getId(), post.getTitle(), post.getCreatedAt())
            );
        }
        System.out.println("----------------------");
    }

    private void viewPost() {
        System.out.println("\n--- View Post Details ---");
        int id = readIntInput("Enter post ID to view: ");

        try {
            Optional<Post> postOptional = blogService.getPostDetails(id);
            if (postOptional.isPresent()) {
                System.out.println(postOptional.get().toDetailedString());
            } else {
                System.out.println("Post with ID " + id + " not found.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("An unexpected database error occurred: " + e.getMessage());
        }
        System.out.println("-------------------------");
    }

    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readIntInput(String prompt) {
        int input = -1;
        while (input < 0) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                if (input < 0) System.out.println("Please enter a non-negative number.");
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.");
                scanner.next();
                input = -1;
            } finally {
                if(scanner.hasNextLine()) scanner.nextLine();
            }

        }
        return input;
    }
}