-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 04, 2025 at 03:04 PM
-- Server version: 8.0.30
-- PHP Version: 8.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `erms_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `mark`
--

CREATE TABLE `mark` (
  `markID` int NOT NULL,
  `studentID` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `subjectID` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `teacherID` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `score` int DEFAULT NULL,
  `grade` varchar(2) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mark`
--

INSERT INTO `mark` (`markID`, `studentID`, `subjectID`, `teacherID`, `score`, `grade`) VALUES
(1, 'B032310805', 'BITP 2223', 'T001', 88, 'A'),
(2, 'B032310011', 'BITP 2223', 'T001', 90, 'A'),
(3, 'B032310012', 'BITP 3123', 'T003', 55, 'D'),
(4, 'B032310068', 'BITP 3453 ', 'T003', 77, 'B'),
(5, 'B032310002', 'BITP 3123', 'T003', 92, 'A'),
(6, 'B032310002', 'BITP 2223', 'T001', 87, 'A'),
(8, 'B032310135', 'BITP 2223', 'T001', 85, 'A'),
(9, 'B032310108', 'BITP 2223', 'T001', 77, 'B');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `studentID` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `studentName` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `class` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`studentID`, `studentName`, `class`, `password`) VALUES
('B032310002', 'AHMAD NAQIUDDIN BIN MOHAMAD', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310011', 'KISHAH A/P PRAKHASH', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310012', 'LISHA ROSHINEE A/P GANESAN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310068', 'NURUL IZZATI BINTI ABDUL RAZAK', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310093', 'SHARON ELYIA ANAK LAYANG', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310108', 'NUR AINA SOFEA BINTI AHMAD NAZZIB', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310126', 'MUHAMMAD KHAIRUL HAQIM BIN SUHAIDIE', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310135', 'SITI BALQIS BINTI MAT MUHARAM', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310136', 'NURLIYANA ATHIRAH BINTI ROSLI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310139', 'PAVITHRAN A/L KANAPATHY', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310148', 'NUR AQILAH BINTI ZAIDI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310156', 'PUTERI FARHAH BINTI HANAPI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310158', 'MOHAMAD IMAN AKMAL BIN ISMAIL', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310160', 'NURALIA AMANDA BINTI MOHD AKHSAN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310162', 'MUHAMMAD AKMAL HAKIM HISHAMUDDIN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310176', 'CHAMNAN CHONG LIK XIONG', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310178', 'MOHAMAD HAIKAL BIN ZABIDI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310183', 'OOI XIEN XIEN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310192', 'HANI NADHIRA BINTI ABDUL AZIZ', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310196', 'KING SOON KIT', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310206', 'MUHAMMAD ZULHUSNI BIN MUHAMMAD ZAIMI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310214', 'AMIR HAMZAH BIN MOHD ZAMRI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310217', 'MUHAMMAD ZULHELMI BIN NOOR AFENDI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310222', 'LIEW WEN JIN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310228', 'AZRUL HAFIZ BIN ABDULLAH', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310256', 'SITI ZULAIKHA BINTI MOHD NIZAM', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310257', 'MUHAMMAD ARIF AIMAN BIN KARIM', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310260', 'NUR HILWANI BINTI MOHD RADZI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310265', 'NG CHUN KEAT', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310274', 'DHANISHAVARSHINI A/P MUHINTHAN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310275', 'NOR AMIRA HUSNA BINTI ABDUL RAHIM', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310277', 'NUR KHAIRUNNAJWA BINTI KUSAIRI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310282', 'MUHAMMAD ZUHAIRY BIN RAZALY', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310284', 'WAN MUHAMMAD HAZIQ DARWISY BIN WAN REMLE', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310291', 'NURUL NAZWA BINTI MOHD HAFILZUDDIN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310314', 'AFIQ AIMAN BIN MAZLAN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310357', 'NUR ALIA MUNIRAH BINTI ABD RAHIM', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310401', 'LOH JIA YING', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310425', 'EZZAH NAZIRAH BINTI DZULKAFLI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310428', 'ZURINE AISHAH BINTI ZULKIFLI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310456', 'AMANDA VERA YAPP', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310457', 'NURATIQAH SAFIAH BINTI AZLI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310505', 'TEE YONG JIAN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310523', 'HARIS A/L R SURESH', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310533', 'TAN LE XIONG', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310544', 'MUHAMMAD HARITH BIN SHARIZAL', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310553', 'NURAIN ZAHIDAH BINTI MOHD YUSOF', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310594', 'SYAZNUR AIDA QAREEN BINTI SAHRIL HAPIDZ', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310619', 'NABILA BATRISYIA BINTI MOHD MARZUKI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310645', 'SHOMESWARAN A/L MUGUNTHAN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310685', 'NURKAYLA AALIYAH BINTI MOHAMMAD SAINI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310701', 'ASZFARWIZAH BINTI ASZNI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310708', 'WAN MOHAMMAD ZAMAN AZ ZIKRY BIN ZAINUDDIN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310727', 'MUHAMMAD AMIERUL BIN CHE WAN MOHD YAZID', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310748', 'NURUL SYAHIRA BINTI', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310783', 'NOOR ELYANA ASSYURA BINTI MUHD JAMIRON', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310801', 'MADHANAVILASHAN A/L GANAPATHY', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('B032310805', 'BOO JIA JUN', 'BITS', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK');

-- --------------------------------------------------------

--
-- Table structure for table `subject`
--

CREATE TABLE `subject` (
  `subjectID` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `subjectName` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `teacherID` varchar(50) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subject`
--

INSERT INTO `subject` (`subjectID`, `subjectName`, `teacherID`) VALUES
('BITP 2223', 'Software Requirement and Design', 'T001'),
('BITP 3123', ' Distributed Application Development', 'T003'),
('BITP 3253', ' Software Validation and Verification', 'T002'),
('BITP 3453 ', 'Mobile Application Development', 'T003');

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE `teacher` (
  `teacherID` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `teacherName` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`teacherID`, `teacherName`, `password`) VALUES
('T001', 'DR. SABRINA BINTI AHMAD', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('T002', 'Dr. Raja Rina', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK'),
('T003', 'DR. MOHD HARIZ', '$2y$10$Rjf1E15QPkI/cBK2JiPTwuciaARAs3RO7lwC1owwg8/IcaTUh2sOK');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mark`
--
ALTER TABLE `mark`
  ADD PRIMARY KEY (`markID`),
  ADD KEY `studentID` (`studentID`),
  ADD KEY `subjectID` (`subjectID`),
  ADD KEY `teacherID` (`teacherID`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`studentID`);

--
-- Indexes for table `subject`
--
ALTER TABLE `subject`
  ADD PRIMARY KEY (`subjectID`),
  ADD KEY `teacherID` (`teacherID`);

--
-- Indexes for table `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`teacherID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mark`
--
ALTER TABLE `mark`
  MODIFY `markID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mark`
--
ALTER TABLE `mark`
  ADD CONSTRAINT `mark_ibfk_1` FOREIGN KEY (`studentID`) REFERENCES `student` (`studentID`),
  ADD CONSTRAINT `mark_ibfk_2` FOREIGN KEY (`subjectID`) REFERENCES `subject` (`subjectID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `mark_ibfk_3` FOREIGN KEY (`teacherID`) REFERENCES `teacher` (`teacherID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
