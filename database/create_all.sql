/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/5/18 星期六 10:44:41                       */
/*==============================================================*/

drop table if exists t_order;

drop table if exists t_cash_application;

drop table if exists t_general_time_slot;

drop table if exists t_general_time_range;

drop table if exists t_special_time_slot;

drop table if exists t_special_time_range;

drop table if exists t_commodity;

drop table if exists t_store;

drop table if exists t_user;

/*==============================================================*/
/* Table: t_cash_application                                    */
/*==============================================================*/
create table t_cash_application
(
    id          int(10)  not null auto_increment,
    user_id     int(10)  not null,
    amount      int(6)   not null,
    status      int(2)   not null,
    create_time datetime not null,
    update_time datetime not null,
    deleted     bit      not null,
    primary key (id)
);

/*==============================================================*/
/* Table: t_commodity                                           */
/*==============================================================*/
create table t_commodity
(
    id                     int(10)      not null auto_increment,
    store_id               int(10)      not null,
    buy_limit              int(4),
    name                   varchar(255) not null,
    description            text,
    images                 varchar(255),
    original_price         int(6),
    current_price          int(6),
    stock_count            int(4),
    sale_count             int(4),
    commission_1           int(6)       not null,
    commission_2           int(6)       not null,
    commission_3           int(6)       not null,
    need_appointment       bit          not null,
    buy_end_time           datetime,
    appointment_start_time datetime,
    appointment_end_time   datetime,
    status                 int(2)       not null,
    create_time            datetime     not null,
    update_time            datetime     not null,
    deleted                bit          not null,
    primary key (id)
);

/*==============================================================*/
/* Table: t_general_time_range                                  */
/*==============================================================*/
create table t_general_time_range
(
    id           int(10)  not null auto_increment,
    commodity_id int(10)  not null,
    day_of_week  int(1)   not null,
    create_time  datetime not null,
    update_time  datetime not null,
    deleted      bit      not null,
    primary key (id)
);

/*==============================================================*/
/* Table: t_general_time_slot                                   */
/*==============================================================*/
create table t_general_time_slot
(
    id                int(10)  not null auto_increment,
    time_range_id     int(10)  not null,
    start_time        time     not null,
    end_time          time     not null,
    count_upper_limit int(5)   not null,
    booked_count      int(5)   not null,
    create_time       datetime not null,
    update_time       datetime not null,
    deleted           bit      not null,
    primary key (id)
);

/*==============================================================*/
/* Table: t_order                                               */
/*==============================================================*/
create table t_order
(
    id                    int(10)      not null auto_increment,
    user_id               int(10)      not null,
    commodity_id          int(10)      not null,
    quantity              int(3)       not null,
    store_id              int(10)      not null,
    amount                int(10)      not null,
    username              varchar(255) not null,
    phone                 varchar(100) not null,
    sn                    varchar(255) not null,
    trade_no              varchar(255) not null,
    commodity_name        varchar(255) not null,
    commodity_description varchar(255),
    status                int(2)       not null,
    appointment_time      datetime,
    create_time           datetime     not null,
    update_time           datetime     not null,
    deleted               bit          not null,
    primary key (id)
);
alter table t_order
    add column promoter_2_id int(10) null after promoter_id;
alter table t_order
    add column promoter_3_id int(10) null after promoter_2_id;
/*==============================================================*/
/* Table: t_special_time_range                                  */
/*==============================================================*/
create table t_special_time_range
(
    id           int(10)  not null auto_increment,
    commodity_id int(10)  not null,
    action_date  date     not null,
    create_time  datetime not null,
    update_time  datetime not null,
    deleted      bit      not null,
    primary key (id)
);

/*==============================================================*/
/* Table: t_special_time_slot                                   */
/*==============================================================*/
create table t_special_time_slot
(
    id                int(10)  not null auto_increment,
    time_range_id     int(10)  not null,
    start_time        time     not null,
    end_time          time     not null,
    count_upper_limit int(5)   not null,
    booked_count      int(5)   not null,
    create_time       datetime not null,
    update_time       datetime not null,
    deleted           bit      not null,
    primary key (id)
);

/*==============================================================*/
/* Table: t_store                                               */
/*==============================================================*/
create table t_store
(
    id          int(10)      not null auto_increment,
    name        varchar(255) not null,
    keeper_name varchar(255) not null,
    phone       varchar(255) not null,
    longitude   varchar(100),
    latitude    varchar(100),
    province    varchar(100),
    city        varchar(100),
    region      varchar(100),
    address     varchar(255),
    login_name  varchar(255),
    password    varchar(255),
    create_time datetime     not null,
    update_time datetime     not null,
    deleted     bit          not null,
    primary key (id)
);

/*==============================================================*/
/* Table: t_user                                                */
/*==============================================================*/
create table t_user
(
    id                 int(10)      not null auto_increment,
    nickname           varchar(255),
    name               varchar(255),
    phone              varchar(100),
    wx_open_id         varchar(255),
    wx_avatar          varchar(255),
    role               int(2)       not null,
    position_longitude varchar(255),
    position_latitude  varchar(255),
    position           varchar(255),
    direct_income      int(6),
    team_income        int(6),
    withdrawals        int(6),
    promo_code         varchar(255),
    team_header_level  int(2),
    leader_id          int(10),
    wx_token_json      varchar(255),
    promoter_phone     varchar(100) null,
    promoter_wx_no     varchar(255) null,
    promoter_name      varchar(255) null,
    create_time        datetime     not null,
    update_time        datetime     not null,
    deleted            bit          not null,
    primary key (id)
);

alter table t_user
    add column promoter_phone varchar(100) null after wx_token_json;
alter table t_user
    add column promoter_wx_no varchar(255) null after promoter_phone;
alter table t_user
    add column promoter_name varchar(255) null after promoter_wx_no;

alter table t_user
    add column promoter_id int(10) null after leader_id;
alter table t_cash_application
    add constraint FK_pk_cash_application_user foreign key (user_id)
        references t_user (id) on delete restrict on update restrict;

alter table t_commodity
    add constraint FK_pk_commodity_and_store foreign key (store_id)
        references t_store (id) on delete restrict on update restrict;

alter table t_general_time_range
    add constraint FK_fk_general_time_range_and_commodity foreign key (commodity_id)
        references t_commodity (id) on delete restrict on update restrict;

alter table t_general_time_slot
    add constraint FK_Reference_5 foreign key (time_range_id)
        references t_general_time_range (id) on delete restrict on update restrict;

alter table t_order
    add constraint FK_pk_order_commodity foreign key (commodity_id)
        references t_commodity (id) on delete restrict on update restrict;

alter table t_order
    add constraint FK_pk_order_user foreign key (user_id)
        references t_user (id) on delete restrict on update restrict;

alter table t_special_time_range
    add constraint FK_fk_special_time_range_and_commodity foreign key (commodity_id)
        references t_commodity (id) on delete restrict on update restrict;

alter table t_special_time_slot
    add constraint FK_Reference_4 foreign key (time_range_id)
        references t_special_time_range (id) on delete restrict on update restrict;


drop table if exists t_amint;
create table t_admin
(
    id          int(10)      not null auto_increment primary key,
    name        varchar(255) not null,
    password    varchar(255) not null,
    create_time datetime     not null,
    update_time datetime     not null,
    deleted     bit          not null
);

