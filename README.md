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
6) Check the database connection credential to ensure it's correct (database.pgp at directory of erms-api folder)
7) Change the url of Google sheet apps script into your own (at the file of submit-mark.php under erms-api > Teacher folder), so data will go export into your google sheet , current one is jia jun's google sheet.
8) All setup completed, now run the MainApp.java as java application (under src > erms package)
9) You should see the login asking screen, then can proceed.


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

## Backend Application

### Technology Stack
- Java
- etc

## API Documentation
1) List of All API endpoints.
2) HTTP method for each endpoint.
3) Required request parameters, headers, and body formats (with JSON examples). - use Postman
4) Example success and error responses (with status code and JSON example). - use Postman
5) Security - Detail the security measures implemented. Explain the choice of mechanism and describe how it protects the endpoint.

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
A diagram showing the database tables, their columns (with data types), and the relationships between them (one-to-one, one-to-many).

### Schema Justification 
Briefly explain why the database was designed this way, highlighting key tables and relationships.

## Business Logic and Data Validation

### Use Case Diagrams/Flowcharts
Illustrate the main user flows, such as "selecting a book," "borrowing a book," and "returning a book." This visually demonstrates the business logic.

### Data Validation
Describe the validation rules implemented on both the frontend (e.g., checking for empty fields) and backend (e.g., ensuring an email is unique).

 
