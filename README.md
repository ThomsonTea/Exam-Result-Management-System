A group project for **BITP 3123 Distributed Application Development** that demonstrates integration between two Java desktop applications and a RESTful API backend with MySQL. Users can manage and view student exam results, with an optional export feature to Google Sheets.

---

DEVELOPMENT TEAM:
| Matrics No.    | Name     |
|--------------|--------------|
| B032310196   | KING SOON KIT  |
| B032310257   | MUHAMMAD ARIF AIMAN |
| B032310645   | SHOMESWARAN A/L MUGUNTHAN |
| B032310805   | BOO JIA JUN |
| row 5   | row 5 |
| row 6   | row 6 |


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
- Built using Spring Boot or Node.js
- Connects to MySQL database
- Provides endpoints for:
  - Mark submission
  - Student login
  - Result retrieval
  - Manual export to Google Sheets
