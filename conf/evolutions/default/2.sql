
# --- !Ups

alter table plate add deleted_at datetime;

alter table license modify fee_to_pay decimal(10,2);

update optional set price = 0;
alter table optional modify price decimal(10,2);

update plate set price = 0;
alter table plate modify price decimal(10,2);

alter table commerce add picture_file_name varchar(255);

alter table request add finished_at datetime;

drop table request_plate;

create table single_request(
  id                     bigint auto_increment not null,
  plate_id               bigint not null,
  request_id             bigint not null,
  comment                varchar(255),
  quantity               int,
  constraint pk_single_request primary key (id))
;

create table single_request_optional(
  single_request_id              bigint not null,
  optional_id                    bigint not null,
  constraint pk_single_request_optional primary key (single_request_id, optional_id))
;

# --- !Downs

alter table plate drop column deleted_at;

alter table license modify fee_to_pay float;

alter table optional modify price float;

alter table plate modify price float;

alter table commerce drop column picture_file_name;

alter table request drop column finished_at;

create table request_plate (
  request_id                     bigint not null,
  plate_id                       bigint not null,
  constraint pk_request_plate primary key (request_id, plate_id))
;

drop table single_request;

drop table single_request_optional;