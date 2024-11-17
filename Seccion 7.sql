# SECCION 7

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

# 3. Accede a un endpoint de la aplicación en el navegador e inicia sesión con el usuario registrado en la tabla customers

# 4. Inicia sesion en la aplicacion, ve al endpoint /login

# 5. Aparecera una pagina web de error 500. Introduce el siguiente Script de JS en la consola de chrome

/*
fetch('http://localhost:8080/loans', {
    method: 'GET',
    headers: {
        'api_key': 'myKey'
    }
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.log(error));
*/

6. Pulsa enter y se imprimira en la consola el objeto que retorna el endpoint, debes asegurarte que el usuario que ha iniciado sesion en el navegador, tenga el rol adecuado para que pueda acceder al endpoint