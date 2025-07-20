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
  
## How to clone and run the program in your local side
1) Copy the HTTPS or SSH URL of the Git repository 
2) Open Eclipse and Go to Git Perspective :

   2.1) Click on Window > Perspective > Open Perspective > Other...

   2.2) Select Git and click OK
3) Clone the Git Repository

   3.1) In the Git Repositories view, right-click > Clone a Git Repository

   3.2) Paste the repository URL and click Next
4) Import the Project into Eclipse

   4.1) Choose the directory where you want the project stored locally (**choose htdocs if using xampp**, not sure if using Workbench need explore yourself)

   4.2) Right-click the repo in Git Repositories view > Import Projects (to make it show in package explorer)
5) Run the erms_db.sql file into your database query to clone the dummy data and db structure.
6) Check the database connection credential to ensure it's correct (database.pgp at directory of erms-api folder.
7) All setup completed, now run the MainApp.java as java application (under src > erms package)
8) You should see the login asking screen, then can proceed.


### Current dummy data for teacher/student login
teacherID: T001 , T002, T003
studentID: total 58 can refer database, our grp member matric no. are included, pls feel free to try
password: 12345678 (for all teachers and students)

---
## Project Overview
The Exam Result Management System is a software application designed to streamline the process of recording, managing, and publishing students’ examination results. It serves as a centralized platform where administrators and teachers can input scores, generate reports, and provide students with secure access to their academic performance.

The system aims to reduce manual errors, eliminate paperwork, and improve the efficiency and transparency of result handling. Students can view their results online, while administrators can generate reports and perform data analysis with ease.

## Commercial Value / Third-Party Integration
We integrate our software with the Google Sheets API to enable teachers and students to view and export data directly into Google Sheets.

## System Architecture

### High-Level Diagram
<img width="841" height="661" alt="HighLevelDAD1 drawio" src="https://github.com/user-attachments/assets/fd49d9a4-89f8-4d77-85d5-63763da06f5a" />

## Backend Application

### Technology Stack
Language - Java
Networking - HttpURLConnection, HttpClient
JSON Processing	- org.json (for JSON parsing)
Web Server - PHP-based backend (API hosted in /erms-api/)

## 1. login(AuthService)
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api//authentication.php
  
  HTTP method: POST
  
  Header: Content-Type - application/json

    Body:
    {
      "userid": "B032310523",
      "password": "12345678",
      "role": "student"
    }

  
    Success response:
    Status Code: 200 OK
    {
        "message": "Login successful.",
        "id": "B032310523",
        "name": "HARIS A/L R SURESH"
    }

  
  Error response:
  Status Code: 401 Unauthorized
  ```json
  {
      "message": "Invalid ID or password2."
  }
```
    
  Status Code: 400 Bad Request
```json
  {
      "message": "User ID, password, and role are required."
  }
```

## 2. exportToSheets(Student Service)
  API endpoint: https://script.google.com/macros/s/AKfycbzFxLauWg_r8wDN3WV9LbT2UUW6sdfe5-NZ9TJTHk4_4a5edYS5j37qWUXk071RX6le/exec
  
  HTTP method: POST
 
  Header: Content-Type - application/json
  
    Body:
    {
    "data": [
      ["Subject ID", "Subject Name", "Score", "Grade", "Teacher ID"],
      ["BITP 3123", "Distributed Application Development", "92", "A", "T003"],
      ["BITP 2223", "Software Requirement and Design", "55", "D", "T001"],
      ["BITP 3253", "Software Validation and Verification", "11", "F", "T002"]
    ]
    }
  
    Success response:
    Status Code: 200 OK
    {
      "url": "https://docs.google.com/spreadsheets/d/1sof4dRyJEy851qU06cyvnu55_FzfsbMgITAiejEkhoM/edit"
    }
    
    Error response:
    Status Code: 400 Bad Request
    {
      "message": "Invalid or missing 'data' array."
    }

