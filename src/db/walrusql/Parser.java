package db.walrusql;

import java.util.Scanner;

/**
 * Created by atm on 4/10/16.
 */
public class Parser {
    static String prompt;
    Scanner scanner;
    String currentSchema, input;
    DatabaseService service;

    public Parser() {
        prompt = "Walrusql > ";
        scanner = new Scanner(System.in).useDelimiter(";");
        currentSchema = "information_schema";
        input = "";
        service = new DatabaseService();
    }

    /**
     * Implements the following commands
     * Schema commands
     * 1. SHOW SCHEMAS;
     * 2. USE <SCHEMA NAME>;
     * 3. CREATE SCHEMA <SCHEMA NAME>
     *
     * Table commands
     * 4. SHOW TABLES;
     * 5. SELECT * FROM <TABLE NAME>
     * 6. CREATE TABLE <TABLE NAME> (<PROPERTIES>)
     * 7. INSERT INTO <TABLE NAME> VALUES (<VALUES>)
     * 8. DROP TABLE
     *
     * Misc commands
     * *. Exit
     * *. Not found
     */
    public void parse() {
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim()
                    .replace(" +", " ")
                    .replace(";", "").toLowerCase();
            boolean response = true;
            System.out.println("INPUT IS: " + input);

            /*
                Schema commands
             */
            // SHOW SCHEMAS;
            if (input.matches("show schemas")) {
                response = service.showSchemas();
            }

            // USE <SCHEMA NAME>
            else if (input.matches("use .*")) {
                response = service.useSchema();
            }

            // CREATE SCHEMA <SCHEMA NAME>
            else if (input.matches("create schema .*")) {
                response = service.createSchema();
            }

            /*
                Tables
             */
            // SHOW TABLES;
            else if (input.matches("show tables")) {
                response = service.showTables();
            }

            // SELECT * FROM <TABLE NAME>
            else if (input.matches("select \\* from .*")) {
                response = service.select();
            }

            // CREATE TABLE <TABLE NAME> (<PROPERTIES>)
            else if (input.matches("create table .*")) {
                response = service.createTable();
            }


            // INSERT INTO <TABLE NAME> VALUES (<VALUES>)
            else if (input.matches("insert into .*")) {
                response = service.insert();
            }

            // DROP TABLE
            else if (input.matches("drop table")) {
                response = service.dropTable();
            }

            /*
            // CREATE SCHEMA <SCHEMA NAME>
            else if (input.matches("")) {
                response = service.exec();
            }
            */

            // EXIT
            else if (input.matches("exit")) {
                return;
            }

            // Not found
            else {
                System.out.println("Invalid command. Try again.");
            }

        } while(true);
    }
}
