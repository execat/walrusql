package db.walrusql;

import db.walrusql.models.InformationSchema;
import db.walrusql.models.Schema;
import db.walrusql.models.Table;

/**
 * Created by atm on 4/10/16.
 */
public class DatabaseService {
    Schema schema;
    Table table;
    String input;

    public DatabaseService(String str) {
        schema = new Schema();
        table = new Table();
        input = str;
    }

    public boolean showSchemas() {
        return schema.show();
    }

    public boolean useSchema(String name) {
        return schema.use(name) && table.setSchema(name);
    }

    public boolean createSchema() {
        return schema.create();
    }

    public boolean showTables(String tableName) {
        return table.show(schema.getCurrent(), tableName);
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
