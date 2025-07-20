A group project for **BITP 3123 Distributed Application Development** that demonstrates integration between two Java desktop applications and a RESTful API backend with MySQL. Users can manage and view student exam results, with an optional export feature to Google Sheets.

---

DEVELOPMENT TEAM:
| Matrics No.    | Name     |
|--------------|--------------|
| B032310196   | KING SOON KIT  |
| B032310257   | MUHAMMAD ARIF AIMAN |
| B032310645   | SHOMESWARAN A/L MUGUNTHAN |
| B032310805   | BOO JIA JUN |
| B032310505   | TEE YONG JIAN |
| B032310523   | HARIS A/L R SURESH |


---

## 📦 Project Structure

### 1️⃣ Teacher Application (Java Swing)
- Inputs student marks by subject
- Sends data to backend via REST API
- Can export all uploaded marks to Google Sheets

### 2️⃣ Student Application (Java Swing)
- Students login using credentials
- Can view their own results
- Can export their own marks to Google Sheets

### 3️⃣ Backend Application (REST API)
- Built using PHP library
- Connects to MySQL database
- Provides endpoints for:
  - Mark submission
  - Student login
  - Result retrieval
  - Manual export to Google Sheets
 
## Requirements to run the program:
1) Web Server: `XAMPP`, `Laragon`, etc.
2) Java Eclipse IDE.
3) Composer (to install `firebase/php-jwt` package).
  
## How to clone and run the program in your local side
1) Install Composer package manager from https://getcomposer.org/download/ if you do not have it yet.
1) Copy the HTTPS or SSH URL of the Git repository 
2) Open Eclipse and Go to Git Perspective :

   2.1) Click on `Window > Perspective > Open Perspective > Other...`

   2.2) Select `Git` and click `OK`
3) Clone the Git Repository

   3.1) In the Git Repositories view, `right-click > Clone a Git Repository`

   3.2) Paste the repository URL and click Next
4) Import the Project into Eclipse

   4.1) Choose the directory where you want the project to stored locally.

   4.2) From the imported project folder, find `emrs-api` folder and copy and paste it on your web server's document root. If you use `XAMPP`, you should paste in in the following path:
   `C:\xampp\htdocs\Exam-Result-Management-System`. Make sure `.htaccess` file is also included. It is recommended to copy and paste the folder via window's file explorer.

   4.3) After that, open the `C:\xampp\htdocs\Exam-Result-Management-System\erms-api` path in terminal and run `composer install`. It will install the `firebase/php-jwt` which is the dependency for using `JWT Token`.
   
   4.4) Then, continuing from the Eclipse IDE, right-click the repo in Git Repositories `view > Import Projects` (to make it show in package explorer)
6) Run the erms_db.sql file into your database query to clone the dummy data and db structure.
7) Check the database connection credential to ensure it's correct (database.pgp at directory of erms-api folder.
8) All setup completed, now run the MainApp.java as java application (under src > erms package)
9) You should see the login asking screen, then can proceed.


### Current dummy data for teacher/student login
teacherID: T001 , T002, T003
studentID: total 58 can refer database, our grp member matric no. are included, pls feel free to try
password: 12345678 (for all teachers and students)

---
## Project Overview
The Exam Result Management System is a software application designed to streamline the process of recording, managing, and publishing students’ examination results. It serves as a centralized platform where teachers can input scores, generate reports, and provide students with secure access to their academic performance.

The system aims to reduce manual errors, eliminate paperwork, and improve the efficiency and transparency of result handling. Students can view their results online, while administrators can generate reports and perform data analysis with ease.

## Commercial Value / Third-Party Integration
We integrate our software with the Google Sheets API to enable teachers and students to view and export data directly into Google Sheets.

## System Architecture

