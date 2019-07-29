create table t_user
(
    id                 int(10) auto_increment
        primary key,
    nickname           varchar(255) null,
    name               varchar(255) null,
    phone              varchar(100) null,
    wx_open_id         varchar(255) null,
    wx_avatar          varchar(255) null,
    role               int(2)       not null,
    position_longitude varchar(255) null,
    position_latitude  varchar(255) null,
    position           varchar(255) null,
    direct_income      int(6)       null,
    team_income        int(6)       null,
    withdrawals        int(6)       null,
    promo_code         varchar(255) null,
    team_header_level  int(2)       null,
    leader_id          int(10)      null,
    promoter_id        int(10)      null,
    wx_token_json      mediumtext   null,
    promoter_phone     varchar(100) null,
    promoter_wx_no     varchar(255) null,
    promoter_name      varchar(255) null,
    create_time        datetime     not null,
    update_time        datetime     not null,
    deleted            bit          not null
)
    charset = utf8mb4;


alter table t_user
    add column score int(6) null after team_income;
alter table t_user
    add column daily_login_times int(6) null after score;