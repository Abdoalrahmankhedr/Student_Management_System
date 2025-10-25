package databases;

import models.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Andrew :)

/*
    Parent abstract class for all databases

    Methods such as readFile & saveToFile were copied from our previous project here:
    https://github.com/acskii/inventory-management/blob/main/src/main/java/databases/GenericDatabase.java
*/

public abstract class Database<T extends Model> {
    protected String logName;
    protected final String filename;
    protected final List<T> records = new ArrayList<>();

    public Database(String filename) {
        this.filename = filename;
    }

    protected void insertRecord(T record) {
        if (record != null) {
            this.records.add(record);
        }
    }

    public void readFile() {
        File file = new File(this.filename);

        if (!file.exists() || !file.getName().endsWith(".txt")) {
            System.out.printf("[%s]: Unable to read file!\n", logName);
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            // Create Scanner resource and cleanup

            // Reset records before reading
            this.records.clear();
            // As long as the file has lines,
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                T record = createRecordFrom(line);
                insertRecord(record);
            }
            System.out.printf("[%s]: Successfully read all records!\n", logName);
        } catch (FileNotFoundException err) {
            System.out.printf("[%s]: File was not found\n", logName);
        }
    }

    public void saveToFile() {
        File file = new File(this.filename);
        try {
            if (!file.exists() && file.getName().endsWith(".txt")) {
                if (file.createNewFile()) System.out.printf("[%s]: Successfully created new file!\n", logName);
                else {
                    System.out.printf("[%s]: Failed to create new file\n", logName);
                    return;
                }
            }
        }
        catch (IOException err) {
            System.out.printf("[%s]: Failed to create new file\n", logName);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename))) {
            // Create BufferedWriter resource and cleanup

            StringBuilder sb = new StringBuilder();
            // Build a string from all record line representations
            // and write that to file
            for (T record : this.records) {
                sb.append(record.lineRepresentation()).append("\n");
            }
            writer.write(sb.toString());
            System.out.printf("[%s]:  Successfully wrote to file!\n", logName);
        } catch (IOException err) {
            System.out.printf("[%s]:  File can not be accessed, perhaps it is being used by another program..\n", logName);
        }
    }

    public abstract T createRecordFrom(String line);
}
