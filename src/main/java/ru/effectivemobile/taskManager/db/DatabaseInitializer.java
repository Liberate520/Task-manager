package ru.effectivemobile.taskManager.db;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        if (!tableExists("users")) {
            createTables();
            insertInitialData();
        }
    }

    private boolean tableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{tableName}, Integer.class);
        return count != null && count > 0;
    }

    private void createTables() {
        String createUsersTable = """
                CREATE TABLE users (
                    id BIGSERIAL PRIMARY KEY,
                    first_name VARCHAR(50),
                    last_name VARCHAR(50),
                    email VARCHAR(255) NOT NULL UNIQUE,
                    preferred_name VARCHAR(255) NOT NULL UNIQUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String createTasksTable = """
                CREATE TABLE tasks (
                    id BIGSERIAL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    description TEXT,
                    status VARCHAR(50),
                    priority VARCHAR(50),
                    author_id BIGINT,
                    assignee_id BIGINT,
                    FOREIGN KEY (author_id) REFERENCES users(id),
                    FOREIGN KEY (assignee_id) REFERENCES users(id)
                );
                """;

        String createCommentsTable = """
                CREATE TABLE comments (
                    id BIGSERIAL PRIMARY KEY,
                    text VARCHAR(1000) NOT NULL,
                    task_id BIGINT NOT NULL,
                    author_id BIGINT NOT NULL,
                    FOREIGN KEY (task_id) REFERENCES tasks(id),
                    FOREIGN KEY (author_id) REFERENCES users(id)
                );
                """;

        jdbcTemplate.execute(createUsersTable);
        jdbcTemplate.execute(createTasksTable);
        jdbcTemplate.execute(createCommentsTable);
    }

    private void insertInitialData() {
        String insertUserSql = "INSERT INTO users (first_name, last_name, email, preferred_name) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertUserSql, "Admin", "User", "admin@example.com", "admin");

        String insertTaskSql = "INSERT INTO tasks (title, description, status, priority, author_id, assignee_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertTaskSql, "Task 1", "Task description 1", "CREATED", "HIGH", 1, 1);
        jdbcTemplate.update(insertTaskSql, "Task 2", "Task description 2", "IN_PROGRESS", "MEDIUM", 1, 1);
        jdbcTemplate.update(insertTaskSql, "Task 3", "Task description 3", "COMPLETED", "LOW", 1, 1);

        String insertCommentSql = "INSERT INTO comments (text, task_id, author_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertCommentSql, "Comment for Task 1", 1, 1);
        jdbcTemplate.update(insertCommentSql, "Comment for Task 2", 2, 1);
    }
}

