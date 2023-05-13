package com.unforgettable.securitypart.utils;

import com.unforgettable.securitypart.repository.EducatorRepository;
import com.unforgettable.securitypart.repository.StudentRepository;
import com.unforgettable.securitypart.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private final JwtService jwtService;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public JwtUtils(JwtService jwtService,
                    EducatorRepository educatorRepository,
                    StudentRepository studentRepository) {
        this.jwtService = jwtService;
        this.educatorRepository = educatorRepository;
        this.studentRepository = studentRepository;
    }

    public Long getStudentId(HttpServletRequest request){
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        Long userId = jwtService.getUserIdByJwt(jwtToken);

        return studentRepository.findStudentIdByUserId(userId);
    }

    public Long getEducatorId(HttpServletRequest request){
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        Long userId = jwtService.getUserIdByJwt(jwtToken);
        return educatorRepository.findEducatorIdByUserId(userId);
    }

}
