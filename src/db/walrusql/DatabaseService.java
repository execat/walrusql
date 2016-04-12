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

    public DatabaseService() {
        schema = new Schema();
        table = new Table();
    }

    /*
     *  SCHEMAS
     */
    // Show schemas
    public boolean showSchemas() {
        return schema.show();
    }

    // Use schema
    public boolean useSchema(String name) {
        return schema.setSchema(name) &&
                schema.use() &&
                table.setSchema(name);
    }

    // Create schema
    public boolean createSchema(String schemaName) {
        return schema.setSchema(schemaName) &&
                schema.create();
    }

    /*
     *  TABLES
     */
    // Show tables
    public boolean showTables() {
        return table.show();
    }

    // Select * from table
    public boolean select(String tableName) {
        return table.setTable(tableName) && table.select();
    }

    public boolean select(String tableName, String columnName,
                          String operand, String value) {
        return table.setTable(tableName) &&
                table.select(columnName, operand, value);
    }

    // Create table
    public boolean createTable(String tableName, String[] datatypes) {
        return table.setTable(tableName) && table.create(datatypes);
    }

    // Insert into table
    public boolean insert(String tableName, String[] values) {
        return table.setTable(tableName) && table.insert(values);
    }

    // Not sure if this is to be implemented
    public boolean dropTable(String tableName) {
        table.setTable(tableName);
        return table.drop();
    }
}
