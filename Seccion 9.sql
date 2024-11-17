# SECCION 8

# 1. Crea la base de datos "db_security_bank" de manera manual en PostgreSQL

# 2. Ejecuta el siguiente Script en PostgreSQL

create table customers(
                          id    bigserial primary key,
                          email varchar(70)  not null,
                          pwd   varchar(500) not null
);

create table roles(
                      id  bigserial primary key,
                      role_name varchar(50),
                      description varchar(100),
                      id_customer bigint,
                      constraint fk_customer foreign key(id_customer) references customers(id)
);

create table partners (
                          id bigserial primary key,
                          client_id varchar(256),
                          client_name varchar(256),
                          client_secret varchar(256),
                          scopes varchar(256),
                          grant_types varchar(256),
                          authentication_methods varchar(256),
                          redirect_uri varchar(256),
                          redirect_uri_logout varchar(256)
);


insert into customers (email, pwd) values
                                       ('account@debuggeandoieas.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO'),
                                       ('cards@debuggeandoieas.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO'),
                                       ('loans@debuggeandoieas.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO'),
                                       ('balance@debuggeandoieas.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO');

insert into roles(role_name, description, id_customer) values
                                                           ('ROLE_ADMIN', 'cant view account endpoint', 1),
                                                           ('ROLE_ADMIN', 'cant view cards endpoint', 2),
                                                           ('ROLE_USER', 'cant view loans endpoint', 3),
                                                           ('ROLE_USER', 'cant view balance endpoint', 4);
insert into partners(
    client_id,
    client_name,
    client_secret,
    scopes,
    grant_types,
    authentication_methods,
    redirect_uri,
    redirect_uri_logout
)
values ('debuggeandoideas',
            'debuggeando ideas',
            '$2a$10$9m4JHagydJWZb5zjc3Rd9O9yKuP5xSJsDNQmI8tz2EMbhYh7vKNkq',
            'read,write',
            'authorization_code,refresh_token',
            'client_secret_basic,client_secret_jwt',
            'https://oauthdebugger.com/debug',
            'https://springone.io/authorized')


# Las contraseñas no estan encriptadas

# 3. Abre el navegador y realiza el mismo procedimiento para autenticar, las contraseñas son: "to_be_encoded"