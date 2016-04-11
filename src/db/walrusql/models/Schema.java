package db.walrusql.models;

import db.walrusql.Constant;
import db.walrusql.DataHandler;

import java.util.ArrayList;

/**
 * Created by atm on 4/10/16.
 */
public class Schema {
    private String schema = null;
    // Create a new handler instance at every call or reset
    DataHandler handler;

    /*
        SHOW SCHEMAS;
     */
    public boolean show() {
        handler = new DataHandler(Constant.schemataTableName, "r");
        ArrayList<String> schemaList = handler.fetchSchemas();
        for(String f: schemaList) {
            System.out.println(f);
        }
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
            System.out.println("FAIL: Cannot 'use' " + schema + ". " +
                    "Schema with that name does not exist");
            schema = null;
            return false;
        }
    }

    /*
        CREATE SCHEMA <SCHEMA NAME>;
     */
    public boolean create() {
        // Create only if schema does NOT exist
        // || condition for now
        if (!silentSchemaExists()) {
            handler = new DataHandler(Constant.schemataTableName, "rw");
            handler.end();
            handler.writeVarchar(schema);
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
        if (silentSchemaExists()) {
            return true;
        } else {
            handler = new DataHandler(Constant.schemataTableName, "r");
            System.out.println("FAIL: Invalid schema in schema name");
            System.out.println("Valid schemas are: " + handler.fetchSchemas());
            return false;
        }
    }

    private boolean silentSchemaExists() {
        handler = new DataHandler(Constant.schemataTableName, "r");
        ArrayList<String> schemaList = handler.fetchSchemas();
        if (schemaList.contains(schema)) {
            return true;
        }
        return false;
    }

    public String getCurrent() {
        return schema;
    }

    public boolean setSchema(String schema) {
        this.schema = schema;
        return true;
    }
}
