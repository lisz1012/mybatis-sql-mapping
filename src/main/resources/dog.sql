create table dog (id int(3) not null auto_increment, dname varchar(10) not null, dgender varchar(10) not null, dage int(3), user_id references (user), primary key (id));


insert into dog (dname, dgender, dage, user_id) values ('D1', 'male', 2, 1);
insert into dog (dname, dgender, dage, user_id) values ('D2', 'female', 1, 8);
insert into dog (dname, dgender, dage, user_id) values ('D3', 'female', 2, 8);