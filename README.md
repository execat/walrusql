# walrusql

Supported commands:

1. `use information_schema;`
The prompt should change to "Walrusql $information_schema >".

2. `create schema zoo;`
This should create a new schema named "zoo".

3. `show schemas;`
This should show all the available schemas. In this case:

```
information_schema
zoo
```

4. `show tables;`
Shows tables in the current schema.

```
SCHEMATA
TABLES
COLUMNS
```

5. `create table zoo (animal_id int primary key, name varchar(20), sector short int);`
Creates table.

6. `
insert into zoo values (57, 'giraffe', 9);
insert into zoo values (12, 'elephant', 5);
insert into zoo values (23, 'lion', 4);
insert into zoo values (17, 'hippo', 5);
`
Inserts values into the table and creates index.

7. `select * from zoo;`

Shows all values from zoo;

`
# For integer like values
select * from zoo where animal_id = 17;
select * from zoo where animal_id > 17;
select * from zoo where animal_id < 17;

# For text
select * from zoo where name = 'Lion';
select * from zoo where name != 'Lion';
`

Supports `=`, `>`, `<`, `!=`.
