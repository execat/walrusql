package db.walrusql;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by atm on 4/11/16.
 */
public class Constant {
    public static String directory  = "data/";
    public static String schemataTableName =
            directory + "information_schema.schemata.tbl";
    public static String tablesTableName =
            directory + "information_schema.tables.tbl";
    public static String columnsTableName =
            directory + "information_schema.columns.tbl";

    private static String[] comparable = {"byte", "int", "short", "long", "float",
            "double", "datetime", "date"};
    public static ArrayList<String> comparableTypes =
            new ArrayList(Arrays.asList(comparable));
}
