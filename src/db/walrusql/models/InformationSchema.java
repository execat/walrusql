package db.walrusql.models;

import db.walrusql.Constant;
import db.walrusql.DataHandler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by atm on 4/10/16.
 */
public class InformationSchema {
    static String schemataTableName;
    static String tablesTableName;
    static String columnsTableName;
    static String directory;

    public InformationSchema() {
        directory  = Constant.directory;
        schemataTableName = Constant.schemataTableName;
        tablesTableName = Constant.tablesTableName;
        columnsTableName = Constant.columnsTableName;
    }

    public boolean create() {
        try {
            // Make directory tree for source
            File dir = new File(directory);
            dir.mkdirs();

            DataHandler schema = null;
            DataHandler tables = null;
            DataHandler columns = null;

            // Create files if they don't exist (Assuming schemataTableName is an indicator for all 3 files)
            // All or nothing
            if (!new File(schemataTableName).exists()) {
                schema = new DataHandler(schemataTableName, "rw");
                tables = new DataHandler(tablesTableName, "rw");
                columns = new DataHandler(columnsTableName, "rw");
            } else {
                System.out.println("Files already exist. Will not execute unsafe operation. " +
                        "Exiting without creating new information schema. " +
                        "Delete files manually to force this operation."
                );
                return true;
            }

            /*
             *  Create the SCHEMATA table file.
             *  Initially it has only one entry: information_schema
             */
            schema.writeVarchar("information_schema");

            /*
             *  Create the TABLES table file.
             *  Initially it has three rows (each row may have a different length).
             *  It has the following columns:
             *      # Table schema
             *      # Table name
             *      # Table rows
             */
            // 1: information_schema.tables.tbl
            tables.writeVarchar("information_schema");
            tables.writeVarchar("SCHEMATA");
            tables.writeLong(1);

            // 2: information_schema.tables.tbl
            tables.writeVarchar("information_schema");
            tables.writeVarchar("TABLES");
            tables.writeLong(3);

            // 3: information_schema.tables.tbl
            tables.writeVarchar("information_schema");
            tables.writeVarchar("COLUMNS");
            tables.writeLong(11);

            /*
             *  Create the COLUMNS table file.
             *  Initially it has 11 rows.
             *  It has the following columns:
             *      # Table schema
             *      # Table name
             *      # Column name
             *      # Ordinal position
             *      # Column type
             *      # Is nullable
             *      # Column key
             */
            // 1: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("SCHEMATA");
            columns.writeVarchar("SCHEMA_NAME");
            columns.writeInt(1);
            columns.writeVarchar("varchar(64)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 2: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("TABLES");
            columns.writeVarchar("TABLE_SCHEMA");
            columns.writeInt(1);
            columns.writeVarchar("varchar(64)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 3: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("TABLES");
            columns.writeVarchar("TABLE_NAME");
            columns.writeInt(2);
            columns.writeVarchar("varchar(64)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 4: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("TABLES");
            columns.writeVarchar("TABLE_ROWS");
            columns.writeInt(3);
            columns.writeVarchar("long int");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 5: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("COLUMNS");
            columns.writeVarchar("TABLE_SCHEMA");
            columns.writeInt(1);
            columns.writeVarchar("varchar(64)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 6: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("COLUMNS");
            columns.writeVarchar("TABLE_NAME");
            columns.writeInt(2); // ORDINAL_POSITION
            columns.writeVarchar("varchar(64)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 7: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("COLUMNS");
            columns.writeVarchar("COLUMN_NAME");
            columns.writeInt(3); // ORDINAL_POSITION
            columns.writeVarchar("varchar(64)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 8: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("COLUMNS");
            columns.writeVarchar("ORDINAL_POSITION");
            columns.writeInt(4); // ORDINAL_POSITION
            columns.writeVarchar("int");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 9: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("COLUMNS");
            columns.writeVarchar("COLUMN_TYPE");
            columns.writeInt(5); // ORDINAL_POSITION
            columns.writeVarchar("varchar(64)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 10: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("COLUMNS");
            columns.writeVarchar("IS_NULLABLE");
            columns.writeInt(6); // ORDINAL_POSITION
            columns.writeVarchar("varchar(3)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");

            // 11: information_schema.columns.tbl
            columns.writeVarchar("information_schema");
            columns.writeVarchar("COLUMNS");
            columns.writeVarchar("COLUMN_KEY");
            columns.writeInt(7); // ORDINAL_POSITION
            columns.writeVarchar("varchar(3)");
            columns.writeVarchar("NO");
            columns.writeVarchar("");
        }
        catch(Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}
