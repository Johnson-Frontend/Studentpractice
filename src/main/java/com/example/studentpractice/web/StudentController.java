package com.example.studentpractice.web;

import com.example.studentpractice.repositories.StudentRepository;
import com.example.studentpractice.entities.Student;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;

    // Static flag to indicate success message
    static int num = 0;

    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    @GetMapping(path = "/index")
    public String students(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<Student> students;
        if (keyword.isEmpty()) {
            students = studentRepository.findAll();
        } else {
            long key = Long.parseLong(keyword);
            students = studentRepository.findStudentById(key);
        }
        model.addAttribute("listStudents", students);
        model.addAttribute("num", num); // Pass the flag to the view
        num = 0; // Reset flag after displaying the message
        return "students";
    }

    @PostMapping("/addStudent")
    public String addStudent(Student student) {
        studentRepository.save(student);
        num = 1; // Set flag to show success message
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentRepository.deleteById(id);
        num = 2; // Set flag to indicate deletion
        return "redirect:/index";
    }
}
