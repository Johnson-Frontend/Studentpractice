package com.example.studentpractice.web;

import com.example.studentpractice.entities.Student;
import com.example.studentpractice.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.ModelMap;

import java.util.List;

@Controller
@AllArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

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
            try {
                long key = Long.parseLong(keyword);
                students = studentRepository.findStudentById(key);
            } catch (NumberFormatException e) {
                students = List.of(); // Return empty list if keyword is not a valid number
            }
        }
        model.addAttribute("listStudents", students);
        return "students";
    }

    @GetMapping("/formStudents")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        return "formStudents";
    }

    @PostMapping(path = "/save")
    public String saveStudent(Student student, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "formStudents";
        }
        studentRepository.save(student); // Save new or edited student
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentRepository.deleteById(id); // Delete student by ID
        return "redirect:/index";
    }

    // New method to handle editing
    @GetMapping("/editStudents")
    public String editStudent(@RequestParam("id") Long id, Model model) {
        // Find the student by ID and pass it to the form
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            model.addAttribute("student", student); // Add student to model for pre-filling
            return "formStudents"; // Reuse formStudents.html for editing
        }
        return "redirect:/index"; // If student not found, redirect to main page
    }
}
