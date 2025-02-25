package ru.effectivemobile.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.effectivemobile.taskManager.model.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
