package queue.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Student implements Comparable<Student>{

    @Id
    @GeneratedValue
    private Long ID;

    private Integer position;

    private String name;

    private String whatToHandOver;

    @ManyToOne
    @JoinColumn(name = "queue_id") // имя столбца, который будет использоваться для хранения связи
    private Queue queue;


    @Override
    public int compareTo(Student o) {
        return this.getPosition() - o.getPosition();
    }
}
