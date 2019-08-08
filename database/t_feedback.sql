drop table if exists t_feedback;

create table t_feedback
(
    id          int(10)      not null auto_increment primary key,
    user_id     int(10)      null,
    username    varchar(255) not null,
    phone       varchar(255) null,
    content     text         not null,
    create_time datetime     not null,
    update_time datetime     not null,
    deleted     bit          not null
);


select fb.id          fb_id,
       fb.user_id     fb_user_id,
       fb.username    fb_username,
       fb.phone       fb_phone,
       fb.content     fb_content,
       fb.create_time fb_create_time,
       fb.update_time fb_update_time,
       fb.deleted     fb_deleted
from t_feedback as fb
where fb.deleted = false;