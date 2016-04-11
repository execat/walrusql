package db.walrusql;

import java.util.Scanner;

/**
 * Created by atm on 4/10/16.
 */
public class Parser {
    static String prompt;
    Scanner input;
    String currentSchema, inputString;
    DatabaseService service;

    public Parser() {
        prompt = "Walrusql > ";
        input = new Scanner(System.in).useDelimiter(";");
        currentSchema = "information_schema";
        inputString = "";
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
            inputString = input.nextLine().trim()
                    .replace(" +", " ")
                    .replace(";", "").toLowerCase();
            boolean response = true;
            System.out.println("INPUT IS: " + inputString);

            /*
                Schema commands
             */
            // SHOW SCHEMAS;
            if (inputString.matches("show schemas")) {
                response = service.showSchemas();
            }

            // USE <SCHEMA NAME>
            else if (inputString.matches("use .*")) {
                response = service.useSchema();
            }

            // CREATE SCHEMA <SCHEMA NAME>
            else if (inputString.matches("create schema .*")) {
                response = service.createSchema();
            }

            /*
                Tables
             */
            // SHOW TABLES;
            else if (inputString.matches("show tables")) {
                response = service.showTables();
            }

            // SELECT * FROM <TABLE NAME>
            else if (inputString.matches("select \\* from .*")) {
                response = service.select();
            }

            // CREATE TABLE <TABLE NAME> (<PROPERTIES>)
            else if (inputString.matches("create table .*")) {
                response = service.createTable();
            }


            // INSERT INTO <TABLE NAME> VALUES (<VALUES>)
            else if (inputString.matches("insert into .*")) {
                response = service.insert();
            }

            // DROP TABLE
            else if (inputString.matches("drop table")) {
                response = service.dropTable();
            }

            /*
            // CREATE SCHEMA <SCHEMA NAME>
            else if (inputString.matches("")) {
                response = service.exec();
            }
            */

            // EXIT
            else if (inputString.matches("exit")) {
                return;
            }

            // Not found
            else {
                System.out.println("Invalid command. Try again.");
            }

        } while(true);
    }
}
