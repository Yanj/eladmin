-- -------------------------------------------------------------
-- TablePlus 3.12.0(354)
--
-- https://tableplus.com/
--
-- Database: eladmin
-- Generation Time: 2020-12-29 13:55:37.2320
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


INSERT INTO `yy_resource` (`id`, `dept_id`, `resource_category_id`, `name`, `count`, `status`, `remark`) VALUES
('3', '32', '5', 'INDIBA', '1', NULL, NULL),
('4', '32', '6', '产后塑形仪', '4', NULL, NULL),
('5', '32', '7', '菲蜜丽', '1', NULL, NULL),
('6', '32', '8', '骨盆理疗床', '2', NULL, NULL),
('7', '32', '9', '理疗师001', '1', NULL, NULL),
('8', '32', '10', '盆底修复A', '2', NULL, NULL),
('9', '32', '11', '盆底修复B', '2', NULL, NULL),
('10', '32', '12', '中医医生001', '1', NULL, NULL),
('11', '32', '12', '中医医生002', '1', NULL, NULL);

INSERT INTO `yy_resource_category` (`id`, `dept_id`, `name`, `status`, `remark`) VALUES
('5', '32', 'INDIBA', NULL, NULL),
('6', '32', '产后塑形仪', NULL, NULL),
('7', '32', '菲蜜丽', NULL, NULL),
('8', '32', '骨盆理疗床', NULL, NULL),
('9', '32', '理疗师', NULL, NULL),
('10', '32', '盆底修复A', NULL, NULL),
('11', '32', '盆底修复B', NULL, NULL),
('12', '32', '中医医生', NULL, NULL);

INSERT INTO `yy_resource_group` (`id`, `dept_id`, `name`, `status`, `remark`) VALUES
('33', '32', '产后塑形仪', NULL, NULL),
('34', '32', 'INDIBA', NULL, NULL),
('35', '32', '菲蜜丽', NULL, NULL),
('36', '32', '骨盆理疗床', NULL, NULL),
('37', '32', '理疗师', NULL, NULL),
('38', '32', '盆底修复A', NULL, NULL),
('39', '32', '盆底修复B', NULL, NULL),
('40', '32', '中医医生', NULL, NULL);

INSERT INTO `yy_resource_group_category` (`resource_group_id`, `resource_category_id`) VALUES
('33', '6'),
('34', '5'),
('35', '7'),
('36', '8'),
('37', '9'),
('38', '10'),
('39', '11'),
('40', '12');

