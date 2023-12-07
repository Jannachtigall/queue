package queue.repository;

import org.springframework.data.repository.CrudRepository;
import queue.model.Queue;

public interface QueueRepository extends CrudRepository<Queue, Long> {
}
