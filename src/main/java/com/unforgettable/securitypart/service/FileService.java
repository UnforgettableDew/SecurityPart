package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.entity.*;
import com.unforgettable.securitypart.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final CourseRepository courseRepository;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final LaboratoryWorkRepository laboratoryWorkRepository;
    private final TaskRepository taskRepository;

    public FileService(CourseRepository courseRepository,
                       EducatorRepository educatorRepository,
                       StudentRepository studentRepository,
                       LaboratoryWorkRepository laboratoryWorkRepository,
                       TaskRepository taskRepository) {
        this.courseRepository = courseRepository;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
        this.laboratoryWorkRepository = laboratoryWorkRepository;
        this.taskRepository = taskRepository;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        file.transferTo(new File("D:\\Files\\" + file.getOriginalFilename()));
    }

    public void createDirectories() {
        File educatorsDir = new File("D:\\Files\\");
        if (!educatorsDir.exists()) {
            educatorsDir.mkdirs();
        }

        List<Educator> educators = educatorRepository.findAll();

        for (Educator educator : educators) {
            File educatorDir = new File(educatorsDir, educator.getFirstname() + educator.getLastname());
            if (!educatorDir.exists()) {
                educatorDir.mkdirs();
            }

            List<Course> courses = educator.getCourses();
            for (Course course : courses) {
                File courseDir = new File(educatorDir, course.getTitle());
                if (!courseDir.exists()) {
                    courseDir.mkdirs();
                }

                File taskFile = new File(courseDir, "Tasks");
                if(!taskFile.exists()){
                    taskFile.mkdirs();
                }

                File studentFile = new File(courseDir, "Students");
                if(!studentFile.exists())
                    studentFile.mkdirs();

                List<Student> students = studentRepository.findStudentsByCourseId(course.getId());
                for (Student student : students) {
                    File studentDir = new File(studentFile, student.getFirstname() + student.getLastname());
                    if (!studentDir.exists()) {
                        studentDir.mkdirs();
                    }

                    File labsDir = new File(studentDir, "Labs");
                    if (!labsDir.exists()) {
                        labsDir.mkdirs();
                    }

                }
            }
        }
    }
}
