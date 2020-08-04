CREATE TABLE `user` (
  `id` int(11) DEFAULT NULL,
  `userName` varchar(150) DEFAULT NULL,
  `userAge` varchar(150) DEFAULT NULL,
  `userAddress` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into mybatis.user (id, userName, userAge, userAddress) values (1, 'zhangsan', 18, 'hunan.changsha');
insert into mybatis.user (id, userName, userAge, userAddress) values (2, 'lisi', 19, 'hunan.changsha');
insert into mybatis.user (id, userName, userAge, userAddress) values (3, 'wangwu', 20, 'hunan.changsha');
insert into mybatis.user (id, userName, userAge, userAddress) values (4, 'zhaoliu', 21, 'hunan.changsha');
insert into mybatis.user (id, userName, userAge, userAddress) values (5, 'zhangsan2', 22, 'beijing');
insert into mybatis.user (id, userName, userAge, userAddress) values (6, 'lisi2', 23, 'beijing');
