
# --- !Ups

alter table plate add deleted_at datetime;

alter table license modify fee_to_pay decimal(10,2);

update optional set price = 0;
alter table optional modify price decimal(10,2);

update plate set price = 0;
alter table plate modify price decimal(10,2);

alter table commerce add picture_file_name string;

# --- !Downs

alter table plate drop column deleted_at;

alter table license modify fee_to_pay float;

alter table optional modify price float;

alter table plate modify price float;

alter table commerce drop column picture_file_name;