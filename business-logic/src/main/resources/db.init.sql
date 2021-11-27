create table gift_certificate
(
    id               bigint NOT NULL AUTO_INCREMENT primary key,
    name             varchar(255),
    description      varchar(7000),
    price            double,
    duration         int,
    create_date      timestamp DEFAULT CURRENT_TIMESTAMP,
    last_update_date timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table tag
(
    id   bigint NOT NULL AUTO_INCREMENT primary key,
    name varchar(255)

);

create table tag_gift_certificate
(
    id                  bigint NOT NULL AUTO_INCREMENT primary key,
    tag_id              bigint,
    gift_certificate_id bigint

);

alter table tag_gift_certificate
    add foreign key (tag_id) references tag (id) ON DELETE CASCADE;

alter table tag_gift_certificate
    add foreign key (gift_certificate_id) references gift_certificate (id) ON DELETE CASCADE;



create table `user`
(
    id       bigint       NOT NULL AUTO_INCREMENT primary key,
    username varchar(255),
    password varchar(255) NOT NULL,
    role     enum ('ADMIN','USER'),
    CONSTRAINT unique_name UNIQUE (username)
);

create table `order`
(
    id          bigint NOT NULL AUTO_INCREMENT primary key,
    user_id     bigint,
    update_date timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cost        double,
    foreign key (user_id) references `user` (id) ON DELETE CASCADE
);

create table order_gift_certificate
(
    id                        bigint NOT NULL AUTO_INCREMENT primary key,
    json_certificate_snapshot varchar(10000),
    order_id                  bigint,
    gift_certificate_id       bigint,
    foreign key (order_id) references `order` (id) ON DELETE CASCADE,
    foreign key (gift_certificate_id) references gift_certificate (id) ON DELETE CASCADE

);