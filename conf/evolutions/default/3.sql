
# --- !Ups

create table qualification (
  id                        bigint auto_increment not null,
  request_id                bigint,
  user_id                   bigint,
  comment                   varchar(255),
  response                  varchar(255),
  score                     int,
  constraint pk_qualification primary key (id))
;

alter table qualification add constraint fk_qualification_request_18 foreign key (request_id) references request (id) on delete restrict on update restrict;
create index ix_qualification_request_1 on qualification (request_id);

alter table qualification add constraint fk_qualification_user_19 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_qualification_user_1 on qualification (user_id);

# --- !Downs

alter table qualification drop foreign key fk_qualification_request_18;
drop index ix_qualification_request_1 on qualification;

alter table qualification drop foreign key fk_qualification_user_19;
drop index ix_qualification_user_1 on qualification;

drop table qualification;