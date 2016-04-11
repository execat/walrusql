package db.walrusql.models;

/**
 * Created by atm on 4/10/16.
 */
public class Table {
    private String currentSchema = null;
    private String current = null;  // Current table

    public Table() {
    }

    public Table(String schemaName, String tableName) {
        currentSchema = schemaName;
        current = tableName;
    }

    public boolean show() {
        return true;
    }

    public boolean select() {
        return checkCurrentTable();
    }

    public boolean create() {
        // Must not exist
        return true;
    }

    public boolean insert() {
        return checkCurrentTable();
    }

    public boolean drop() {
        return exists();
    }

    private boolean checkCurrentTable() {
        if (currentSchema != null && current != null) {
            return true;
        }
        return false;
    }

    private boolean exists() {
        return true;
    }
}
