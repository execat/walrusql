package db.walrusql.models;

import db.walrusql.DataHandler;

/**
 * Created by atm on 4/10/16.
 */
public class Schema {
    private String current = null;

    public boolean show() {
        Table t = new Table("information_schema", "schemata");
        return true;
    }

    public boolean use(String name) {
        if (exists(name)) {
            System.out.println("Using " + name);
            current = name;
            return true;
        } else {
            System.out.println("FAIL: Cannot 'use' " + name + ". " +
                    "Schema with that name does not exist");
            return false;
        }
    }

    public boolean create() {
        return true;
    }

    private boolean exists(String name) {
        return true;
    }

    public String getCurrent() {
        return current;
    }
}
