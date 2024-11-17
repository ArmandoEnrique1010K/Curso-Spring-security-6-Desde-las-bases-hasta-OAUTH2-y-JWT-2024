# SECCION 4

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
                                       ('super_user@debuggeandoieas.com', '$2a$12$RmBy7wboh01WnZbOGJLQx.zG0STbjW7bS7mx6vikS1IuWWpq5dooO', 'admin'),
                                        ('basic_user@debuggeandoieas.com', '$2a$12$6p/tw.f7u3LtMSAXe03Kse9I6zzPtb72gpuFN5r/jl3tzEcsENbza', 'user');

# Las contraseñas estan encriptadas, ambas contraseñas son "to_be_encoded"
# https://bcrypt-generator.com/

# 3. Accede a un endpoint de la aplicación en el navegador e inicia sesión con el usuario registrado en la tabla customers