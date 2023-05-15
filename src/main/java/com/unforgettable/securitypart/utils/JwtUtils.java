//package com.unforgettable.securitypart.utils;
//
//import com.unforgettable.securitypart.repository.ApplicationUserRepository;
//import com.unforgettable.securitypart.repository.EducatorRepository;
//import com.unforgettable.securitypart.repository.StudentRepository;
//import com.unforgettable.securitypart.service.JwtService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtUtils {
//    private final JwtService jwtService;
//    private final EducatorRepository educatorRepository;
//    private final StudentRepository studentRepository;
//    private final ApplicationUserRepository userRepository;
//
//    @Autowired
//    public JwtUtils(JwtService jwtService,
//                    EducatorRepository educatorRepository,
//                    StudentRepository studentRepository, ApplicationUserRepository userRepository) {
//        this.jwtService = jwtService;
//        this.educatorRepository = educatorRepository;
//        this.studentRepository = studentRepository;
//        this.userRepository = userRepository;
//    }
//
//    public Long getStudentId(HttpServletRequest request){
//        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
//        Long userId = jwtService.getUserIdByJwt(jwtToken);
//
//        return studentRepository.findStudentIdByUserId(userId);
//    }
//
//    public Long getEducatorId(HttpServletRequest request){
//        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
//        Long userId = jwtService.getUserIdByJwt(jwtToken);
//        return educatorRepository.findEducatorIdByUserId(userId);
//    }
//
//    public Long getUserIdByJwt(String jwt){
//        String username = jwtService.extractUsername(jwt);
//        return userRepository.findUserIdByUsername(username);
//    }
//
//}
