create sequence hibernate_sequence start with 1 increment by 1
create table user (id bigint not null, active boolean not null, address varchar(255), created bigint not null, created_at timestamp not null, deleted bigint, deleted_at timestamp, deleted_flag boolean not null, email varchar(255), email_token varchar(255), last_login timestamp, name varchar(255), next_login_change_pwd boolean not null, password varchar(255), password_expired boolean not null, phone varchar(255), settlement_id bigint, temp_password varchar(255), temp_password_expired timestamp, updated bigint, updated_at timestamp, user_type varchar(255), username varchar(255) not null, primary key (id))
create table user_roles (id bigint not null, roles integer, user_id bigint, primary key (id))
alter table user add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email)
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username)
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user
