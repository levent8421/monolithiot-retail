drop table if exists t_address_index;
create table t_address_index
(
    id            int(10)      not null auto_increment primary key,
    province      varchar(255) not null,
    city          varchar(244) not null,
    city_code     varchar(100) not null,
    district      varchar(255) null,
    district_code varchar(100) null,
    create_time   datetime     not null,
    update_time   datetime     not null,
    deleted       bit          not null
);

    select ai.id            ai_id,
           ai.province      ai_province,
           ai.city          ai_city,
           ai.city_code     ai_city_code,
           ai.district      ai_district,
           ai.district_code ai_district_code,
           ai.create_time   ai_create_time,
           ai.update_time   ai_update_time,
           ai.deleted       ai_deleted
    from t_address_index as ai
    where ai.deleted = false