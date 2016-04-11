package db.walrusql;

import db.walrusql.models.Schema;
import db.walrusql.models.Table;

/**
 * Created by atm on 4/10/16.
 */
public class DatabaseService {
    Schema schema;
    Table table;

    public DatabaseService() {
        schema = new Schema();
        table = new Table();
    }

    public boolean showSchemas() {
        return schema.show();
    }

    public boolean useSchema() {
        return schema.use();
    }

    public boolean createSchema() {
        return schema.create();
    }

    public boolean showTables() {
        return table.show();
    }

    public boolean select() {
        return table.select();
    }

    public boolean createTable() {
        return table.create();
    }

    public boolean insert() {
        return table.insert();
    }

    public boolean dropTable() {
        return table.drop();
    }
}
