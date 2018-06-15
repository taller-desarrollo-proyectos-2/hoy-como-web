
# --- !Ups

alter table commerce add suspended tinyint(1) default 0;

# --- !Downs

alter table commerce drop column suspended;