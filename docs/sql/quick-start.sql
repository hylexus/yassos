# used by plugin : yassos-user-loader-jdbc
create database if not exists yassos_server default charset 'utf8';
use yassos_server;

create table yassos_user
(
    id                 bigint primary key auto_increment,
    name               varchar(128) not null comment 'username',
    password           varchar(256) not null comment 'encoded password',
    locked             tinyint      not null default 0 comment '0:unlocked;1:locked',
    credential_expired tinyint      not null default 0 comment '0:expired;1:not expired',
    avatarUrl          varchar(256) not null default ''
) comment 'userDetails for yassos';

INSERT INTO yassos_user (name, password, locked, credential_expired, avatarUrl)
VALUES ('yassos', 'yassos', 0, 0, 'https://avatars1.githubusercontent.com/u/13914832'),
       ('hylexus', 'yassos', 0, 0, 'https://avatars1.githubusercontent.com/u/13914832'),
       ('dfEric', 'yassos', 0, 0, 'https://avatars0.githubusercontent.com/u/12884239')
;
