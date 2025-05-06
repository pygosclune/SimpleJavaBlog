package org.example.blog.repository;

import org.example.blog.config.DatabaseManager;
import org.example.blog.model.Post;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset; // Use consistent time zone
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlitePostRepository implements PostRepository {

    @Override
    public Post save(Post post) {
        String sql = "INSERT INTO posts(title, content) VALUES(?, ?)";
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating post failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new Post(id, post.getTitle(), post.getContent(), now, now);
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving post: " + e.getMessage());
            throw new RuntimeException("Database error during post save", e);
        }
    }

    @Override
    public Optional<Post> findById(int id) {
        String sql = "SELECT id, title, content, createdAt, updatedAt FROM posts WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Post post = mapResultSetToPost(rs);
                return Optional.of(post);
            } else {
                return Optional.empty(); // Not found
            }
        } catch (SQLException e) {
            System.err.println("Error finding post by ID: " + e.getMessage());
            throw new RuntimeException("Database error during findById", e);
        }
    }

    @Override
    public List<Post> findAllSummaries() {
        // Fetch only necessary columns for the summary list
        String sql = "SELECT id, title, createdAt, updatedAt FROM posts ORDER BY createdAt DESC";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Create Post object with null content for summary
                Post post = new Post(
                        rs.getInt("id"),
                        rs.getString("title"),
                        null,
                        rs.getTimestamp("createdAt").toLocalDateTime(),
                        rs.getTimestamp("updatedAt").toLocalDateTime()
                );
                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all posts: " + e.getMessage());
            throw new RuntimeException("Database error during findAllSummaries", e);
        }
        return posts;
    }

    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        return new Post(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getTimestamp("createdAt").toLocalDateTime(),
                rs.getTimestamp("updatedAt").toLocalDateTime()
        );
    }
}