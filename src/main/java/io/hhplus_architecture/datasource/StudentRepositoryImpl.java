package io.hhplus_architecture.datasource;

import io.hhplus_architecture.business.interfaces.IStudentRepository;
import io.hhplus_architecture.datasource.interfaces.IStudentJpaRepository;
import io.hhplus_architecture.domain.entity.Student;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentRepositoryImpl implements IStudentRepository{
    private final IStudentJpaRepository studentJpaRepository;

    @Autowired
    public StudentRepositoryImpl(IStudentJpaRepository studentRepository){
        this.studentJpaRepository = studentRepository;
    }
    // 학생 저장
    @Override
    public Student save(String name){
        return studentJpaRepository.save(Student.builder().name(name).build());
    }

    // 학생 조회
    @Override
    public Optional<Student> findById(Long id){
        return studentJpaRepository.findById(id);
    }
}
