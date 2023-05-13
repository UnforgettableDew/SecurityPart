-- INSERT INTO users (username, password, role)
-- VALUES ('andrew', '$2a$10$UkK5p9u5J1Z6TVxf4NM.euQgh1ZdMn8ZlLcFptlQgqLOGO1flw.Ze', 'EDUCATOR'),
--        ('nazar', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
--        ('artem', '$2a$10$wouShpN6vTvZgH7YKlqiM.081yHGh/1EnDOA3XAmoWCwkVYo/POra', 'STUDENT');
insert into users(username, password, role)
values ('kostya', '$2a$10$UkK5p9u5J1Z6TVxf4NM.euQgh1ZdMn8ZlLcFptlQgqLOGO1flw.Ze', 'EDUCATOR'),
       ('tom', '$2a$10$UkK5p9u5J1Z6TVxf4NM.euQgh1ZdMn8ZlLcFptlQgqLOGO1flw.Ze', 'EDUCATOR'),
       ('john', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Jane', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Bob', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Alice', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Tomik', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Samantha', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Matthew', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Olivia', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Ethan', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('Ava', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('johnsmith', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('emily', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('michael', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('otaylor', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT'),
       ('william', '$2a$10$71qP7nfUwlhaKCp4G8rZ.eJkycdkwGLG63017shIqq2lKdU5uzMSO', 'STUDENT');

insert into educator(first_name, last_name, age, email, telegram_contact, user_id)
values ('Kostyantin', 'Zhereb', 33, 'koszher@gmail.com', 'zhereb', 1),
       ('Tom', 'Yorkshir', 28, 'tomyork@gmail.com', 'tomyork', 2);

insert into course(title, description, educator_id)
values ('Back-end', 'Course for Back-end developers', 1),
       ('Front-end', 'Course for Front-end developers', 2),
       ('DevOps', 'Course for DevOps', 2);

insert into lw_task(title, description, max_score, course_id)
values ('Database Fundamentals', 'Learn the basics of database design and management', 8, 1),
       ('SQL Queries', 'Learn how to write SQL queries to retrieve and manipulate data', 9, 1),
       ('Server-side Scripting', 'Learn how to write server-side scripts using PHP', 9, 1),
       ('API Development', 'Learn how to develop RESTful APIs for web applications', 10, 1),
       ('Security and Authentication', 'Learn about web application security and authentication techniques', 10, 1),
       ('HTML Basics', 'Learn the fundamentals of HTML', 6, 2),
       ('CSS Styling', 'Learn how to style your HTML pages with CSS', 8, 2),
       ('JavaScript Essentials', 'Learn the basics of JavaScript programming language', 9, 2),
       ('Responsive Design', 'Learn how to create responsive web pages that adapt to different screen sizes', 9, 2),
       ('Advanced CSS', 'Learn advanced techniques for styling web pages with CSS', 10, 2),
       ('Implement CI/CD pipeline for a Java application', 'Configure Jenkins to build, test, and deploy a Java application using a pipeline', 20, 3),
       ('Create a Kubernetes deployment for a microservice', 'Deploy a microservice on a Kubernetes cluster using YAML files', 20, 3),
       ('Configure an Ansible playbook for a web server', 'Write an Ansible playbook to install and configure a web server on a remote machine', 20, 3),
       ('Set up monitoring and logging for a Dockerized application', 'Configure Prometheus and Grafana to monitor and visualize logs for a Dockerized application', 20, 3);

insert into student(first_name, last_name, age, email, telegram_contact, student_group, user_id, registration_date)
values ('John', 'Doe', 21, 'johndoe@example.com', '@johndoe', 'Group A', 3, '2023-01-08 14:30:00'),
       ('Jane', 'Smith', 23, 'janesmith@example.com', '@janesmith', 'Group B', 4, '2023-05-03 14:30:00'),
       ('Bob', 'Johnson', 20, 'bobjohnson@example.com', '@bobjohnson', 'Group A', 5, '2023-07-08 14:30:00'),
       ('Alice', 'Williams', 22, 'alicewilliams@example.com', '@alicewilliams', 'Group C', 6, '2023-01-04 14:30:00'),
       ('Tom', 'Brown', 21, 'tombrown@example.com', '@tombrown', 'Group B', 7, '2023-05-07 14:30:00'),
       ('Samantha', 'Lee', 21, 'samantha.lee@example.com', '@samanthalee', 'Group A', 8, '2023-06-12 14:30:00'),
       ('Matthew', 'Nguyen', 19, 'matthew.nguyen@example.com', '@matthewnguyen', 'Group A', 9, '2023-2-08 14:30:00'),
       ('Olivia', 'Garcia', 22, 'olivia.garcia@example.com', '@oliviagarcia', 'Group A', 10, '2023-05-03 14:30:00'),
       ('Ethan', 'Kim', 23, 'ethan.kim@example.com', '@ethankim', 'Group C', 11, '2023-04-09 14:30:00'),
       ('Ava', 'Patel', 20, 'ava.patel@example.com', '@avapatel', 'Group B', 12, '2023-03-02 14:30:00'),
       ('John', 'Smith', 20, 'john.smith@example.com', '@john_smith', 'Group A', 13, '2023-05-03 14:30:00'),
       ('Emily', 'Jones', 22, 'emily.jones@example.com', '@emily_jones', 'Group B', 14, '2023-05-04 14:34:00'),
       ('Michael', 'Davis', 21, 'michael.davis@example.com', '@michael_davis', 'Group A', 15, '2023-05-05 14:31:00'),
       ('Olivia', 'Taylor', 19, 'olivia.taylor@example.com', '@olivia_taylor', 'Group B', 16, '2023-05-06 14:32:00'),
       ('William', 'Brown', 20, 'william.brown@example.com', '@william_brown', 'Group C', 17, '2023-05-07 14:33:00');

insert into laboratory_work(title, github_reference, score, is_passed, comment, student_id, task_id)
values ('lab1_john', 'github/lab1_john', 8, true, 'great', 1, 1),
       ('lab2_john', 'github/lab2_john', 7, true,'good', 1, 2),
       ('lab3_john', 'github/lab3_john', 7, true,'well', 1, 3),
       ('lab4_john', 'github/lab4_john', null, false, null, 1, 4),
       ('lab1_jane', 'github/lab1_jane', 8, true, 'great', 2, 1),
       ('lab3_jane', 'github/lab3_jane', 8, true, 'well', 2, 3),
       ('lab5_jane', 'github/lab5_jane', 7, true, 'great', 2, 5),
       ('lab1_jane', 'github/lab1_jane', 6, true, 'well', 2, 6),
       ('lab2_jane', 'github/lab2_jane', null, false, null, 2, 7),
       ('lab1_bob', 'github/lab1_bob', 7, true, 'great', 3, 1),
       ('lab1_alice', 'github/lab1_alice', 8, true, 'great', 4, 1),
       ('lab2_alice', 'github/lab2_alice', 8, true, 'well', 4, 2),
       ('lab1_tom', 'github/lab1_tom', 8, true, 'great', 5, 1),
       ('lab2_tom', 'github/lab2_tom', 6, true, 'good', 5, 2),
       ('lab3_tom', 'github/lab3_tom', 9, true, 'well', 5, 3),
       ('lab4_tom', 'github/lab4_tom', 10, true, 'great', 5, 4),
       ('lab1_tom', 'github/lab1_tom', 5, true, 'good', 5, 6),
       ('lab2_tom', 'github/lab2_tom', 8, true, 'well', 5, 7),
       ('lab1_samantha', 'github/lab1_samantha', 8, true, 'well', 6, 1),
       ('lab2_samantha', 'github/lab2_samantha', 8, true, 'great', 6, 2),
       ('lab3_samantha', 'github/lab3_samantha', 9, true, 'good', 6, 3),
       ('lab1_matthew', 'github/lab1_matthew', 7, true, 'great', 7, 1),
       ('lab2_matthew', 'github/lab2_matthew', null, false, null, 7, 2),
       ('lab3_matthew', 'github/lab3_matthew', 8, true, 'great', 7, 3),
       ('lab5_matthew', 'github/lab5_matthew', 10, true, 'good', 7, 5),
       ('lab1_olivia', 'github/lab1_olivia', 7, true, 'well', 8, 1),
       ('lab1_olivia', 'github/lab1_olivia', 5, true, 'great', 8, 6),
       ('lab1_ethan', 'github/lab1_ethan', 8, true, 'good', 9, 1),
       ('lab1_ava', 'github/lab1_ava', 8, true, 'well', 10, 1),
       ('lab2_ava', 'github/lab2_ava', 8, true, 'good', 10, 2),
       ('lab1_john', 'github/lab1_john', 19, true, 'great', 11, 11),
       ('lab2_john', 'github/lab2_john', 18, true, 'well', 11, 12),
       ('lab3_john', 'github/lab3_john', 18, true, 'good', 11, 13),
       ('lab1_emily', 'github/lab1_emily', 18, true, 'good', 12, 13),
       ('lab1_emily', 'github/lab1_emily', 6, true, 'good', 12, 6),
       ('lab1_michael', 'github/lab1_michael', 18, true, 'good', 13, 11),
       ('lab2_michael', 'github/lab2_michael', null, false, null, 13, 12),
       ('lab1_olivia', 'github/lab1_olivia', 15, true, 'well', 14, 11),
       ('lab2_olivia', 'github/lab2_olivia', null, false, null, 14, 12),
       ('lab1_william', 'github/lab1_william', null, false, null, 15, 11),
       ('lab1_william', 'github/lab1_william', 6, true, 'good', 15, 6),
       ('lab2_william', 'github/lab2_william', 6, true, 'well', 15, 7);




insert into course_student(student_id, course_id)
values (1, 1),
       (2, 1),
       (2, 2),
       (3, 1),
       (4, 2),
       (5, 1),
       (5, 2),
       (6, 2),
       (7, 1),
       (8, 1),
       (8, 2),
       (9, 1),
       (10, 2),
       (11,3),
       (12,2),
       (12,3),
       (13,3),
       (14,3),
       (15,2),
       (15,3);



