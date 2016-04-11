package db.walrusql;

import java.util.Scanner;
import java.util.StringTokenizer;

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
            boolean response = false;
            System.out.println("INPUT IS: " + input);

            /*
                Schema commands
             */
            // SHOW SCHEMAS;
            if (input.matches("show schemas")) {
                // Needs no parameters
                response = service.showSchemas();
            }

            // USE <SCHEMA NAME>
            else if (input.matches("use .*")) {
                String[] tokens = input.split(" ");
                // Expected value: tokens = ["use", "<SCHEMA NAME>"]
                response = service.useSchema(tokens[1]);
            }

            // CREATE SCHEMA <SCHEMA NAME>
            else if (input.matches("create schema .*")) {
                String[] tokens = input.split(" ");
                // Expected value: tokens = ["create", "schema", "<SCHEMA NAME>"]
                response = service.createSchema(tokens[2]);
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
                String[] tokens = input.split(" ");
                // Expected value: tokens = ["select", "*", "from", "<SCHEMA NAME>"]
                response = service.select(tokens[3]);
            }

            // CREATE TABLE <TABLE NAME> (<PROPERTIES>)
            else if (input.matches("create table .*")) {
                String[] tokens = input.split(" ");
                // Expected value:
                // ["create", "table", "<TABLE NAME>", "(col_1", "type_1(size)", ...]
                int start = input.indexOf('(') + 1;
                int end = input.length() - 1;
                // To reduce confusion between "long int" and "short int", combine
                // into a single word
                String params = input.substring(start, end)
                        .replaceAll("short int", "short")
                        .replaceAll("long int", "long")
                        .replaceAll("primary key", "pri")
                        .replaceAll("not null", "nnull")
                        .replaceAll(", ", ",");
                response = service.createTable(tokens[2], params.split(","));
            }


            // INSERT INTO <TABLE NAME> VALUES (<VALUES>)
            else if (input.matches("insert into .*")) {
                String[] tokens = input.split(" ");
                // Expected value: ["insert", "into", "<TABLE NAME>", "values",
                // "(value_1,", "value_2," ... "value_n)"]
                // CORRECT THIS
                response = service.insert(tokens[2], tokens);
            }

            // DROP TABLE
            // Does this have to be implemented?
            else if (input.matches("drop table")) {
                response = service.dropTable(input);
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
