--creating the table "Authors"
create table "Authors"
(
    id int
        constraint AUTHORS_PK
            primary key,
    name varchar(64) not null
        check (length(name) > 0),
    comment varchar(255)
);

--tests--
insert into "Authors" (id, name, comment) values (4, '', null);
insert into "Authors" (id, name, comment) values (4, null, null);
--end--

--filling the table "Authors" with initial data
insert into "Authors"
(id, name, comment)
values
(1, 'Arnold Grey', null),
(2, 'Tom Hawkins', 'new author'),
(3, 'Jim Beam', null);

--creating the table "Documents"
create table "Documents"
(
    id int
        constraint DOCUMENTS_PK
            primary key ,
    title varchar(64) not null
        check (length(title) > 0),
    content varchar(1024),
    created date not null with default current date,
    author int
        constraint AUTHORS_FK
            references "Authors"(id)
);

--tests--
insert into "Documents"
(id, title, content, author) values (5, '', 'content', 1);
insert into "Documents"
(id, title, content, author) values (5, null, 'content', 1);
--end--


--filling the table "Documents" with initial data
insert into "Documents"
(id, title, content, author)
values
(1, 'Project plan', 'First content', 1),
(2, 'First report', 'Second content', 2),
(3, 'Test result', 'Third content', 2),
(4, 'Second report', 'Report content', 3);


--updating data in the table "Authors": setting field "comment" values to "No data" if it was blank
update "Authors"
set comment = 'No data'
where comment is null;


--list of the documents, created by Tom Hawkins
select D.id, D.title, D.content, D.created
from "Documents" D inner join "Authors" A on D.author = A.id
where A.name = 'Tom Hawkins';


--list of all the documents, which title contains the word "report"
select D.id,D.title,A.name
from "Documents" D inner join "Authors" A on D.author = A.id
where D.title like '%report%'