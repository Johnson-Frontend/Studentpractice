package com.example.studentpractice.web;

import com.example.studentpractice.entities.Student;
import com.example.studentpractice.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

    // Redirect from root path ("/") to "/index"
    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    // Display students list with optional search keyword
    @GetMapping(path = "/index")
    public String students(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<Student> students;
        if (keyword.isEmpty()) {
            students = studentRepository.findAll(); // Fetch all students if no keyword
        } else {
            try {
                long key = Long.parseLong(keyword); // Convert keyword to ID (assuming it's numeric)
                students = studentRepository.findStudentById(key); // Custom query to find student by ID
            } catch (NumberFormatException e) {
                students = List.of(); // Return empty list if keyword is not numeric
            }
        }
        model.addAttribute("listStudents", students); // Add students to model for rendering
        return "students"; // Return view name "students"
    }

    // Delete a student by ID
    @GetMapping("/delete")
    public String deleteStudent(@RequestParam("id") Long id) {
        studentRepository.deleteById(id); // Delete student with the specified ID
        return "redirect:/index"; // Redirect back to the student list page
    }
}
