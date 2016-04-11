package db.walrusql;

import db.walrusql.models.InformationSchema;

public class Walrusql {
    // Checks for presence of information schema
    private static boolean checkInformationSchema() {
        InformationSchema i = new InformationSchema();
        return i.create();
    }

    public static void main(String[] args) {
        Parser p;
        // If information schema is set, start parser else return
        if (checkInformationSchema()) {
            p = new Parser();
        } else {
            System.out.println("There was some error initializing the information schema. Please delete all your data files and try again.");
            return;
        }

        p.parse();
    }
}
