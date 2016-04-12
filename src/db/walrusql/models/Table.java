package db.walrusql.models;

import com.sun.tools.internal.jxc.ap.Const;
import com.sun.tools.jdi.DoubleTypeImpl;
import db.walrusql.Constant;
import db.walrusql.DataHandler;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.RandomAccessFile;
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
    public ArrayList<ArrayList> silentSelect() {
        DataHandler dataHandler = null;
        if (sanity()) {
            ArrayList types = tableColumnTypes();
            String directory = Constant.directory;
            String filename = directory + schema.toLowerCase() + "." +
                    table.toLowerCase() + ".tbl";
            dataHandler = new DataHandler(filename, "r");
            ArrayList<ArrayList> result = dataHandler.readRecords(types);


            // TODO: Fetch data depending on `types` array
            // System.out.println(columns);
            // System.out.println(types);

            return result;
        }
        return null;
    }

    public boolean select() {
        ArrayList<ArrayList> data = silentSelect();

        System.out.println(tableColumnNames());
        for(Object o: data) {
            System.out.println(o);
        }

        return true;
    }

    /*
        SELECT * FROM <TABLE> (No conditions)
     */
    public boolean select(String columnName, String operand, String value) {
        ArrayList<ArrayList> data = silentSelect();
        ArrayList<ArrayList> result = filter(data, columnName, operand, value);

        System.out.println(tableColumnNames());

        if (result == null) {
            System.out.println("No data");
            return false;
        }

        for(Object o: result) {
            System.out.println(o);
        }

        return true;
    }

    private ArrayList<ArrayList> filter(ArrayList<ArrayList> data, String columnName,
                                        String operand, String value) {
        ArrayList<String> columnNames = tableColumnNames();
        ArrayList<String> columnTypes = tableColumnTypes();
        ArrayList<ArrayList> result = new ArrayList();

        int index = columnNames.indexOf(columnName);
        // If column name does not exist
        if(index < 0) {
            System.out.println("No column named " + columnName);
            return null;
        }

        String columnType = columnTypes.get(index);

        if(value.contains("'")) {
            value = value.substring(1, value.length() - 1);
        }

        for(ArrayList list: data) {
            try {
                Comparable compare = (Comparable) list.get(index);
                // < and > only for comparable types
                if (Constant.comparableTypes.contains(columnType)) {
                    Object d = null;
                    switch (columnType) {
                        case "int":
                            d = Integer.parseInt(value);
                            break;
                        case "byte":
                            d = Byte.parseByte(value);
                            break;
                        case "short":
                            d = Short.parseShort(value);
                            break;
                        case "float":
                            d = Float.parseFloat(value);
                            break;
                        case "double":
                            d = Double.parseDouble(value);
                            break;
                        case "date":
                        case "datetime":
                        case "long":
                            d = Long.parseLong(value);
                            break;
                    }

                    if (operand.equals(">")) {
                        if (compare.compareTo(d) > 0) {
                            result.add(list);
                        }
                    } else if (operand.equals("<")) {
                        if (compare.compareTo(d) < 0) {
                            result.add(list);
                        }
                    }
                }
                if (operand.equals("=")) {
                    try {
                        if (compare.toString().equals(value.toString())) {
                            result.add(list);
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                }
                if (operand.equals("!=")) {
                    try {
                        if (!compare.toString().equals(value.toString())) {
                            result.add(list);
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }

        return result;
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
        DataHandler dataHandler = null;
        if (sanity()) {
            String directory = Constant.directory;
            String filename = directory + schema.toLowerCase() + "." +
                    table.toLowerCase() + ".tbl";

            dataHandler = new DataHandler(filename, "rw");
            dataHandler.end();

            ArrayList<String> types =  tableColumnTypes();

            return dataHandler.writeRecords(values, types);
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

    private ArrayList tableStructure() {
        DataHandler handler = new DataHandler(Constant.columnsTableName, "r");
        ArrayList<ArrayList> columnsTable = handler.fetchColumns();
        // Stores the column structure of the table
        ArrayList<ArrayList> columns = new ArrayList();
        for (ArrayList c: columnsTable) {
            if(c.get(1).toString().toLowerCase().equals(table)) {
                columns.add(c);
            }
        }
        return columns;
    }

    private ArrayList tableColumnTypes() {
        ArrayList<ArrayList> columns = tableStructure();
        ArrayList types = new ArrayList();

        for(ArrayList c: columns) {
            String type = (String)c.get(4);
            int start = type.indexOf('(');
            int end = type.indexOf(')');
            if (start > 0 && end > 0) {
                // Strip brackets
                type = type.substring(0, start) + type.substring(end + 1, type.length());
            }
            types.add(type);
        }
        return types;
    }

    private ArrayList tableColumnNames() {
        ArrayList<ArrayList> columns = tableStructure();
        ArrayList names = new ArrayList();

        for (ArrayList c : columns) {
            String name = c.get(2).toString();
            names.add(name);
        }
        return names;
    }

    /*
        Setters
     */
    public boolean setSchema(String schema) {
        this.schema = schema;
        return true;
    }

    public boolean setTable(String table) {
        if(schema != null) {
            this.table = table;
            return true;
        } else {
            System.out.println("FAIL: No database selected");
            return false;
        }
    }
}
