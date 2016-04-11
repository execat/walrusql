package db.walrusql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

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
                int length = file.readByte();
                byte[] bytes = new byte[length];
                file.read(bytes);
                output.add(new String(bytes));
            } while(true);
        } catch (IOException e) {
        }
        return output;
    }

}
