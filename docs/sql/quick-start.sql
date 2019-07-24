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
VALUES ('admin', '1234561', 0, 0, 'https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=4665601df0039245b5b8e95de6fdcfa7/503d269759ee3d6dd050111b4f166d224e4ade48.jpg');
