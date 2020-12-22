-- -------------------------------------------------------------
-- TablePlus 3.12.0(354)
--
-- https://tableplus.com/
--
-- Database: eladmin
-- Generation Time: 2020-12-08 21:25:15.9640
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('191', NULL, '14', '0', '康约平台', NULL, NULL, '2', 'date', 'ptt', b'0', b'0', b'0', NULL, 'admin', 'admin', '2020-12-01 11:25:58', '2020-12-01 11:25:58'),
('192', '191', '3', '1', '医院管理', 'Org', 'ptt/org/index', '1', 'app', 'org', b'0', b'0', b'0', 'org:list', 'admin', 'admin', '2020-12-01 11:27:18', '2020-12-01 11:27:27'),
('193', '192', '0', '2', '医院新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'org:add', 'admin', 'admin', '2020-12-01 11:27:48', '2020-12-01 11:27:48'),
('194', '192', '0', '2', '医院修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'org:edit', 'admin', 'admin', '2020-12-01 11:28:05', '2020-12-01 11:28:05'),
('195', '192', '0', '2', '医院删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'org:del', 'admin', 'admin', '2020-12-01 11:28:21', '2020-12-01 11:28:21'),
('196', '191', '3', '1', '资源类型', 'ResourceType', 'ptt/resourceType/index', '2', 'server', 'resourceType', b'0', b'0', b'0', 'resourceType:list', 'admin', 'admin', '2020-12-01 17:30:36', '2020-12-01 17:30:36'),
('197', '196', '0', '2', ' 资源类型新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'resourceType:add', 'admin', 'admin', '2020-12-01 17:30:58', '2020-12-01 17:30:58'),
('198', '196', '0', '2', ' 资源类型修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'resourceType:edit', 'admin', 'admin', '2020-12-01 17:31:16', '2020-12-01 17:31:16'),
('199', '196', '0', '2', '资源类型移除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'resourceType:del', 'admin', 'admin', '2020-12-01 17:31:44', '2020-12-01 17:31:44'),
('200', '191', '3', '1', '资源管理', 'Resource', 'ptt/resource/index', '3', 'database', 'resource', b'0', b'0', b'0', 'resource:list', 'admin', 'admin', '2020-12-01 18:15:57', '2020-12-05 10:47:14'),
('201', '200', '0', '2', '资源新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'resource:add', 'admin', 'admin', '2020-12-01 18:16:22', '2020-12-05 10:47:27'),
('202', '200', '0', '2', '资源编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'resource:edit', 'admin', 'admin', '2020-12-01 18:16:46', '2020-12-05 10:47:36'),
('203', '200', '0', '2', '资源删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'resource:del', 'admin', 'admin', '2020-12-01 18:17:04', '2020-12-05 10:47:51'),
('204', '191', '3', '1', '套餐管理', 'Term', 'ptt/term/index', '4', 'database', 'term', b'0', b'0', b'0', 'term:list', 'admin', 'admin', '2020-12-01 22:36:29', '2020-12-05 10:48:03'),
('205', '204', '0', '2', '套餐添加', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'term:add', 'admin', 'admin', '2020-12-01 22:37:07', '2020-12-01 22:37:07'),
('206', '204', '0', '2', '套餐修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'term:edit', 'admin', 'admin', '2020-12-01 22:37:34', '2020-12-01 22:37:34'),
('207', '204', '0', '2', '套餐移除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'term:del', 'admin', 'admin', '2020-12-01 22:37:51', '2020-12-01 22:37:51'),
('208', '191', '3', '1', '套餐资源类型', 'TermResourceType', 'ptt/termResourceType/index', '5', 'database', 'termResourceType', b'0', b'0', b'0', 'termResourceType:list', 'admin', 'admin', '2020-12-01 22:58:21', '2020-12-01 22:58:21'),
('209', '208', '0', '2', ' 套餐资源类型新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'termResourceType:add', 'admin', 'admin', '2020-12-01 22:58:59', '2020-12-01 22:58:59'),
('210', '208', '0', '2', '套餐资源类型修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'termResourceType:edit', 'admin', 'admin', '2020-12-01 22:59:21', '2020-12-01 22:59:21'),
('211', '208', '0', '2', '套餐资源类型移除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'termResourceType:del', 'admin', 'admin', '2020-12-01 22:59:44', '2020-12-01 22:59:44'),
('212', '191', '3', '1', '患者管理', 'Patient', 'ptt/patient/index', '7', 'people', 'patient', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-02 16:58:39', '2020-12-05 10:48:13'),
('213', '212', '0', '2', '患者添加', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-02 16:59:01', '2020-12-02 16:59:01'),
('214', '212', '0', '2', '患者修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-02 16:59:25', '2020-12-02 16:59:25'),
('215', '212', '0', '2', '患者移除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-02 16:59:40', '2020-12-02 16:59:40'),
('216', '191', '3', '1', '患者同步', 'QueryPatient', 'ptt/patient/queryPatient', '6', 'people', 'queryPatient', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-02 17:41:58', '2020-12-02 17:41:58'),
('217', '216', '0', '2', '患者同步新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-02 17:42:48', '2020-12-02 17:42:48'),
('218', '216', '0', '2', '患者同步编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-02 17:43:06', '2020-12-02 17:43:48'),
('219', '216', '0', '2', '患者同步删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-02 17:43:28', '2020-12-02 17:43:28'),
('220', '191', '3', '1', '患者医院', 'PatientDept', 'ptt/patientDept/index', '8', 'people', 'patientDept', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-03 14:08:13', '2020-12-03 14:08:13'),
('221', '220', '0', '2', '患者医院新址', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-03 14:08:38', '2020-12-03 14:08:38'),
('222', '220', '0', '2', '患者医院修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-03 14:08:57', '2020-12-03 14:08:57'),
('223', '220', '0', '2', '患者医院删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-03 14:09:18', '2020-12-03 14:09:18'),
('224', '191', '3', '1', '患者套餐', 'PatientTerm', 'ptt/patientTerm/index', '9', 'database', 'patientTerm', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-03 16:52:07', '2020-12-03 16:52:07'),
('225', '224', '0', '2', '患者套餐新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-03 16:52:31', '2020-12-03 16:52:31'),
('226', '224', '0', '2', '患者套餐编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-03 16:52:49', '2020-12-03 16:52:49'),
('227', '224', '0', '2', '患者套餐删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-03 16:53:05', '2020-12-03 16:53:05'),
('228', '191', '3', '1', '患者套餐日志', 'PatientTermLog', 'ptt/patientTermLog/index', '10', 'log', 'patientTermLog', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-03 17:38:45', '2020-12-03 17:38:45'),
('229', '228', '0', '2', '患者套餐日志新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-03 17:39:10', '2020-12-03 17:39:10'),
('230', '228', '0', '2', '患者套餐日志编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-03 17:39:28', '2020-12-03 17:39:28'),
('231', '228', '0', '2', '患者套餐日志删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-03 17:39:42', '2020-12-03 17:39:42'),
('232', '191', '3', '1', '患者预约', 'PatientTermReserve', 'ptt/patientTermReserve/index', '11', 'develop', 'patientTermReserve', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-05 15:09:29', '2020-12-05 15:09:29'),
('233', '232', '0', '2', '患者预约新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-05 15:10:22', '2020-12-05 15:10:22'),
('234', '232', '0', '2', '患者预约编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-05 15:10:43', '2020-12-05 15:10:43'),
('235', '232', '0', '2', '患者预约删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-05 15:10:58', '2020-12-05 15:10:58'),
('236', '191', '3', '1', '患者预约日志', 'PatientTermReserveLog', 'ptt/patientTermReserveLog/index', '12', 'develop', 'patientTermReserveLog', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-05 15:11:36', '2020-12-05 15:11:36'),
('237', '236', '0', '2', '患者预约日志新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-05 15:11:50', '2020-12-05 15:11:50'),
('238', '236', '0', '2', '患者预约日志编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-05 15:12:05', '2020-12-05 15:12:05'),
('239', '236', '0', '2', '患者预约日志删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-05 15:12:18', '2020-12-05 15:12:18'),
('240', '191', '3', '1', '自定义管理', 'Cus', 'ptt/cus/index', '13', 'theme', 'cus', b'0', b'0', b'0', 'cus:list', 'admin', 'admin', '2020-12-05 17:42:17', '2020-12-05 17:42:17'),
('241', '240', '0', '2', ' 自定义新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'cus:add', 'admin', 'admin', '2020-12-05 17:42:41', '2020-12-05 17:42:41'),
('242', '240', '0', '2', ' 自定义修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'cus:edit', 'admin', 'admin', '2020-12-05 17:43:00', '2020-12-05 17:43:00'),
('243', '240', '0', '2', ' 自定义删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'cus:del', 'admin', 'admin', '2020-12-05 17:43:12', '2020-12-05 17:43:12'),
('244', '191', '3', '1', '患者自定义信息', 'PatientWithDept', 'ptt/patient/index_with_dept', '14', 'app', 'patientWithDept', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-08 15:26:46', '2020-12-08 15:26:46'),
('245', '244', '0', '2', '患者自定义新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-08 15:27:10', '2020-12-08 15:27:10'),
('246', '244', '0', '2', '患者自定义编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-08 15:27:28', '2020-12-08 15:27:28'),
('247', '244', '0', '2', '患者自定义删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-08 15:27:42', '2020-12-08 15:27:42');


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;