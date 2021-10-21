

create table gift_certificate
(
    id               bigint NOT NULL AUTO_INCREMENT primary key,
    name   	         varchar(255),
    description      varchar(7000),
    price            double,
    duration         int,
    create_date      timestamp DEFAULT CURRENT_TIMESTAMP,
    last_update_date timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table tag
(
    id            bigint NOT NULL AUTO_INCREMENT primary key ,
    name   	      varchar(255)

);

create table tag_gift_certificate
(
    id                        bigint NOT NULL AUTO_INCREMENT primary key,
    tag_id		              bigint,
    gift_certificate_id		  bigint

);

alter table tag_gift_certificate
    add foreign key (tag_id) references tag (id) ON DELETE CASCADE;

alter table tag_gift_certificate
add foreign key(gift_certificate_id) references gift_certificate(id) ON DELETE CASCADE