INSERT INTO `yy_term` (`id`, `dept_id`, `code`, `name`, `price`, `original_price`, `times`, `unit`, `status`, `remark`) VALUES
('3', '32', 'ZLC070100', '魔力射频治疗-身体', '168000', '168000', '1', '次', NULL, NULL),
('4', '32', 'ZLC060031', 'INDIBA腿部', '228000', '228000', '1', '次', NULL, NULL),
('5', '32', 'ZLA073008', '产后康复（仪器+手法）', '30000', '30000', '1', '次', NULL, NULL),
('6', '32', 'ZLA073008004', '产后康复（仪器+手法）-产后排残乳', '56000', '56000', '1', '次', NULL, NULL),
('7', '32', 'ZLA073008001', '产后康复（仪器+手法）-产后塑形', '30000', '30000', '1', '每部位', NULL, NULL),
('8', '32', 'ZLA073008006', '产后康复（仪器+手法）-腹直肌分离治疗', '30000', '30000', '1', '每部位', NULL, NULL),
('9', '32', 'ZLA073008007', '产后康复（仪器+手法）-肩颈理疗', '36000', '36000', '1', '每部位', NULL, NULL),
('10', '32', 'ZLA073008008', '产后康复（仪器+手法）-腰腿疼痛', '30000', '30000', '1', '每部位', NULL, NULL),
('11', '32', 'ZLA073008002', '产后康复（仪器+手法）-子宫复旧', '36000', '36000', '1', '每部位', NULL, NULL),
('12', '32', 'ZLA070002', '催乳', '58000', '58000', '1', '次', NULL, NULL),
('13', '32', 'ZLA070001', '乳房按摩', '10000', '10000', '1', '次', NULL, NULL),
('14', '32', 'ZLA070015', '乳房塑形', '48000', '48000', '1', '次', NULL, NULL),
('15', '32', 'ZLA070003', '子宫复旧', '48000', '48000', '1', '次', NULL, NULL),
('16', '32', 'ZLC140019', '阴道激光治疗', '840000', '840000', '1', '次', NULL, NULL),
('17', '32', 'ZLC140020', '外阴激光治疗', '480000', '480000', '1', '次', NULL, NULL),
('18', '32', 'ZLC140021', '乳头激光治疗', '480000', '480000', '1', '次', NULL, NULL),
('19', '32', 'ZLC060010', '骨盆修复', '396000', '396000', '1', '次', NULL, NULL),
('20', '32', 'ZLA073011', '通乳', '38000', '38000', '1', '人次', NULL, NULL),
('21', '32', 'ZLA070006', '盆底修复', '49800', '49800', '1', '次', NULL, NULL),
('22', '32', 'ZLC150001', '成人针灸疗法', '20000', '20000', '1', '次', NULL, NULL),
('23', '32', 'ZLA073010', '通乳', '58000', '58000', '1', '人次', NULL, NULL),
('24', '32', 'ZLC150019', '药浴', '20000', '20000', '1', '次', NULL, NULL),
('25', '32', 'ZLC150017', '孕妇局部推拿', '16000', '16000', '1', '部位', NULL, NULL),
('26', '32', 'ZLC150018', '孕妇全身推拿', '52000', '52000', '1', '次', NULL, NULL),
('27', '32', 'ZLC153027', '孕妇身体局部疼痛缓解', '16000', '16000', '1', '项', NULL, NULL);

INSERT INTO `yy_term_resource_group` (`term_id`, `resource_group_id`) VALUES
('3', '34'),
('4', '34'),
('5', '33'),
('6', '33'),
('7', '33'),
('8', '33'),
('9', '33'),
('10', '33'),
('11', '33'),
('12', '33'),
('13', '33'),
('14', '33'),
('15', '33'),
('16', '35'),
('17', '35'),
('18', '35'),
('19', '36'),
('20', '37'),
('21', '38'),
('21', '39'),
('22', '40'),
('23', '40'),
('24', '40'),
('25', '40'),
('26', '40'),
('27', '40');

INSERT INTO `yy_work_time` (`id`, `dept_id`, `begin_time`, `end_time`, `status`, `remark`) VALUES
('4', '32', '08:00', '08:30', NULL, NULL),
('5', '32', '08:30', '09:00', NULL, NULL),
('6', '32', '09:00', '09:30', NULL, NULL),
('7', '32', '09:30', '10:00', NULL, NULL),
('8', '32', '10:00', '10:30', NULL, NULL),
('9', '32', '10:30', '11:00', NULL, NULL),
('10', '32', '11:00', '11:30', NULL, NULL),
('11', '32', '11:30', '12:00', NULL, NULL),
('12', '32', '12:00', '12:30', NULL, NULL),
('13', '32', '12:30', '13:00', NULL, NULL),
('14', '32', '13:00', '13:30', NULL, NULL),
('15', '32', '13:30', '14:00', NULL, NULL),
('16', '32', '14:00', '14:30', NULL, NULL),
('17', '32', '14:30', '15:00', NULL, NULL),
('18', '32', '15:00', '15:30', NULL, NULL),
('19', '32', '15:30', '16:00', NULL, NULL),
('20', '32', '16:00', '16:30', NULL, NULL),
('21', '32', '16:30', '17:00', NULL, NULL),
('22', '32', '17:00', '17:30', NULL, NULL),
('23', '32', '17:30', '18:00', NULL, NULL),
('24', '32', '18:00', '18:30', NULL, NULL);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;