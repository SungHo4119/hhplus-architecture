package io.hhplus_architecture.business;

import io.hhplus_architecture.business.interfaces.IStudentRepository;
import io.hhplus_architecture.domain.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final IStudentRepository studentRepository;

    @Autowired
    public StudentService(IStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student saveStudent(String name) {
        return studentRepository.save(name);
    }
}
