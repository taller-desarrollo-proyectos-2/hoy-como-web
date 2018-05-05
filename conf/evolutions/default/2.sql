
# --- !Ups

alter table plate add deleted_at datetime;

alter table license modify fee_to_pay decimal(10,2);

update optional set price = 0;
alter table optional modify price decimal(10,2);

update plate set price = 0;
alter table plate modify price decimal(10,2);

alter table commerce add picture_file_name varchar(255);

alter table request add finished_at datetime;
alter table request add destination_id bigint;

alter table address add user_id bigint;

drop table request_plate;

create table single_request(
  id                     bigint auto_increment not null,
  plate_id               bigint,
  request_id             bigint,
  comment                varchar(255),
  quantity               int,
  constraint pk_single_request primary key (id))
;

create table single_request_optional(
  single_request_id              bigint not null,
  optional_id                    bigint not null,
  constraint pk_single_request_optional primary key (single_request_id, optional_id))
;

alter table single_request add constraint fk_single_request_plate_13 foreign key (plate_id) references plate (id) on delete restrict on update restrict;
create index ix_single_request_plate_1 on single_request (plate_id);

alter table single_request add constraint fk_single_request_request_14 foreign key (request_id) references request (id) on delete restrict on update restrict;
create index ix_request_destination_1 on single_request (request_id);

alter table request add constraint fk_request_destination_15 foreign key (destination_id) references address (id) on delete restrict on update restrict;
create index ix_request_destination_1 on request (destination_id);

alter table address add constraint fk_address_user_16 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_address_user_1 on address (user_id);

# --- !Downs

alter table single_request drop foreign key fk_single_request_plate_13;
drop index ix_single_request_plate_1 on single_request;

alter table single_request drop foreign key fk_single_request_request_14;
drop index ix_request_destination_1 on single_request;

alter table request drop foreign key fk_request_destination_15;
drop index ix_request_destination_1 on request;

alter table address drop foreign key fk_address_user_16;
drop index ix_address_user_1 on address;

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

alter table request drop column destination_id;
alter table address drop column user_id;