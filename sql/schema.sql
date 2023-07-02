DROP TABLE IF EXISTS `menu`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `restaurant`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
     id varchar(40) not null primary key,
     user_id varchar(50) not null unique ,
     password varchar(255) not null ,
     nickname varchar(50) not null unique ,
     type varchar(20) not null ,
     logged_in_at datetime,
     created_at datetime not null ,
     updated_at datetime,
     deleted_at datetime
);

CREATE TABLE `restaurant` (
      id bigint not null auto_increment primary key ,
      name varchar(100) not null ,
      address varchar(255) not null ,
      x double default 0.0 not null ,
      y double default 0.0 not null ,
      description varchar(255),
      open time not null ,
      close time not null ,
      count_of_tables int default 1 not null ,
      max_per_reservation int ,
      contact_number varchar(15) not null ,
      rating double default 0 ,
      user_id varchar(40) ,
      created_at datetime not null ,
      updated_at datetime ,
      delete_req_at datetime ,
      deleted_at datetime ,
      FOREIGN KEY (user_id) REFERENCES `users` (id)
);

CREATE TABLE `menu` (
    id bigint not null auto_increment primary key ,
    name varchar(50) not null ,
    price bigint default 0 not null ,
    restaurant_id bigint ,
    created_at datetime not null ,
    updated_at datetime ,
    FOREIGN KEY (restaurant_id) REFERENCES `restaurant` (id)
);


CREATE TABLE `reservation` (
    id bigint not null auto_increment primary key ,
    number varchar(20) not null unique ,
    num_of_person int not null ,
    client_contact_number varchar(11) not null ,
    reserved_at datetime,
    canceled_at datetime,
    visited_at datetime,
    approved_at datetime,
    refused_at datetime,
    status varchar(50) not null ,
    user_id varchar(40) not null ,
    restaurant_id bigint not null ,
    created_at datetime not null ,
    updated_at datetime ,
    FOREIGN KEY (user_id) REFERENCES `users` (id),
    FOREIGN KEY (restaurant_id) REFERENCES `restaurant` (id)
);

CREATE TABLE `review` (
      id bigint not null auto_increment primary key ,
      rating double default 1.0 not null ,
      content varchar(255) not null ,
      image_src varchar(255),
      user_id varchar(40) ,
      restaurant_id bigint ,
      reservation_id bigint ,
      created_at datetime not null ,
      updated_at datetime ,
      FOREIGN KEY (user_id) REFERENCES `users` (id),
      FOREIGN KEY (restaurant_id) REFERENCES `restaurant` (id),
      FOREIGN KEY (reservation_id) REFERENCES `reservation` (id)
);
