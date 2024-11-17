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



insert into customers (email, pwd) values
                                       ('account@debuggeandoieas.com', 'to_be_encoded'),
                                       ('cards@debuggeandoieas.com', 'to_be_encoded'),
                                       ('loans@debuggeandoieas.com', 'to_be_encoded'),
                                       ('balance@debuggeandoieas.com', 'to_be_encoded');

insert into roles(role_name, description, id_customer) values
                                                           ('ROLE_ADMIN', 'cant view account endpoint', 1),
                                                           ('ROLE_ADMIN', 'cant view cards endpoint', 2),
                                                           ('ROLE_USER', 'cant view loans endpoint', 3),
                                                           ('ROLE_USER', 'cant view balance endpoint', 4);


# Las contraseñas no estan encriptadas

# 3. Abre postman y accede al endpoint "localhost:8080/balance" sin haberse autenticado, debe mostrar un error 401 y un mensaje "Unauthorized"

# 4. Realiza una petición de tipo POST al endpoint "localhost:8080/authenticate" y envia un JSON que contiene el username y password con el email y contraseña de un usuario que se encuentra en la base de datos.

# 5. Copia el token generado. Realiza una petición de tipo GET a la URL "localhost:8080/cards" y "localhost:8080/balance", para pasar el token ve a la pestaña auth y selecciona Bearer Token. Pega el token y envia la petición.

# 6. Mostrara un mensaje con el objeto devuelto en el controlador o un error 403 Forbidden si el usuario no tiene los permisos.