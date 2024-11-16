# SECCION 2

# 1. Crea la base de datos "db_security_bank" de manera manual en PostgreSQL

# 2. Ejecuta el siguiente Script en PostgreSQL

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


# 3. Accede a un endpoint de la aplicación en el navegador e inicia sesión con el usuario registrado en la tabla customers