--- SECCION 2

# Crea la base de datos "db_security_bank" de manera manual en PostgreSQL

# Ejecuta el siguiente Script en PostgreSQL

# Crea la tabla customers con los campos especificados
create table customers(
                          id bigserial primary key,
                          email varchar(50) not null,
                          pwd varchar(500) not null,
                          rol varchar(20) not null);

# Inserta los datos en la tabla customers
insert into customers (email, pwd, rol) VALUES
                                       ('super_user@debuggeandoieas.com', 'to_be_encoded', 'admin'),
                                        ('basic_user@debuggeandoieas.com', 'to_be_encoded', 'user');


# Accede a un endpoint de la aplicación en el navegador e inicia sesión con el usuario registrado en la tabla customers



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