### High-Level Diagram
![WhatsApp Image 2025-07-20 at 5 12 59 PM](https://github.com/user-attachments/assets/784b4a89-6b17-4fe7-ad60-65a93c2a52ca)

## Backend Application

**Technology Stack**

- Language - Java
- Networking - HttpURLConnection, HttpClient
- JSON Processing	- org.json (for JSON parsing)
- Web Server - PHP-based backend (API hosted in /erms-api/)

## 1. login(AuthService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api//authentication.php`
  
  HTTP method: `POST`
  
  Header: `Content-Type - application/json`

  Body:
    
    {
      "userid": "B032310523",
      "password": "12345678",
      "role": "student"
    }

  **Success response:**
    
  Status Code: `200 OK`
  
    {
        "message": "Login successful.",
        "id": "B032310523",
        "name": "HARIS A/L R SURESH"
    }
 
  **Error response:**

  Status Code: `401 Unauthorized`
  
    {
        "message": "Invalid ID or password2."
    }

    
  Status Code: `400 Bad Request`
  
    {
        "message": "User ID, password, and role are required."
    }

## 2. exportToSheets(Student Service)
  API endpoint: `https://script.google.com/macros/s/AKfycbzFxLauWg_r8wDN3WV9LbT2UUW6sdfe5-NZ9TJTHk4_4a5edYS5j37qWUXk071RX6le/exec`
  
  HTTP method: `POST`
 
  Header: `Content-Type - application/json`
  
  Body:
    
    {
      "data": [
        ["Subject ID", "Subject Name", "Score", "Grade", "Teacher ID"],
        ["BITP 3123", "Distributed Application Development", "92", "A", "T003"],
        ["BITP 2223", "Software Requirement and Design", "55", "D", "T001"],
        ["BITP 3253", "Software Validation and Verification", "11", "F", "T002"]
      ]
    }
    
  **Success response:**
    
  Status Code: `200 OK`
  
    {
      "url": "https://docs.google.com/spreadsheets/d/1sof4dRyJEy851qU06cyvnu55_FzfsbMgITAiejEkhoM/edit"
    }

  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
      "message": "Invalid or missing 'data' array."
    }

