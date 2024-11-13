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

    // Display the main page at the root URL ("/")
    @GetMapping("/")
    public String students2(Model model, ModelMap mm,
                            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<Student> students;
        if (keyword.isEmpty()) {
            students = studentRepository.findAll(); // Fetch all students if no keyword is provided
        } else {
            try {
                mm.put("e", 0);
                mm.put("a", 0);
                long key = Long.parseLong(keyword);
                students = studentRepository.findStudentById(key); // Fetch student by ID
            } catch (NumberFormatException e) {
                students = List.of(); // Return empty list if keyword is not a valid number
            }
        }
        model.addAttribute("listStudents", students); // Add the list of students to the model
        return "students"; // Render the students view
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
                students = List.of();
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
        studentRepository.save(student);
        return "redirect:/"; // Redirect to the main page (root URL)
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentRepository.deleteById(id);
        return "redirect:/"; // Redirect to the main page (root URL)
    }

    // New method to handle editing
    @GetMapping("/editStudents")
    public String editStudent(@RequestParam("id") Long id, Model model) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            model.addAttribute("student", student);
            return "formStudents"; // Reuse formStudents.html for editing
        }
        return "redirect:/"; // If student not found, redirect to main page
    }
}
