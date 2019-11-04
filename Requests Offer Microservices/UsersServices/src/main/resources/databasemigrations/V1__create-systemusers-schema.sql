create table users (
    id BIGINT not NULL auto_increment,
    username varchar(25) not null,
    password varchar(15) not null,
    roles varchar(200) not null, 
    constraint username unique (username),
    constraint id_num unique (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE INDEX usersIndex on users(username); 