## 3. fetchSubjects(TeacherService)
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api/Student/fetch-enrolled-subjects-marks.php
  
  HTTP method: POST
 
  Header: Content-Type - application/json
  
    Body:
    {
    "studentID": "B032310523"
    }
    
    Success response:
    Status Code: 200 OK
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
    
    Error response:
    Status Code: 400 Bad Request
    {
      "message": "studentID is required"
    }

  ## 4. fetchMarks(Student Service)
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api/Student/fetch-subject-mark.php
  
  HTTP method: POST
  
  Header: Content-Type - application/json
  
    Body:
    {
      "studentID": "B032310523",
      "subjectID": "BITP 2223"
    }
      
    Success response:
    Status Code: 200 OK
    [
      {
          "score": 78,
          "grade": "B"
      }
    ]
    
    Error response:
    Status Code: 400 Bad Request
    {
      "message": "Both studentID and subjectID are required"
    }

  ## 5. fetchEnrolledSubjects(Student Service)
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api/Student/fetch-enrolled-subjects.php
  
  HTTP method: POST
  
  Header: Content-Type - application/json
  
    Body:
    {
      "studentID": "B032310523"
    }
  
    Success response:
    Status Code: 200 OK
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
    
    Error response:
    Status Code: 400 Bad Request
    {
        "message": "studentID is required"
    }

  ## 6. fetchStudents(TeacherService)
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-students.php
  
  HTTP method: GET
    
    Success response:
    Status Code: 200 OK
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
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-subjects.php
 
    HTTP method: POST
    
    Header: Content-Type - application/json
    
    Body:
    {
      "teacherID": "T003"
    }
  
    Success response:
    Status Code: 200 OK
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
    
    Error response:
    Status Code: 400 Bad Request
    {
      "message": "teacherID is required"
    }

  ## 8. submitMark(Teacher Service)
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-subjects.php
  
  HTTP method: POST
  
  Header: Content-Type - application/json
  
    Body:
    {
    "studentID": "B032310523",
    "subjectID": "BITP 3453",
    "teacherID": "T003",
    "score": 88,
    "grade": "A"
    }
  
    Success response:
    Status Code: 200 OK
    {
      "message": "Mark inserted and exported."
    }
    
    Error response:
    Status Code: 400 Bad Request
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
  API endpoint: http://localhost/Exam-Result-Management-System/erms-api/Teacher/fetch-marks.php?teacherID=" + teacherID
 
  HTTP method: GET
  
  Header: Content-Type - application/json

    Body:
    No Body Required
  
    Success response:
    Status Code: 200 OK
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
  API endpoint: https://script.google.com/macros/s/AKfycbzFxLauWg_r8wDN3WV9LbT2UUW6sdfe5-NZ9TJTHk4_4a5edYS5j37qWUXk071RX6le/exec
  
  HTTP method: POST
  
  Header: Content-Type - application/json
  
    Body:
    {
    "data": [
      ["Student ID", "Subject ID", "Teacher ID", "Score", "Grade"],
      ["B032310505", "BITP 3253", "T001", "92", "A"],
      ["B032310002", "BITP 3253", "T002", "11", "F"]
    ]
    }
  
    Success response:
    {
    "url": "https://docs.google.com/spreadsheets/d/1j2iSfIegL9TgKW2rqatdpmaCqPSDTzkFy9daPAfdQAQ/edit"
    }

    Error response:
    Status Code: 400 Bad Request
    {
      "message": "Invalid or missing 'data' array."
    }


## Implemented Security Measures:
  - Authentication is handled via a centralized login endpoint (/authentication.php).
  - User role-based access via the role field (student, teacher) in the login request.
  - JSON Payloads only; content-type is explicitly set to application/json.
  
## Frontend Applications

For each of the two frontend apps:

## Purpose

### Describe the app's function and target user (e.g., one app for customers, one for administrators).

Technology Stack

### List the frameworks and libraries used (e.g., React, Angular, Vue.js, Swift, Kotlin).

API Integration

### Explain how the frontend communicates with the backend API.

## Database Design

### Entity-Relationship Diagram (ERD)
![WhatsApp Image 2025-06-29 at 10 18 02 PM](https://github.com/user-attachments/assets/7c073250-b9aa-41ca-a56c-9965be3a5278)


### Schema Justification 
Briefly explain why the database was designed this way, highlighting key tables and relationships.

## Business Logic and Data Validation

### Use Case Diagrams/Flowcharts
Illustrate the main user flows, such as "selecting a book," "borrowing a book," and "returning a book." This visually demonstrates the business logic.

### Data Validation
Describe the validation rules implemented on both the frontend (e.g., checking for empty fields) and backend (e.g., ensuring an email is unique).

 
