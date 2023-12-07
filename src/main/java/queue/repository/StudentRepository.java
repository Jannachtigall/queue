package queue.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import queue.model.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
    void deleteAllByQueueId(Long queue_id);

    // Обновление позиций при добавлении нового студента
    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.position = s.position + 1 WHERE s.queue.id = :queueId AND s.position >= :newPosition")
    void updatePositionsForNewStudent(Long queueId, Integer newPosition);

    // Обновление позиций при удалении студента
    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.position = s.position - 1 WHERE s.queue.id = :queueId AND s.position > :removedPosition")
    void updatePositionsForRemovedStudent(Long queueId, Integer removedPosition);

}
