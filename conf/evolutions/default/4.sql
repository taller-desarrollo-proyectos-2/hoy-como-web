
# --- !Ups

create table promo (
  id                        bigint auto_increment not null,
  commerce_id               bigint not null,
  start_at                  datetime,
  end_at                    datetime,
  name                      varchar(255),
  descrption                varchar(255),
  active                    tinyint(1) default 0,
  price                     float,
  constraint pk_promo primary key (id))
;

create table bundle (
  id                        bigint auto_increment not null,
  promo_id                  bigint not null,
  plate_id                  bigint not null,
  optional_available        tinyint(1) default 0,
  constraint pk_bundle primary key (id))
;

alter table promo add constraint fk_promo_commerce_20 foreign key (commerce_id) references commerce (id) on delete restrict on update restrict;
create index ix_promo_commerce_1 on promo (commerce_id);

alter table bundle add constraint fk_bundle_promo_21 foreign key (promo_id) references promo (id) on delete restrict on update restrict;
create index ix_bundle_promo_2 on bundle (promo_id);

alter table bundle add constraint fk_bundle_plate_22 foreign key (plate_id) references plate (id) on delete restrict on update restrict;
create index ix_bundle_plate_3 on bundle (plate_id);

# --- !Downs

alter table promo drop foreign key fk_promo_commerce_20;
drop index ix_promo_commerce_1 on promo;

alter table bundle drop foreign key fk_bundle_promo_21;
drop index ix_bundle_promo_2 on bundle;

alter table bundle drop foreign key fk_bundle_plate_22;
drop index ix_bundle_plate_3 on bundle;

drop table promo;

drop table bundle;