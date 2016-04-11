package db.walrusql.models;

import db.walrusql.Constant;
import db.walrusql.DataHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
            DataHandler handler = new DataHandler(Constant.tablesTableName, "r");
            ArrayList<ArrayList> response = handler.fetchTables();
            for(ArrayList arr: response) {
                if (schema.equals(arr.get(0))) {
                    System.out.println(arr.get(1));
                }
            }
            return true;
        }
        return false;
    }

    /*
        SELECT * FROM <TABLE> (No conditions)
     */
    public boolean select() {
        if (sanity()) {
            DataHandler handler = new DataHandler(Constant.columnsTableName, "r");
            ArrayList<ArrayList> columnsTable = handler.fetchColumns();
            // Stores the column structure of the table
            ArrayList<ArrayList> columns = new ArrayList();
            ArrayList<String> types = new ArrayList();
            for (ArrayList c: columnsTable) {
                if(c.get(1).toString().toLowerCase().equals(table)) {
                    columns.add(c);
                    types.add((String)c.get(4));
                }
            }

            // TODO: Fetch data depending on `types` array
            System.out.println(types);

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
    public boolean create(String[] cols) {
        if (schemaExists()) {
            if (silentTableExists()) {
                System.out.println("Table with this name already exists");
                return false;
            }

            DataHandler tableHandler = new DataHandler(Constant.tablesTableName, "rw");
            DataHandler columnHandler = new DataHandler(Constant.columnsTableName, "rw");

            System.out.println(schema);
            System.out.println(table);
            tableHandler.insertIntoTables(schema, table, cols.length);
            columnHandler.insertIntoColumns(schema, table, cols);

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
        boolean response = silentTableExists();
        if (!response) {
            System.out.println("FAIL: Table does not exist");
        }
        return response;
    }

    private boolean silentTableExists() {
        boolean exists = false;
        DataHandler handler = new DataHandler(Constant.tablesTableName, "r");
        ArrayList<ArrayList> response = handler.fetchTables();
        for(ArrayList arr: response) {
            if (table.equals(arr.get(1).toString().toLowerCase()) &&
                    schema.equals(arr.get(0).toString().toLowerCase())) {
                exists = true;
            }
        }
        return exists;
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
