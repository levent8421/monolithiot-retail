drop table if exists t_daily_login;

create table t_daily_login
(
    id          int(10)  not null auto_increment primary key,
    user_id     int(10)  not null,
    login_date  date     not null,
    create_time datetime not null,
    update_time datetime not null,
    deleted     bit      not null
);

select dl.id          dl_id,
       dl.user_id     dl_user_id,
       dl.login_date  dl_login_date,
       dl.create_time dl_create_time,
       dl.update_time dl_update_time,
       dl.deleted     dl_deleted
from t_daily_login as dl
where dl.deleted = false;