## 3. fetchSubjects(TeacherService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/Student/fetch-enrolled-subjects-marks.php`
  
  HTTP method: `POST`
 
  Header: `Content-Type - application/json`
  
  Body:
    
    {
    "studentID": "B032310523"
    }
    
  **Success response:**
    
  Status Code: `200 OK`
  
    [
      {
          "subjectID": "BITP 2223",
          "subjectName": "Software Requirement and Design"
      },
      {
          "subjectID": "BITP 3253",
          "subjectName": " Software Validation and Verification"
      }
    ]

  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
      "message": "studentID is required"
    }

  ## 4. fetchMarks(Student Service)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/Student/fetch-subject-mark.php`
  
  HTTP method: `POST`
  
  Header: `Content-Type - application/json`
  
  Body:
    
    {
      "studentID": "B032310523",
      "subjectID": "BITP 2223"
    }
    
  **Success response:**
    
  Status Code: `200 OK`
  
    [
      {
          "score": 78,
          "grade": "B"
      }
    ]
    
  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
      "message": "Both studentID and subjectID are required"
    }

  ## 5. fetchEnrolledSubjects(Student Service)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/Student/fetch-enrolled-subjects.php`
  
  HTTP method: `POST`
  
  Header: `Content-Type - application/json`
  
  Body:
    
    {
      "studentID": "B032310523"
    }

  **Success response:**
    
  Status Code: `200 OK`
  
     [
        {
            "subjectID": "BITP 2223",
            "subjectName": "Software Requirement and Design",
            "score": 78,
            "grade": "B",
            "teacherID": "T001"
        },
        {
            "subjectID": "BITP 3253",
            "subjectName": " Software Validation and Verification",
            "score": 80,
            "grade": "A",
            "teacherID": "T002"
        }
    ]

  **Error response:**
    
  Status Code: `400 Bad Request`

    {
        "message": "studentID is required"
    }

  ## 6. fetchStudents(TeacherService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-students.php`
  
  HTTP method: `GET`

  **Success response:**
    
  Status Code: `200 OK`
  
    [
      {
          "studentID": "B032310002",
          "studentName": "AHMAD NAQIUDDIN BIN MOHAMAD"
      },
      {
          "studentID": "B032310011",
          "studentName": "KISHAH A/P PRAKHASH"
      }
    ]
  
  ## 7. fetchSubjects (TeacherService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-subjects.php`
   
  HTTP method: `POST`
      
  Header: `Content-Type - application/json`
    
    Body:
    {
      "teacherID": "T003"
    }

  **Success response:**
    
  Status Code: `200 OK`
  
    [
      {
          "subjectID": "BITP 3123",
          "subjectName": " Distributed Application Development"
      },
      {
          "subjectID": "BITP 3453 ",
          "subjectName": "Mobile Application Development"
      }
    ]

  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
      "message": "teacherID is required"
    }

  ## 8. submitMark(Teacher Service)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-subjects.php`
  
  HTTP method: `POST`
  
  Header: `Content-Type - application/json`
  
  Body:
    
    {
    "studentID": "B032310523",
    "subjectID": "BITP 3453",
    "teacherID": "T003",
    "score": 88,
    "grade": "A"
    }

  **Success response:**
    
  Status Code: `200 OK`
  
    {
      "message": "Mark inserted and exported."
    }

  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
      "message": "Missing required fields."
    }
  
    Status Code: 409 Conflict
    {
      "message": "This student already has a mark for this subject."
    }
  
    Status Code: 500 Internal Server Error
    {
      "error": "SQLSTATE[23000]: Integrity constraint violation: 1452 Cannot add or update a child row: a foreign key constraint fails (`erms_db`.`mark`, CONSTRAINT `mark_ibfk_2` FOREIGN KEY (`subjectID`)     REFERENCES `subject` (`subjectID`))"
    }

  ## 9. fetchMarks (TeacherService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-marks.php?teacherID=" + teacherID`
 
  HTTP method: `GET`
  
  Header: `Content-Type - application/json`

  Body:
  
    No Body Required

  **Success response:**
    
  Status Code: `200 OK`
  
    [
      {
        "studentID": "B032310505",
        "subjectID": "BITP 3253",
        "teacherID": "T002",
        "score": 11,
        "grade": "F"
      },
      {
        "studentID": "B032310002",
        "subjectID": "BITP 3253",
        "teacherID": "T002",
        "score": 89,
        "grade": "B"
      }
    ]


  ## 10. exportToSheets(TeacherService)
  API endpoint: `https://script.google.com/macros/s/AKfycbzFxLauWg_r8wDN3WV9LbT2UUW6sdfe5-NZ9TJTHk4_4a5edYS5j37qWUXk071RX6le/exec`
  
  HTTP method: `POST`
  
  Header: `Content-Type - application/json`
  
  Body:
    
    {
      "data": [
        ["Student ID", "Subject ID", "Teacher ID", "Score", "Grade"],
        ["B032310505", "BITP 3253", "T001", "92", "A"],
        ["B032310002", "BITP 3253", "T002", "11", "F"]
      ]
    }

  **Success response:**
    
  Status Code: `200 OK`
  
    {
    "url": "https://docs.google.com/spreadsheets/d/1j2iSfIegL9TgKW2rqatdpmaCqPSDTzkFy9daPAfdQAQ/edit"
    }

  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
      "message": "Invalid or missing 'data' array."
    }

  ## 11. generateToken (JwtService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/jwt/generate_token.php`
  
  HTTP method: `POST`
  
  Header: `Content-Type - application/json`
  
  Body:
    
    {
        "userid": "B032310002",
        "password": "12345678",
        "role": "student"
    }

  **Success response:**
    
  Status Code: `200 OK`
  
    {
        "message": "Token generated successfully.",
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NTMwMTczMzYsImV4cCI6MTc1MzEwMzczNiwiZGF0YSI6eyJ1c2VyaWQiOiJCMDMyMzEwMDAyIiwicm9sZSI6InN0dWRlbnQifX0.hgRECmzGl_ADk8S3G1agXcADP2B_j-k477wwqsVeiv8"
    }

  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
        "message": "Incomplete data. 'userid', password and 'role' are required."
    }

  ## 12. validateToken (JwtService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/jwt/validate_token.php`
  
  HTTP method: `POST`
  
  Header: `Authorization - Bearer<token>`
  
  Token Example:
    
    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NTMwMTczMzYsImV4cCI6MTc1MzEwMzczNiwiZGF0YSI6eyJ1c2VyaWQiOiJCMDMyMzEwMDAyIiwicm9sZSI6InN0dWRlbnQifX0.hgRECmzGl_ADk8S3G1agXcADP2B_j-k477wwqsVeiv8

  **Success response:**
    
  Status Code: `200 OK`
    
    {
        "message": "Access granted.",
        "data": {
            "userid": "B032310002",
            "role": "student"
        }
    }

  **Error response:**
    
  Status Code: `400 Bad Request`
  
    {
        "message": "Incomplete data. 'userid', password and 'role' are required."
    }

  Status Code: `401 Unauthorized`
  
    {
        "message": "Access denied. Invalid token.",
        "error": "Syntax error, malformed JSON"
    }

  Status Code: `401 Unauthorized`
  
    {
        "message": "Access denied. No token provided."
    }
    
  ## 13. logout (AuthService)
  API endpoint: `http://localhost/Exam-Result-Management-System/erms-api/logout.php`
  
  HTTP method: `POST`
  
  Header: `Authorization - Bearer<token>`
  
  Token Example:
  
    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NTMwMTc2NDMsImV4cCI6MTc1MzEwNDA0MywiZGF0YSI6eyJ1c2VyaWQiOiJCMDMyMzEwMDAyIiwicm9sZSI6InN0dWRlbnQifX0.4XY0uLGK3qDZx3KcHDXRl0cBfjjmPU5qJ6NGn_nTIeo

  **Success response:**
    
    Status Code: 200 OK
    {
        "message": "Logout successful."
    }

  **Error response:**
    
  Status Code: **401 Unauthorized`
    
    {
        "message": "Logout failed. Invalid token or server error.",
        "error": "Signature verification failed"
    }

  Status Code: `401 Unauthorized`
  
    {
        "message": "Authentication required to log out."
    }


## Implemented Security Measures:
  - Authentication is handled via a centralized login endpoint (/authentication.php).
  - User role-based access via the role field (student, teacher) in the login request.
  - Password encryption using bcrypt password-hashing function to secure the user's credentials.
  - Implementation of `JWT Token` to secure the APIs endpoints from unauthorized users.

## Frontend Application: Student App
**Purpose**

The Student App is designed for students to view their enrolled subjects, check marks/grades for individual subjects, and export academic records to Google Sheets. It provides an intuitive, GUI-based interface that allows students to:

- View a list of subjects they are enrolled in.

- Check scores and grades for specific subjects.

- Filter subjects and export records for personal use or future reference.

**Technology Stack**

- Programming Language - Java
- GUI Toolkit - Swing (javax.swing)
- JSON Processing - org.json
- HTTP Communication - HttpURLConnection from Java Standard Library
- Threading - SwingWorker for asynchronous export operations

**API Integration**

- The frontend communicates with a backend REST API using HttpURLConnection and JSON payloads.

Here's how data exchange is handled:
Base API URL: http://localhost/Exam-Result-Management-System/erms-api/

1. Fetch Enrolled Subjects
- Method: POST
- Endpoint: /Student/fetch-enrolled-subjects.php
- Purpose: Populate the table with subjects the student is enrolled in.

2. Fetch Subjects for Marks View
- Method: POST
- Endpoint: /Student/fetch-enrolled-subjects-marks.php
- Purpose: Populate dropdown menu with subjects for which the student can view marks.

3. Fetch Subject Marks
- Method: POST
- Endpoint: /Student/fetch-subject-mark.php
- Purpose: Retrieve score and grade for a selected subject.

4. Export to Google Sheets
- Method: POST
- Endpoint: /Student/export-to-sheets.php
- Purpose: Export the displayed data (enrolled subjects, scores, grades) to a Google Sheets document.

## Frontend Application: Teacher App
**Purpose**

The Teacher App is designed for educators to manage and review students' academic performance. This app provides an interactive GUI that ensures accuracy, transparency, and efficiency in managing student assessments. It enables teachers to:

- Enter marks and grades for students.

- Automatically compute grades based on scores.

- Export grades to Google Sheets.

- View and filter submitted marks for review.

**Technology Stack**

- Programming Language - Java
- GUI Toolkit - Swing (javax.swing)
- JSON Handling - org.json library
- Networking - HttpURLConnection from Java Standard Library
- UI Enhancements - Table sorting, combo boxes, input validation, and real-time grade calculation

**API Integration**

- The frontend communicates with a backend REST API using HttpURLConnection and JSON payloads.

Here's how data exchange is handled:
Base API URL: http://localhost/Exam-Result-Management-System/erms-api/

1. Fetch Students
- Method: GET
- Endpoint: /Teacher/fetch-students.php
- Purpose: Populate the student dropdown in the mark entry form with all available students.

2. Fetch Subjects Assigned to Teacher
- Method: POST
- Endpoint: /Teacher/fetch-subjects.php
- Purpose: Populate the subject dropdown based on the teacher's assigned subjects.

3. Submit Mark
- Method: POST
- Endpoint: /Teacher/submit-mark.php
- Purpose: Submit a new score and grade for a student in a subject and optionally export it to Google Sheets.

4. Fetch Submitted Marks
- Method: GET
- Endpoint: /Teacher/fetch-marks.php?teacherID={teacherID}
- Purpose: Retrieve a list of all submitted marks for review, filtering, or auditing.

## Database Design

### Entity-Relationship Diagram (ERD)
![WhatsApp Image 2025-06-29 at 10 18 02 PM](https://github.com/user-attachments/assets/7c073250-b9aa-41ca-a56c-9965be3a5278)


### Schema Justification 
- The database schema is designed to efficiently support a student exam grading and exam result management system, ensuring data integrity, scalability, and clear relational mapping between key entities.

**Key Tables & Their Roles**

1. Student
- Stores student-specific data such as studentID, studentName, class, and password.
- Acts as a parent entity in the grading system, connected to the Mark table via studentID.

2. Teacher
- Contains teacher details (teacherID, teacherName).
- Linked to the Mark table to track who assigned or entered a specific mark.

3. Subject
- Maintains information about academic subjects.
- Connected to the Mark table through subjectID, enabling subjects to be linked to students and teachers.

4. Mark
- Central table that records student scores and grades.
- Has foreign keys referencing the Student, Teacher, and Subject tables.
- Stores actual grade data (score, grade) and supports auditability by identifying the responsible teacher.

**Relationship Design**

The Mark table serves as a junction table, capturing many-to-many relationships:

- A student can be graded in many subjects.

- A subject can have marks from many students.

- A teacher can assign marks to many students.

Foreign key constraints are used to enforce referential integrity, ensuring that no mark exists without valid student, subject, and teacher references.

This design promotes:
- **Clarity** in academic data tracking.
- **Ease of querying** for reports or analysis.
- **Separation of concerns**, where data is logically grouped and redundant storage is avoided.

## Business Logic and Data Validation

### Flowchart of the System
<img width="491" height="900" alt="image" src="https://github.com/user-attachments/assets/1fa8c60e-3fc5-4da8-b553-90a2a86fae27" />

### Data Validation
1. Score Input Validation
- The score field only accepts valid numeric input using a regular expression check (\\d{1,3}), ensuring that scores are integers.
- Invalid or non-numeric inputs automatically prevent grade calculation, encouraging correct input before submission.

2. Auto-Grade Calculation
- Grades are automatically calculated based on the entered score using predefined grading rules:

A (80-100), B (70–79), C (60–69), D (50–59), E (40–49), F (<40)

This removes the possibility of user error in manual grade entry and enforces consistency across all teachers.

3. Mandatory Field Checks
- Before submission, the app ensures that all required fields (student, subject, score, and grade) are filled.
- If any value is missing or invalid (e.g., empty score or grade), an error dialog is displayed prompting the user to correct the input.

4. Dropdown Selection Control
- Student and subject fields are populated through controlled dropdowns, which eliminates the risk of entering invalid or non-existent IDs.
- This ensures referential integrity before the mark is submitted to the backend.

5. Submission Constraints
- On submission, the app warns if the student already has a mark for the selected subject, preventing duplicate or conflicting entries.

 
