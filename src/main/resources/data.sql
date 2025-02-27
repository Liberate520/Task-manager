INSERT INTO "users" (id, first_name, last_name, email, preferred_name, created_at, update_at)
VALUES (1, 'Admin', 'User', 'admin@example.com', 'admin', NOW(), NOW());
ON CONFLICT (id) DO NOTHING;

INSERT INTO task (id, title, description, status, priority, author_id)
VALUES
    (1, 'Fix login bug', 'Investigate and fix login issue', 'IN_PROGRESS', 'HIGH', 1),
    (2, 'Update documentation', 'Revise API documentation', 'TODO', 'MEDIUM', 1),
    (3, 'Refactor user service', 'Improve code quality in user service', 'DONE', 'LOW', 1);
ON CONFLICT (id) DO NOTHING;

INSERT INTO comment (id, text, task_id, author_id)
VALUES
    (1, 'I am working on this issue.', 1, 1),
    (2, 'Need more details on the API changes.', 2, 1);
ON CONFLICT (id) DO NOTHING;
