DROP DATABASE IF EXISTS `mars`;
CREATE DATABASE mars;
use mars;

-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: mars
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app_mapping`
--

DROP TABLE IF EXISTS `app_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) DEFAULT NULL COMMENT 'app??',
  `host` varchar(100) DEFAULT NULL COMMENT '?????',
  `app_path` varchar(255) DEFAULT NULL COMMENT '????',
  `app_start_shell` varchar(255) DEFAULT NULL COMMENT '??????',
  `docker_name` varchar(50) DEFAULT NULL COMMENT 'docker????',
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_mapping`
--

LOCK TABLES `app_mapping` WRITE;
/*!40000 ALTER TABLE `app_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auto_test_case_configuration`
--

DROP TABLE IF EXISTS `auto_test_case_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auto_test_case_configuration` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `business_line` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  `ssh_visit` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  `absolute_dir` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  `main_commend` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `relative_dir` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `parameter` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auto_test_case_configuration`
--

LOCK TABLES `auto_test_case_configuration` WRITE;
/*!40000 ALTER TABLE `auto_test_case_configuration` DISABLE KEYS */;
/*!40000 ALTER TABLE `auto_test_case_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auto_test_report_record`
--

DROP TABLE IF EXISTS `auto_test_report_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auto_test_report_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_line` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `result_path` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `report_path` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `shell_content` text CHARACTER SET utf8mb4,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auto_test_report_record`
--

LOCK TABLES `auto_test_report_record` WRITE;
/*!40000 ALTER TABLE `auto_test_report_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `auto_test_report_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common_admin`
--

DROP TABLE IF EXISTS `common_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common_admin` (
  `id` varchar(255) NOT NULL COMMENT '用户ID（主键）',
  `username` varchar(45) NOT NULL COMMENT '用户名',
  `password` varchar(128) DEFAULT NULL COMMENT '登录密码',
  `email` varchar(50) NOT NULL DEFAULT '' COMMENT '邮箱地址',
  `real_name` varchar(45) DEFAULT '' COMMENT '用户真实姓名',
  `mobile` varchar(20) DEFAULT '',
  `status` varchar(20) DEFAULT 'DISABLED' COMMENT 'ENABLED: 启用, DISABLED: 禁用',
  `create_timestamp` bigint(20) NOT NULL COMMENT '用户创建时间',
  `update_timestamp` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common_admin`
--

LOCK TABLES `common_admin` WRITE;
/*!40000 ALTER TABLE `common_admin` DISABLE KEYS */;
INSERT INTO `common_admin` VALUES ('U5F4E3B4C6E60FAFBE75AC21A','admin','123456','root@admin.com','admin',NULL,'ENABLED',1598962508560,1598962508560);
/*!40000 ALTER TABLE `common_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common_admin_role`
--

DROP TABLE IF EXISTS `common_admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common_admin_role` (
  `admin_id` varchar(255) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`admin_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common_admin_role`
--

LOCK TABLES `common_admin_role` WRITE;
/*!40000 ALTER TABLE `common_admin_role` DISABLE KEYS */;
INSERT INTO `common_admin_role` VALUES ('U5D243E58B69AB042C9D70B71',11);
/*!40000 ALTER TABLE `common_admin_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common_authority`
--

DROP TABLE IF EXISTS `common_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID（主键）',
  `name` varchar(191) NOT NULL COMMENT '权限名称',
  `authority` varchar(191) NOT NULL COMMENT '权限URL Pattern',
  `method` varchar(10) NOT NULL COMMENT '权限URL 方法',
  `description` text COMMENT '权限描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_authority_method` (`authority`,`method`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common_authority`
--

LOCK TABLES `common_authority` WRITE;
/*!40000 ALTER TABLE `common_authority` DISABLE KEYS */;
INSERT INTO `common_authority` VALUES (1,'listHttpMaterial','GET:/http-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(2,'deleteById','DELETE:/http-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(3,'update','PUT:/http-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(4,'save','POST:/http-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(5,'listTaskRecords','GET:/task-record','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(6,'save','POST:/task-record','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(7,'listDocument','GET:/app-mapping','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(8,'deleteById','DELETE:/test-case-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(9,'listTestCase','GET:/test-case-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(10,'update','PUT:/test-case-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(11,'save','POST:/test-case-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(12,'upload','POST:/file/upload','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(13,'deleteById','DELETE:/redis-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(14,'listRedisMaterial','GET:/redis-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(15,'update','PUT:/redis-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(16,'save','POST:/redis-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(17,'deleteById','DELETE:/service-mapping','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(18,'listAll','GET:/service-mapping/all','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(19,'listByUserId','GET:/service-mapping','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(20,'update','PUT:/service-mapping','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(21,'save','POST:/service-mapping','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(22,'listFormField','GET:/form-field','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(23,'save','POST:/form-field','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(24,'addNewTestCase','POST:/testcase/configuration/add','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(25,'deleteTestCase','POST:/testcase/configuration/delete','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(26,'updateTestCase','POST:/testcase/configuration/update','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(27,'getAutoCaseConfigurationByBusinessLine','GET:/testcase/configuration/businessLine/{businessLine}','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(28,'getAllBusinessLine','GET:/testcase/configuration/all/businessLine','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(29,'updateBusinessLine','POST:/testcase/configuration/businessLine/update/name','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(30,'execute','POST:/testcase/configuration/execute/{businessLine}','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(31,'deleteById','DELETE:/mq-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(32,'listMqMaterial','GET:/mq-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(33,'update','PUT:/mq-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(34,'save','POST:/mq-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(35,'save','POST:/test-record','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(36,'deleteById','DELETE:/test-group-case','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(37,'listTestGroupCase','GET:/test-group-case','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(38,'updatePriorityById','PUT:/test-group-case','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(39,'save','POST:/test-group-case','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(40,'run',':/task/{taskBeanName}/{taskMethodName}/run','','Added since Wed Sep 02 14:13:27 CST 2020'),(41,'listFieldCategory','GET:/field-category','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(42,'save','POST:/field-category','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(43,'deleteById','DELETE:/nested-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(44,'listAll','GET:/nested-material/all','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(45,'listByMaterialType','GET:/nested-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(46,'update','PUT:/nested-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(47,'save','POST:/nested-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(48,'deleteById','DELETE:/test-group','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(49,'listTestGroup','GET:/test-group','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(50,'update','PUT:/test-group','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(51,'save','POST:/test-group','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(52,'getAutoTestSleep','GET:/variable/sleep','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(53,'setAutoTestSleep','POST:/variable/sleep','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(54,'logout','POST:/auth/logout','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(55,'login','POST:/auth/login','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(56,'deleteById','DELETE:/share-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(57,'listShareMaterial','GET:/share-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(58,'saveShareMaterialParams','POST:/share-material/params','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(59,'updateShareMaterialParams','PUT:/share-material/params','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(60,'deleteShareMaterialParamsById','DELETE:/share-material/params','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(61,'update','PUT:/share-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(62,'save','POST:/share-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(63,'deleteById','DELETE:/data-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(64,'listSqlMaterial','GET:/data-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(65,'getById','GET:/data-material/{id}','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(66,'update','PUT:/data-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(67,'save','POST:/data-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(68,'deleteById','DELETE:/mongo-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(69,'listSqlMaterial','GET:/mongo-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(70,'update','PUT:/mongo-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(71,'save','POST:/mongo-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(72,'addRole','POST:/admin/roles','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(73,'getMe','GET:/admin/me','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(74,'getAuthorities','GET:/admin/authorities','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(75,'updateRole','POST:/admin/roles/{roleId}/update','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(76,'deleteRole','POST:/admin/roles/{roleId}/delete','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(77,'getAdmins','GET:/admin/users','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(78,'getAdmin','GET:/admin/users/{userId}','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(79,'addAdmin','POST:/admin/users','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(80,'updateAdmin','POST:/admin/users/{userId}/update','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(81,'getRole','GET:/admin/roles/{roleId}','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(82,'getRoles','GET:/admin/roles','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(83,'deleteById','DELETE:/env','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(84,'listSqlMaterial','GET:/env','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(85,'update','PUT:/env','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(86,'save','POST:/env','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(87,'listTaskGroups','GET:/task-group','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(88,'deleteById','DELETE:/sql-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(89,'listSqlMaterial','GET:/sql-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(90,'update','PUT:/sql-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(91,'save','POST:/sql-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(92,'deleteById','DELETE:/document','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(93,'listDocument','GET:/document','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(94,'getById','GET:/document/{id}','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(95,'updateLock','PUT:/document/{id}','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(96,'update','PUT:/document','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(97,'save','POST:/document','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(98,'deleteById','DELETE:/mock-material','DELETE','Added since Wed Sep 02 14:13:27 CST 2020'),(99,'listMockMaterial','GET:/mock-material','GET','Added since Wed Sep 02 14:13:27 CST 2020'),(100,'update','PUT:/mock-material','PUT','Added since Wed Sep 02 14:13:27 CST 2020'),(101,'save','POST:/mock-material','POST','Added since Wed Sep 02 14:13:27 CST 2020'),(102,'cleanerToNestedMaterial','POST:/cleaner','POST','Added since Wed Sep 02 14:13:27 CST 2020');
/*!40000 ALTER TABLE `common_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common_role`
--

DROP TABLE IF EXISTS `common_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID（主键）',
  `name` varchar(45) NOT NULL COMMENT '角色名称',
  `status` varchar(20) NOT NULL DEFAULT 'DISABLED' COMMENT 'ENABLED: 启用, DISABLED: 禁用',
  `description` text COMMENT '角色描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common_role`
--

LOCK TABLES `common_role` WRITE;
/*!40000 ALTER TABLE `common_role` DISABLE KEYS */;
INSERT INTO `common_role` VALUES (11,'ADMIN','ENABLED',NULL),(12,'NORMAL','ENABLED',NULL);
/*!40000 ALTER TABLE `common_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common_role_authority`
--

DROP TABLE IF EXISTS `common_role_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common_role_authority` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`,`authority_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common_role_authority`
--

LOCK TABLES `common_role_authority` WRITE;
/*!40000 ALTER TABLE `common_role_authority` DISABLE KEYS */;
INSERT INTO `common_role_authority` VALUES (0,1),(0,2),(0,3),(0,4),(0,5),(0,6),(0,7),(0,8),(0,9),(0,10),(0,11),(0,12),(0,13),(0,14),(0,15),(0,16),(0,17),(0,18),(0,19),(0,20),(0,21),(0,22),(0,23),(0,24),(0,25),(0,26),(0,27),(0,28),(0,29),(0,30),(0,31),(0,32),(0,33),(0,34),(0,35),(0,36),(0,37),(0,38),(0,39),(0,40),(0,41),(0,42),(0,43),(0,44),(0,45),(0,46),(0,47),(0,48),(0,49),(0,50),(0,51),(0,52),(0,53),(0,54),(0,55),(0,56),(0,57),(0,58),(0,59),(0,60),(0,61),(0,62),(0,63),(0,64),(0,65),(0,66),(0,67),(0,68),(0,69),(0,70),(0,71),(0,72),(0,73),(0,74),(0,75),(0,76),(0,77),(0,78),(0,79),(0,80),(0,81),(0,82),(0,83),(0,84),(0,85),(0,86),(0,87),(0,88),(0,89),(0,90),(0,91),(0,92),(0,93),(0,94),(0,95),(0,96),(0,97),(0,98),(0,99),(0,100),(0,101),(0,102);
/*!40000 ALTER TABLE `common_role_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_material`
--

DROP TABLE IF EXISTS `data_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_material` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `user_id` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `describe` mediumtext,
  `data` mediumtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `group_name` varchar(50) DEFAULT NULL,
  `second_group_name` varchar(50) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定义原始数据的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_material`
--

LOCK TABLES `data_material` WRITE;
/*!40000 ALTER TABLE `data_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `data_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(60) DEFAULT NULL,
  `content` mediumtext,
  `user_id` varchar(255) DEFAULT NULL,
  `md_content` mediumtext,
  `ext_id` varchar(255) DEFAULT NULL,
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lock` tinyint(4) DEFAULT '0',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `env_config`
--

DROP TABLE IF EXISTS `env_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `env_config` (
  `id` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `user_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
  `http` mediumtext CHARACTER SET utf8,
  `sql` mediumtext CHARACTER SET utf8,
  `redis` mediumtext CHARACTER SET utf8,
  `mq` mediumtext CHARACTER SET utf8,
  `mongo` mediumtext CHARACTER SET utf8,
  `describe` mediumtext CHARACTER SET utf8,
  `group_name` varchar(50) DEFAULT NULL,
  `global_variable` mediumtext CHARACTER SET utf8,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环境配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `env_config`
--

LOCK TABLES `env_config` WRITE;
/*!40000 ALTER TABLE `env_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `env_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_category`
--

DROP TABLE IF EXISTS `field_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `field_category` (
  `id` varchar(255) NOT NULL,
  `data_type` varchar(20) DEFAULT NULL COMMENT '数据类型',
  `category` varchar(20) DEFAULT NULL COMMENT '字段类别',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '当前时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段类别表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_category`
--

LOCK TABLES `field_category` WRITE;
/*!40000 ALTER TABLE `field_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_field`
--

DROP TABLE IF EXISTS `form_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `form_field` (
  `id` varchar(255) NOT NULL,
  `field_category_id` varchar(255) NOT NULL COMMENT '字段类别',
  `field_value` varchar(255) NOT NULL COMMENT '字段值',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_field`
--

LOCK TABLES `form_field` WRITE;
/*!40000 ALTER TABLE `form_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `id` varchar(255) NOT NULL,
  `title` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `brief` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `http_material`
--

DROP TABLE IF EXISTS `http_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `http_material` (
  `id` varchar(255) NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '请求名称',
  `method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `headers` mediumtext COMMENT '请求头',
  `params` mediumtext COMMENT '请求参数',
  `data` mediumtext COMMENT '请求body参数',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` varchar(255) DEFAULT NULL,
  `environment` varchar(20) DEFAULT NULL,
  `url` text,
  `data_type` varchar(20) DEFAULT 'JSON' COMMENT '请求post接口的数据类型，可以为JSON，FILE',
  `group_name` varchar(50) DEFAULT NULL,
  `param_replace` mediumtext,
  `second_group_name` varchar(50) DEFAULT NULL,
  `data_handle_type` varchar(255) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='http请求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `http_material`
--

LOCK TABLES `http_material` WRITE;
/*!40000 ALTER TABLE `http_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `http_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jacoco_config`
--

DROP TABLE IF EXISTS `jacoco_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jacoco_config` (
  `id` int(100) NOT NULL AUTO_INCREMENT,
  `ip` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  `ports` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  `project_directory` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  `report_directory` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `ssh_identity` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `ssh_known_host` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `branch_name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `new_tag` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `old_tag` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `remote_uri` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `need_compile` tinyint(1) NOT NULL DEFAULT '1',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jacoco_config`
--

LOCK TABLES `jacoco_config` WRITE;
/*!40000 ALTER TABLE `jacoco_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `jacoco_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mock_material`
--

DROP TABLE IF EXISTS `mock_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mock_material` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `method` varchar(200) DEFAULT NULL,
  `material` mediumtext COMMENT 'mock返回的数据',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='mock表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mock_material`
--

LOCK TABLES `mock_material` WRITE;
/*!40000 ALTER TABLE `mock_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `mock_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mongo_material`
--

DROP TABLE IF EXISTS `mongo_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mongo_material` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `action` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `material` mediumtext,
  `datasource` varchar(255) DEFAULT NULL,
  `database` varchar(255) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `second_group_name` varchar(255) DEFAULT NULL,
  `schema` mediumtext,
  `param_replace` mediumtext,
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mongo_material`
--

LOCK TABLES `mongo_material` WRITE;
/*!40000 ALTER TABLE `mongo_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `mongo_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mq_material`
--

DROP TABLE IF EXISTS `mq_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mq_material` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `environment` varchar(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `exchange` varchar(255) DEFAULT NULL,
  `routing_key` varchar(255) DEFAULT NULL,
  `content` mediumtext,
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `group_name` varchar(50) DEFAULT NULL,
  `param_replace` mediumtext,
  `second_group_name` varchar(50) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='rabbitmq表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mq_material`
--

LOCK TABLES `mq_material` WRITE;
/*!40000 ALTER TABLE `mq_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `mq_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nested_material`
--

DROP TABLE IF EXISTS `nested_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nested_material` (
  `id` varchar(255) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `parent_id` varchar(255) DEFAULT NULL COMMENT '父节点id',
  `material_id` varchar(255) DEFAULT NULL COMMENT '资源id',
  `material_type` varchar(20) DEFAULT NULL COMMENT 'HTTP, SQL, REDIS, DATA...',
  `type` varchar(20) DEFAULT 'CATALOG' COMMENT 'ITEM, CATALOG',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源嵌套表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nested_material`
--

LOCK TABLES `nested_material` WRITE;
/*!40000 ALTER TABLE `nested_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `nested_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `redis_material`
--

DROP TABLE IF EXISTS `redis_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `redis_material` (
  `id` varchar(255) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `action` varchar(10) DEFAULT NULL COMMENT '执行动作',
  `key` varchar(1000) DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` varchar(255) DEFAULT NULL,
  `environment` varchar(20) DEFAULT NULL,
  `datasource` varchar(50) DEFAULT NULL,
  `database` varchar(50) DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `param_replace` mediumtext,
  `second_group_name` varchar(50) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='执行redis表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `redis_material`
--

LOCK TABLES `redis_material` WRITE;
/*!40000 ALTER TABLE `redis_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `redis_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_health_check_mapping`
--

DROP TABLE IF EXISTS `service_health_check_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_health_check_mapping` (
  `id` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `user_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `docker_name` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
  `environment_name` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
  `need_check` tinyint(4) DEFAULT '1',
  `status` varchar(20) CHARACTER SET utf8 DEFAULT 'RUNNING',
  `cmd` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
  `restart_cmd` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `stop_cmd` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `create_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='服务健康检查配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_health_check_mapping`
--

LOCK TABLES `service_health_check_mapping` WRITE;
/*!40000 ALTER TABLE `service_health_check_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `service_health_check_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `share_material`
--

DROP TABLE IF EXISTS `share_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `share_material` (
  `id` varchar(255) NOT NULL,
  `type` varchar(20) NOT NULL COMMENT '分享类型：CASE, HTTP, REDIS, SQL, MQ, MONGO',
  `user_id` varchar(255) DEFAULT NULL COMMENT '分享者id',
  `material_id` varchar(255) DEFAULT NULL COMMENT '分享的资源id',
  `shared_user_ids` mediumtext COMMENT '被分享人的用户id列表',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_material_id_` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share_material`
--

LOCK TABLES `share_material` WRITE;
/*!40000 ALTER TABLE `share_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `share_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `share_material_params`
--

DROP TABLE IF EXISTS `share_material_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `share_material_params` (
  `id` varchar(255) NOT NULL,
  `share_material_id` varchar(50) NOT NULL COMMENT '分享资源id',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `data_material_id` varchar(255) DEFAULT NULL COMMENT '数据源id',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share_material_params`
--

LOCK TABLES `share_material_params` WRITE;
/*!40000 ALTER TABLE `share_material_params` DISABLE KEYS */;
/*!40000 ALTER TABLE `share_material_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sql_material`
--

DROP TABLE IF EXISTS `sql_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sql_material` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `environment` varchar(20) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL COMMENT 'sql名称',
  `material` mediumtext COMMENT 'sql语句',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `datasource` varchar(50) DEFAULT NULL,
  `database` varchar(50) DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `param_replace` mediumtext,
  `second_group_name` varchar(50) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='sql执行表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sql_material`
--

LOCK TABLES `sql_material` WRITE;
/*!40000 ALTER TABLE `sql_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `sql_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_record`
--

DROP TABLE IF EXISTS `task_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_record` (
  `id` varchar(255) NOT NULL,
  `task_id` varchar(255) DEFAULT NULL COMMENT '任务id',
  `task_type` varchar(20) DEFAULT NULL COMMENT '任务类型',
  `task_delay` varchar(20) DEFAULT NULL COMMENT '任务延长执行时间',
  `task_assert` mediumtext COMMENT '任务断言',
  `assert_result` tinyint(4) DEFAULT NULL COMMENT '任务断言结果，0是失败，1是成功',
  `task_result` mediumtext COMMENT '任务结果',
  `test_case_id` varchar(255) DEFAULT NULL COMMENT '测试用例id',
  `test_group_id` varchar(255) DEFAULT NULL COMMENT '测试组id',
  `execute_time` bigint(20) DEFAULT NULL COMMENT '执行任务的时间',
  `user_id` varchar(255) DEFAULT NULL,
  `environment` varchar(255) DEFAULT NULL,
  `task_param` mediumtext,
  `task_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='执行任务的报告';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_record`
--

LOCK TABLES `task_record` WRITE;
/*!40000 ALTER TABLE `task_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_case`
--

DROP TABLE IF EXISTS `test_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_case` (
  `id` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT '接口url',
  `method` varchar(10) DEFAULT NULL COMMENT '接口方法',
  `request_data` mediumtext COMMENT '请求参数',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(100) DEFAULT NULL COMMENT '测试用例名称',
  `body` mediumtext COMMENT 'post请求数据',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `environment` varchar(20) DEFAULT NULL COMMENT '测试环境',
  `material` mediumtext,
  `group_name` varchar(50) DEFAULT NULL,
  `second_group_name` varchar(50) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_case`
--

LOCK TABLES `test_case` WRITE;
/*!40000 ALTER TABLE `test_case` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_group`
--

DROP TABLE IF EXISTS `test_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_group` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `name` varchar(60) DEFAULT NULL COMMENT '测试组名称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `environment` varchar(20) DEFAULT NULL COMMENT '测试组的环境，例如（staging，production)',
  `material` mediumtext,
  `concurrency` tinyint(4) DEFAULT '1' COMMENT '是否并发执行测试用例',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_group`
--

LOCK TABLES `test_group` WRITE;
/*!40000 ALTER TABLE `test_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_group_case`
--

DROP TABLE IF EXISTS `test_group_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_group_case` (
  `id` varchar(255) NOT NULL,
  `test_group_id` varchar(255) NOT NULL COMMENT '测试组id',
  `test_case_id` varchar(255) NOT NULL COMMENT '测试用例id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `delay` int(11) DEFAULT NULL COMMENT '延长多长时间执行，单位秒',
  `assert_condition` mediumtext COMMENT '断言，校验结果正确性',
  `pre_condition` mediumtext COMMENT '前置条件，在请求前需要做什么',
  `post_condition` mediumtext COMMENT '后置条件，在请求后需要做什么',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `environment` varchar(20) DEFAULT NULL COMMENT '测试环境',
  `priority` int(11) DEFAULT '0' COMMENT '在测试组中的执行顺序',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试组实例';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_group_case`
--

LOCK TABLES `test_group_case` WRITE;
/*!40000 ALTER TABLE `test_group_case` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_group_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_record`
--

DROP TABLE IF EXISTS `test_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_record` (
  `id` varchar(255) NOT NULL,
  `test_group_case_id` varchar(255) DEFAULT NULL COMMENT '测试组用例id',
  `record` mediumtext COMMENT '报告',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_record`
--

LOCK TABLES `test_record` WRITE;
/*!40000 ALTER TABLE `test_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `mobile_number` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `create_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `variable`
--

DROP TABLE IF EXISTS `variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `variable` (
  `name` varchar(50) NOT NULL,
  `value` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variable`
--

LOCK TABLES `variable` WRITE;
/*!40000 ALTER TABLE `variable` DISABLE KEYS */;
/*!40000 ALTER TABLE `variable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-02 14:14:54
