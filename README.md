Quiz Management System
The Quiz Management System is a full-featured web application designed for managing quizzes, exams, and course assessments in an educational environment. This project supports multiple user roles—Admin, Teacher, and Student—with robust functionality for each role.

Key Features
User Management (Phase A)
Registration & Authentication: Secure sign-up and login for teachers and students using JWT-based authentication.
Role-Based Access Control: Admins can approve registrations and assign roles, ensuring only verified users gain access.
Course Management (Phase B)
Course Creation & Management: Admins can create and manage courses, while teachers are assigned to these courses.
Enrollment: Students can enroll in courses, and teachers can manage student rosters.
Question Bank (Phase C)
Multiple-Choice & Descriptive Questions: Teachers can create a rich question bank supporting both question types.
Single Table Inheritance: Efficient storage and management of question types using a single database table.
Exam Management (Phase D)
Exam Creation: Teachers can create exams, assign questions, and configure exam parameters like duration and start time.
Student Exam Participation: Students can start, resume (in case of connectivity issues), and complete exams with real-time timers and auto-save functionality.
Question Navigation: Seamless presentation of exam questions with support for resuming from the last answered question.
Exam Grading (Phase E)
Auto-Grading for Multiple-Choice: Automatic grading based on predefined correct answers.
Manual Grading for Descriptive Questions: Teachers can review and assign scores to descriptive answers.
Final Score Calculation: The system aggregates scores from all questions to calculate the final exam score.
Technologies Used
Backend: Java, Spring Boot, Spring Security, Spring Data JPA
Database: PostgreSQL
Authentication: JWT-based security
Mapping: ModelMapper for entity-to-DTO conversion
Build Tool: Maven
IDE: IntelliJ IDEA
