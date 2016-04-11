package db.walrusql.models;

/**
 * Created by atm on 4/10/16.
 */
public class Table {
    private String schema = null;
    private String current = null;  // Current table

    public Table() {
    }

    public Table(String schemaName, String tableName) {
        schema = schemaName;
        current = tableName;
    }

    /*
        SHOW TABLES
     */
    public boolean show() {
        if (databaseExists()) {
            // Code to show all the tables
            return true;
        }
        return false;
    }

    /*
        SELECT * FROM <TABLE> (No conditions)
     */
    public boolean select(String table) {
        current = table;
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
    public boolean create(String table, String[] datatypes) {
        current = table;
        if (databaseExists()) {
            // Create table
            return true;
        }
        return false;
    }

    /*
        INSERT INTO TABLE <TABLE> VALUES (value_1, value_2, ..., value_n);
     */
    public boolean insert(String table, String[] values) {
        current = table;
        if (sanity()) {
            // Try inserting
            return true;
        }
        return false;
    }

    /*

     */
    public boolean drop(String table) {
        current = table;
        if (sanity()) {
            // Try dropping
            return true;
        }
        return false;
    }

    private boolean sanity() {
        return databaseExists() && tableExists();
    }

    private boolean tableExists() {
        // Write a better condition here
        if (!true) {
            System.out.println("FAIL: Table does not exist");
            return false;
        }
        return true;
    }

    private boolean databaseExists() {
        if (schema == null) {
            System.out.println("FAIL: No database selected");
            return false;
        }
        return true;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
