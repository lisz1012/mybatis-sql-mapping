create table user (id int(3) not null auto_increment, name varchar(10) not null, job varchar(10) not null, birth_date datetime, profile longblob, email varchar(20) not null, score double, created_at datetime, modified_at datetime, version bigint not null, primary key (id));

insert into user (name, job, birth_date, email, score, created_at, modified_at, version) values ('lisz', 'SDE', '1984-10-12', 'lisz@gmail.com', 100.00, '2020-12-13', '2020-12-13', 0);

insert into user (name, job, birth_date, email, score, created_at, modified_at, version) values ('guisun', 'SDE', '1990-11-11', 'guisun@gmail.com', 100.00, '2020-12-13', '2020-12-13', 0);

