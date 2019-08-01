alter table t_user
    add column score int(6) null after team_income;

alter table t_user
    add column daily_login_times int(6) null after score;

desc t_user;
