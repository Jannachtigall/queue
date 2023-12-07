package queue.controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import queue.model.Queue;
import queue.model.Student;
import queue.repository.QueueRepository;
import queue.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/queues")
@AllArgsConstructor
public class IndexController {

    private QueueRepository queueRepository;

    private StudentRepository studentRepository;

    private final String redirect = "redirect:/queues";

    @GetMapping
    public String showQueues() {
        return "queues";
    }

    @ModelAttribute
    public void addQueuesToModel(Model model){
        List<Queue> queues = (List<Queue>) queueRepository.findAll();
        model.addAttribute("queues", queues);
    }

    @ModelAttribute(name = "queue")
    public Queue queue(){
        return new Queue();
    }

    @ModelAttribute(name = "student")
    public Student student(){
        return new Student();
    }

    @PostMapping("/addQueue")
    public String addQueue(@ModelAttribute Queue queue){
        queue.setStudents(new ArrayList<>());
        queueRepository.save(queue);
        return redirect;
    }

    @PostMapping("/addStudent")
    public String addStudent(@RequestParam("queueId") Long queueId,
                             @RequestParam("studentPosition") Integer studentPosition,
                             @ModelAttribute Student student){
        Optional<Queue> optionalQueue = queueRepository.findById(queueId);
        student.setPosition(studentPosition);
        if (optionalQueue.isPresent()) {
            studentRepository.updatePositionsForNewStudent(queueId, student.getPosition());
            Queue queue = optionalQueue.get();
            if (student.getPosition() > queue.getStudents().size())
                student.setPosition(queue.getStudents().size()+1);
            student.setQueue(queue);
            queue.getStudents().add(student);
            studentRepository.save(student);
        }
        return redirect;
    }

    @PostMapping("/clearQueue")
    @Transactional
    public String clearQueue(@RequestParam("queueId") Long queueId){
        studentRepository.deleteAllByQueueId(queueId);
        return redirect;
    }

    @PostMapping("/deleteStudent")
    @Transactional
    public String deleteStudent(@RequestParam("studentId") Long studentId){
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            studentRepository.updatePositionsForRemovedStudent(student.getQueue().getId(), student.getPosition());
            studentRepository.deleteById(studentId);
        }
        return redirect;
    }

    @PostMapping("/deleteQueue")
    public String deleteQueue(@RequestParam("queueId") Long queueId){
        queueRepository.deleteById(queueId);
        return redirect;
    }
}