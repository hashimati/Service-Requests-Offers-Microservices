create table systemuser (
    id BIGINT not NULL auto_increment,
    username varchar(25) not null,
    password varchar(15) not null,
    constraint username unique (username),
    constraint id_num unique (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;