package db.walrusql.models;

import db.walrusql.DataHandler;

/**
 * Created by atm on 4/10/16.
 */
public class Schema {
    private String schema = null;

    /*
        SHOW SCHEMAS;
     */
    public boolean show() {
        Table t = new Table("information_schema", "schemata");
        t.show();
        return true;
    }

    /*
        USE SCHEMA <SCHEMA NAME>;
     */
    public boolean use() {
        if (schemaExists()) {
            System.out.println("Using " + schema);
            return true;
        } else {
            schema = null;
            System.out.println("FAIL: Cannot 'use' " + schema + ". " +
                    "Schema with that name does not exist");
            return false;
        }
    }

    /*
        CREATE SCHEMA <SCHEMA NAME>;
     */
    public boolean create() {
        // Create only if schema does NOT exist
        // || condition for now
        if (!schemaExists() || true) {
            // Insert into information schema
            return true;
        } else {
            schema = null;
            System.out.println("FAIL: Schema already exists");
            return false;
        }
    }

    private boolean exists(String schemaName) {
        return true;
    }

    // schema should be a valid schema
    private boolean schemaExists() {
        if (!true) {
            System.out.println("FAIL: Invalid schema in schema name");
            return false;
        }
        return true;
    }
    public String getCurrent() {
        return schema;
    }

    public boolean setSchema(String schema) {
        this.schema = schema;
        return true;
    }
}
