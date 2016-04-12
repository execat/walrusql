package db.walrusql;

import javax.swing.text.DateFormatter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by atm on 4/10/16.
 */
public class DataHandler {
    RandomAccessFile file = null;

    public DataHandler(String name, String access) {
        try {
            file = new RandomAccessFile(name, access);
        } catch (FileNotFoundException e) {
            System.out.println("Error in DataHandler#DataHandler");
            e.printStackTrace();
        }
    }

    public boolean writeVarchar(String input) {
        try {
            // Remove quotes
            input = input.substring(1, input.length() - 1);
            // Insert
            file.writeByte(input.length());
            file.writeBytes(input);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeLong(int i) {
        try {
            file.writeLong(i);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeInt(int i) {
        try {
            file.writeInt(i);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean reset() {
        try {
            file.seek(0);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean end() {
        try {
            file.seek(file.length());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> fetchSchemas() {
        ArrayList output = new ArrayList();
        try {
            do {
                output.add(new String(readVarchar()));
            } while(true);
        } catch (IOException e) {
        }
        return output;
    }

    private String readVarchar() throws IOException {
        int length = 0;
        byte[] bytes;
        length = file.readByte();
        bytes = new byte[length];
        file.read(bytes);
        return new String(bytes);
    }

    public ArrayList fetchTables() {
        ArrayList<ArrayList> output = new ArrayList();
        try {
            do {
                ArrayList sublist = new ArrayList();

                // Table schema
                sublist.add(new String(readVarchar()));
                // Table name
                sublist.add(new String(readVarchar()));
                // Table rows
                sublist.add(file.readLong());

                output.add(sublist);
            } while(true);
        } catch (IOException e) {
        }
        return output;
    }

    public ArrayList<ArrayList> fetchColumns() {
        ArrayList<ArrayList> output = new ArrayList();
        try {
            do {
                ArrayList sublist = new ArrayList();

                sublist.add(new String(readVarchar()));
                sublist.add(new String(readVarchar()));
                sublist.add(new String(readVarchar()));
                sublist.add(file.readInt());
                sublist.add(new String(readVarchar()));
                sublist.add(new String(readVarchar()));
                sublist.add(new String(readVarchar()));

                output.add(sublist);
            } while(true);
        } catch (IOException e) {
        }
        return output;
    }

    public boolean insertIntoTables(String schema, String table, int rows) {
        end();
        writeVarchar(schema);
        writeVarchar(table);
        writeLong(rows);
        return true;
    }


    public boolean insertIntoColumns(String schema, String table, String[] cols) {
        /*
            Assume cols are in this format:
            col_name_1 data_type(size) not_null primary_key
         */
        end();
        int position = 1;
        for(String col: cols) {
            String[] tokens = col.split(" ");
            // Schema name
            writeVarchar(schema);
            // Table name
            writeVarchar(table);
            // Column name
            writeVarchar(tokens[0]);
            // Position
            writeInt(position);
            // Column type
            writeVarchar(tokens[1]);

            // Nullable?
            boolean nullWritten = false;
            for (int i = 2; i < tokens.length; i++) {
                if ((tokens[i] == "nnull" || tokens[i] == "pri") && !nullWritten) {
                    writeVarchar("NO");
                    nullWritten = true;
                }
            }
            if(!nullWritten) {
                writeVarchar("YES");
            }

            // PK?
            boolean pkWritten = false;
            for (int i = 2; i < tokens.length; i++) {
                if (tokens[i] == "pri" && !pkWritten) {
                    writeVarchar("PRI");
                    pkWritten = true;
                }
            }
            if(!pkWritten) {
                writeVarchar("");
            }

            position++;
        }
        return true;
    }

    public boolean writeNumber(String i, String type) {
        int number = Integer.parseInt(i);

        try {
            switch(type) {
                case "byte":    file.write(number); break;
                case "short":   file.writeShort(number); break;
                case "int":     file.writeInt(number); break;
                case "long":    file.writeLong(number); break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean write(String i, String type) {
        // Date and datetime are not implemented
        try {
            if (type.equals("byte") || type.equals("short") ||
                    type.equals("int") || type.equals("long")) {
                writeNumber(i, type);
            }
            // TODO: implement char and date and datetime
            if (type.matches("char") && !type.matches("varchar")) {
                writeVarchar("char::Not implemented");
            } else if (type.matches("varchar")) {
                writeVarchar(i);
            } else if (type == "float") {
                float f = Float.parseFloat(i);
                file.writeFloat(f);
            } else if (type == "double") {
                double d = Double.parseDouble(i);
                file.writeDouble(d);
            } else if (type == "date" || type == "datetime") {
                SimpleDateFormat format = null;
                if (type == "date") {
                    format = new SimpleDateFormat("YYYY-MM-dd");
                } else if (type == "datetime") {
                    format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
                }
                file.writeLong(format.parse(i).getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Could not parse date/datetime");
            e.printStackTrace();
        }
        return true;
    }

    public boolean writeRecords(String[] values, ArrayList<String> types) {
        int i = 0;
        for (String type: types) {
            write(values[i], type);
            i++;
        }
        return true;
    }

    private Object read(String type) throws IOException {
        Object o = null;
        switch(type) {
            case "byte":
                o = new Byte(file.readByte());
                break;
            case "short":
                o = new Short(file.readShort());
                break;
            case "int":
                int num = file.readInt();
                o = new Integer(num);
                break;
            case "long":
                o = new Long(file.readLong());
                break;
            // case "char":    o = file.readChar(); break;
            case "varchar":
                o = readVarchar();
                break;
            case "float":
                o = new Float(file.readFloat());
                break;
            case "double":
                o = new Double(file.readDouble());
                break;
            case "datetime":
            case "date":
                o = new Date(file.readLong()).toString();
                break;
        }
        return o;
    }

    public ArrayList readRecords(ArrayList<String> types) {
        ArrayList<ArrayList> result = new ArrayList();
        boolean shouldBreak = false;
        do {
            ArrayList<Object> o = new ArrayList<>();
            for (String type : types) {
                try {
                    o.add(read(type));
                } catch (IOException e) {
                    shouldBreak = true;
                }
            }
            result.add(o);
        } while(!shouldBreak);
        return result;
    }
}
