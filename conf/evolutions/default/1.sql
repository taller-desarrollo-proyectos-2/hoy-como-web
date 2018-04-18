# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table address (
  id                        bigint auto_increment not null,
  street                    varchar(255),
  number                    integer,
  additional_information    varchar(255),
  constraint pk_address primary key (id))
;

create table category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table commerce (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  company_id                bigint,
  business_name             varchar(255),
  license_id                bigint,
  address_id                bigint,
  email                     varchar(255),
  location_id               bigint,
  constraint pk_commerce primary key (id))
;

create table company (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_company primary key (id))
;

create table license (
  id                        bigint auto_increment not null,
  expiration_date           datetime,
  fee_to_pay                float,
  constraint pk_license primary key (id))
;

create table location (
  id                        bigint auto_increment not null,
  lat                       double,
  lng                       double,
  constraint pk_location primary key (id))
;

create table opening_time (
  id                        bigint auto_increment not null,
  commerce_id               bigint not null,
  day                       varchar(9),
  from_hour                 datetime,
  to_hour                   datetime,
  constraint ck_opening_time_day check (day in ('VIERNES','LUNES','DOMINGO','MARTES','SABADO','MIERCOLES','JUEVES')),
  constraint pk_opening_time primary key (id))
;

create table optional (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  price                     float,
  constraint pk_optional primary key (id))
;

create table phone (
  id                        bigint auto_increment not null,
  commerce_id               bigint not null,
  number                    varchar(255),
  constraint pk_phone primary key (id))
;

create table plate (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  commerce_id               bigint,
  price                     float,
  constraint pk_plate primary key (id))
;

create table request (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  status                    varchar(20),
  init_at                   datetime,
  constraint ck_request_status check (status in ('CANCELED_BY_USER','CANCELED_BY_COMMERCE','ON_PREPARATION','DELIVERED','ON_THE_WAY')),
  constraint pk_request primary key (id))
;

create table user (
  USER_TYPE                 varchar(31) not null,
  id                        bigint auto_increment not null,
  token                     varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  commerce_id               bigint,
  constraint pk_user primary key (id))
;


create table commerce_category (
  commerce_id                    bigint not null,
  category_id                    bigint not null,
  constraint pk_commerce_category primary key (commerce_id, category_id))
;

create table plate_category (
  plate_id                       bigint not null,
  category_id                    bigint not null,
  constraint pk_plate_category primary key (plate_id, category_id))
;

create table plate_optional (
  plate_id                       bigint not null,
  optional_id                    bigint not null,
  constraint pk_plate_optional primary key (plate_id, optional_id))
;

create table request_plate (
  request_id                     bigint not null,
  plate_id                       bigint not null,
  constraint pk_request_plate primary key (request_id, plate_id))
;

create table user_commerce (
  user_id                        bigint not null,
  commerce_id                    bigint not null,
  constraint pk_user_commerce primary key (user_id, commerce_id))
;
alter table commerce add constraint fk_commerce_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_commerce_company_1 on commerce (company_id);
alter table commerce add constraint fk_commerce_license_2 foreign key (license_id) references license (id) on delete restrict on update restrict;
create index ix_commerce_license_2 on commerce (license_id);
alter table commerce add constraint fk_commerce_address_3 foreign key (address_id) references address (id) on delete restrict on update restrict;
create index ix_commerce_address_3 on commerce (address_id);
alter table commerce add constraint fk_commerce_location_4 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_commerce_location_4 on commerce (location_id);
alter table opening_time add constraint fk_opening_time_commerce_5 foreign key (commerce_id) references commerce (id) on delete restrict on update restrict;
create index ix_opening_time_commerce_5 on opening_time (commerce_id);
alter table phone add constraint fk_phone_commerce_6 foreign key (commerce_id) references commerce (id) on delete restrict on update restrict;
create index ix_phone_commerce_6 on phone (commerce_id);
alter table plate add constraint fk_plate_commerce_7 foreign key (commerce_id) references commerce (id) on delete restrict on update restrict;
create index ix_plate_commerce_7 on plate (commerce_id);
alter table request add constraint fk_request_user_8 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_request_user_8 on request (user_id);
alter table user add constraint fk_user_commerce_9 foreign key (commerce_id) references commerce (id) on delete restrict on update restrict;
create index ix_user_commerce_9 on user (commerce_id);



alter table commerce_category add constraint fk_commerce_category_commerce_01 foreign key (commerce_id) references commerce (id) on delete restrict on update restrict;

alter table commerce_category add constraint fk_commerce_category_category_02 foreign key (category_id) references category (id) on delete restrict on update restrict;

alter table plate_category add constraint fk_plate_category_plate_01 foreign key (plate_id) references plate (id) on delete restrict on update restrict;

alter table plate_category add constraint fk_plate_category_category_02 foreign key (category_id) references category (id) on delete restrict on update restrict;

alter table plate_optional add constraint fk_plate_optional_plate_01 foreign key (plate_id) references plate (id) on delete restrict on update restrict;

alter table plate_optional add constraint fk_plate_optional_optional_02 foreign key (optional_id) references optional (id) on delete restrict on update restrict;

alter table request_plate add constraint fk_request_plate_request_01 foreign key (request_id) references request (id) on delete restrict on update restrict;

alter table request_plate add constraint fk_request_plate_plate_02 foreign key (plate_id) references plate (id) on delete restrict on update restrict;

alter table user_commerce add constraint fk_user_commerce_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_commerce add constraint fk_user_commerce_commerce_02 foreign key (commerce_id) references commerce (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table address;

drop table category;

drop table commerce;

drop table commerce_category;

drop table company;

drop table license;

drop table location;

drop table opening_time;

drop table optional;

drop table phone;

drop table plate;

drop table plate_category;

drop table plate_optional;

drop table request;

drop table request_plate;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

