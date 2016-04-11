package db.walrusql.models;

/**
 * Created by atm on 4/10/16.
 */
public class Table {
    private String schema = null;
    private String table = null;  // Current table

    public Table() {
    }

    public Table(String schemaName, String tableName) {
        schema = schemaName;
        table = tableName;
    }

    /*
        SHOW TABLES
     */
    public boolean show() {
        if (schemaExists()) {
            // Code to show all the tables
            return true;
        }
        return false;
    }

    /*
        SELECT * FROM <TABLE> (No conditions)
     */
    public boolean select() {
        if (sanity()) {
            // SELECT * FROM <TABLE>
            return true;
        }
        return false;
    }

    /*
        CREATE TABLE <TABLE> (
            column_name_1 data_type(size) [primary_key|not null],
            column_name_2 data_type(size) [primary_key|not null],
            :
            column_name_n data_type(size) [primary_key|not null],
        );
     */
    public boolean create(String[] datatypes) {
        if (schemaExists()) {
            // Create table
            return true;
        }
        return false;
    }

    /*
        INSERT INTO TABLE <TABLE> VALUES (value_1, value_2, ..., value_n);
     */
    public boolean insert(String[] values) {
        if (sanity()) {
            // Try inserting
            return true;
        }
        return false;
    }

    /*
        DO YOU HAVE TO SUPPORT DROP OR NOT?
     */
    public boolean drop() {
        if (sanity()) {
            // Try dropping
            return true;
        }
        return false;
    }

    /*
        Existance checkers
     */
    private boolean sanity() {
        return schemaExists() && tableExists();
    }

    private boolean tableExists() {
        // Write a better condition here
        if (!true) {
            System.out.println("FAIL: Table does not exist");
            return false;
        }
        return true;
    }

    private boolean schemaExists() {
        if (schema == null) {
            System.out.println("FAIL: No database selected");
            return false;
        }
        return true;
    }

    /*
        Setters
     */
    public boolean setSchema(String schema) {
        this.schema = schema;
        return true;
    }

    public boolean setTable(String table) {
        this.table = table;
        return true;
    }
}
