CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company (id, name) values (1, 'First Company');
insert into company (id, name) values (2, 'Second Company');
insert into company (id, name) values (3, 'Third Company');
insert into company (id, name) values (4, 'Fourth Company');
insert into company (id, name) values (5, 'Fifth Company');
insert into company (id, name) values (6, 'Sixth Company');
insert into company (id, name) values (7, 'Seventh Company');
insert into company (id, name) values (8, 'Eighth Company');

insert into person(id, name, company_id) values (1, 'Michael Ford', 1);
insert into person(id, name, company_id) values (2, 'John Smith', 2);
insert into person(id, name, company_id) values (3, 'Jack Daniels', 3);
insert into person(id, name, company_id) values (4, 'Richard Benson', 4);
insert into person(id, name, company_id) values (5, 'Jessica Nichols', 5);
insert into person(id, name, company_id) values (6, 'Emily Dunt', 6);
insert into person(id, name, company_id) values (7, 'Amy Scott', 7);
insert into person(id, name, company_id) values (8, 'Viola Hill', 8);
insert into person(id, name, company_id) values (9, 'Mary Duncan', 3);
insert into person(id, name, company_id) values (10, 'Katy Pine', 4);
insert into person(id, name, company_id) values (11, 'Abigail Spencer', 4);
insert into person(id, name, company_id) values (12, 'William George ', 3);

/*   1   */
/* - имена всех person, которые не состоят в компании с id = 5 + название компании для каждого человека. */
SELECT p.name AS person_name, c.name AS company_name FROM person p
JOIN company c ON p.company_id = c.id
WHERE c.id <> 5;


/*   2   */
/* выбрать название компании с максимальным количеством человек + количество человек в этой компании. */
WITH emps AS (
SELECT company.name, COUNT(person) AS c 
	FROM company JOIN person ON (company.id = person.company_id) GROUP BY company.name
)
SELECT * FROM emps WHERE emps.c = (SELECT MAX(emps.c) FROM emps);