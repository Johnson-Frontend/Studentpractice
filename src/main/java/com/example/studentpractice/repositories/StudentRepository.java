package com.example.studentpractice.repositories;

import com.example.studentpractice.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}