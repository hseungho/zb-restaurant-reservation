DROP TABLE IF EXISTS `menu`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `restaurant`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
     id varchar(40) not null primary key,
     user_id varchar(50) not null unique ,
     password varchar(255) not null ,
     nickname varchar(50) not null unique ,
     type varchar(20) not null ,
     logged_in_at timestamp,
     created_at timestamp not null ,
     updated_at timestamp,
     deleted_at timestamp
);

CREATE TABLE `restaurant` (
      id bigint not null auto_increment primary key ,
      name varchar(100) not null ,
      address varchar(255) not null ,
      x double default 0.0 ,
      y double default 0.0 ,
      description varchar(255),
      open_hour int,
      open_minute int,
      close_hour int,
      close_minute int,
      count_of_tables int default 1 not null ,
      max_per_reservation int ,
      contact_number varchar(15) not null ,
      rating double default 0.0 ,
      manager_id varchar(40) not null ,
      created_at timestamp not null ,
      updated_at timestamp ,
      delete_req_at timestamp ,
      deleted_at timestamp ,
      FOREIGN KEY (manager_id) REFERENCES `users` (id)
);

CREATE TABLE `menu` (
    id bigint not null auto_increment primary key ,
    name varchar(50) not null ,
    price bigint default 0 not null ,
    restaurant_id bigint not null ,
    FOREIGN KEY (restaurant_id) REFERENCES `restaurant` (id)
);

CREATE TABLE `review` (
    id bigint not null auto_increment primary key ,
    rating double default 1.0 not null ,
    content varchar(255) not null ,
    image_src varchar(255),
    author_id varchar(40) not null ,
    restaurant_id bigint not null ,
    FOREIGN KEY (author_id) REFERENCES `users` (id),
    FOREIGN KEY (restaurant_id) REFERENCES `restaurant` (id)
);
