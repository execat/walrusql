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
import java.util.Collections;
import java.util.Comparator;
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
            if(input.contains("'")) {
                input = input.substring(1, input.length() - 1);
            }
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

    public boolean start() {
        try {
            file.seek(0);
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
                if ((tokens[i].equals("nnull") || tokens[i].equals("pri")) && !nullWritten) {
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
                if (tokens[i].equals("pri") && !pkWritten) {
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
            } else if (type.equals("float")) {
                float f = Float.parseFloat(i);
                file.writeFloat(f);
            } else if (type.equals("double")) {
                double d = Double.parseDouble(i);
                file.writeDouble(d);
            } else if (type.equals("date") || type.equals("datetime")) {
                SimpleDateFormat format = null;
                if (type.equals("date")) {
                    format = new SimpleDateFormat("YYYY-MM-dd");
                } else if (type.equals("datetime")) {
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

    public ArrayList<ArrayList> readRecords(ArrayList<String> types, boolean addPosition) {
        ArrayList<ArrayList> result = new ArrayList();
        start();
        boolean shouldBreak = false;
        do {
            ArrayList<Object> o = new ArrayList();
            // This is needed for index creation
            if(addPosition) {
                o.add(position());
            }
            for (String type : types) {
                try {
                    Object obj = read(type);
                    o.add(obj);
                } catch (IOException e) {
                    shouldBreak = true;
                }
            }
            result.add(o);
        } while(!shouldBreak);
        return result;
    }

    private long position() {
        try {
            return file.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean createIndex(int index, ArrayList<String> types, DataHandler data) {
        ArrayList<ArrayList> records = data.readRecords(types, true);
        ArrayList<ArrayList> indexes = new ArrayList<>();
        for(ArrayList record: records) {
            if(record.size() == 0) {
                throw new IndexOutOfBoundsException();
            }
            ArrayList insert = new ArrayList();
            try {
                insert.add(record.get(index + 1));
                insert.add(1);
                insert.add(record.get(0));
            } catch (IndexOutOfBoundsException e) {
            }

            indexes.add(insert);
        }

        for(int i = 0; i < indexes.size(); i++) {
            for (int j = i + 1; j < indexes.size(); j++) {
                try {
                    if(!(indexes.get(j).size() < 2 || indexes.get(i).size() < 2)) {
                        ArrayList a = indexes.get(i);
                        ArrayList b = indexes.get(j);
                        if (a.get(0).equals(b.get(0))) {
                            ArrayList modified = new ArrayList();
                            modified.add(a.get(0));
                            modified.add(Integer.parseInt(a.get(1).toString()) +
                                    Integer.parseInt(b.get(1).toString()));

                            // For merging data
                            for(int x = 2; x < a.size(); x++) {
                                modified.add(a.get(x));
                            }
                            for(int x = 2; x < b.size(); x++) {
                                modified.add(b.get(x));
                            }
                            indexes.remove(j);
                            j--;
                            indexes.remove(i);
                            indexes.add(i, modified);
                            i--;
                        }
                    }
                } catch(IndexOutOfBoundsException e) {

                }
            }
        }

        String type = types.get(index);


        Collections.sort(indexes, new Comparator<ArrayList>() {
            @Override
            public int compare(ArrayList o1, ArrayList o2) {
                if(!(o1.size() > 0 && o2.size() > 0)) {
                    return 0;
                }

                Object a = o1.get(0);
                Object b = o2.get(0);

                if (type.matches("char") && !type.matches("varchar")) {
                    System.out.println("Can't compare char");
                } else if (type.matches("varchar")) {
                    return (o1.toString()).compareTo(o2.toString());
                } else if (type.equals("float")) {
                    Float f1 = new Float(Float.parseFloat(a.toString()));
                    Float f2 = new Float(Float.parseFloat(b.toString()));
                    return f1.compareTo(f2);
                } else if (type.equals("double")) {
                    Double f1 = new Double(Double.parseDouble(a.toString()));
                    Double f2 = new Double(Double.parseDouble(b.toString()));
                    return f1.compareTo(f2);
                } else if (type.equals("byte") || type.equals("int") ||
                        type.equals("long") || type.equals("short")) {
                    Long f1 = new Long(Long.parseLong(a.toString()));
                    Long f2 = new Long(Long.parseLong(b.toString()));
                    return f1.compareTo(f2);
                }
                return 0;
        } } );

        for(ArrayList list: indexes) {
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                if (i == 0) {
                    if(type.equals("byte") || type.equals("short")) {
                        write(o.toString(), "int");
                    } else {
                        write(o.toString(), type);
                    }
                } else {
                    write(o.toString(), "int");
                }
            }
        }

        return true;
    }
}
