---- tables ----

create table users(
    username varchar(50) not null primary key,
    password varchar(500) not null,
    enabled boolean not null
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username, authority);

---- data ----

insert into users (username, password, enabled) VALUES
('admin', 'to_be_encoded', true),
('user', 'to_be_encoded', true);

insert into authorities (username, authority) VALUES
('admin', 'admin'),
('user', 'user');