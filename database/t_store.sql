DROP TABLE IF EXISTS `t_store`;
CREATE TABLE `t_store`
(
    `id`               int(10)      NOT NULL AUTO_INCREMENT,
    `name`             varchar(255) NOT NULL,
    `keeper_name`      varchar(255) NOT NULL,
    `phone`            varchar(255) NOT NULL,
    `longitude`        varchar(100) DEFAULT NULL,
    `latitude`         varchar(100) DEFAULT NULL,
    `province`         varchar(100) DEFAULT NULL,
    `city`             varchar(100) DEFAULT NULL,
    `region`           varchar(100) DEFAULT NULL,
    `address`          varchar(255) DEFAULT NULL,
    `address_index_id` int(10)      default null,
    `order_num`        int(10)      default null,
    `login_name`       varchar(255) DEFAULT NULL,
    `password`         varchar(255) DEFAULT NULL,
    `wx_open_id`       varchar(255) DEFAULT NULL,
    `create_time`      datetime     NOT NULL,
    `update_time`      datetime     NOT NULL,
    `deleted`          bit(1)       NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8;

desc t_store;

alter table t_store
    add column address_index_id int(10) default null after address;
alter table t_store
    add column order_num int(10) default null after address_index_id;
alter table t_store
    modify longitude double default null;
alter table t_store
    modify latitude double default null;