-- 国家行政区域代码表
 CREATE TABLE `wc_res` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(200) DEFAULT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8

insert into test.wc_res(word,count) values('hello',1)