package ru.avalon.javaapp.dev120.propertyfiledemo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PropertyFile {
    private final Map<String, String> properties = new HashMap<>();
    private File usedFile;
    public PropertyFile() {
    }

    public PropertyFile(String fileName) throws IOException {
        this(new File(fileName));
    }

    public PropertyFile(File file) throws IOException {
        if (file == null){
            throw new IllegalArgumentException("Illegal file");
        }
        read(file);
    }

    public String get(String propertyName) {
        return properties.get(propertyName);
    }

    public void set(String propertyName, String propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    public void delete(String propertyName) {
        properties.remove(propertyName);
    }

    public boolean contains(String propertyName) {
        return properties.containsKey(propertyName);
    }

    private void read(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            int lineNo = 0;
            while ((s = br.readLine()) != null) {
                lineNo++;
                if (s.isEmpty() || s.charAt(0) == '#') {
                    continue;
                }

                int p = s.indexOf('=');
                if (p == -1) {
                    throw new IOException(String.format("Broken file. String %d in wrong format", lineNo));
                }

                String key = s.substring(0, p);
                String value = s.substring(p + 1);

                properties.put(key, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        usedFile = file;
    }

    public void save(File file) throws FileNotFoundException {
        if (file == null){
            throw new IllegalArgumentException("File cant be null");
        }
        try (PrintWriter pw = new PrintWriter(file)) {
            properties.forEach((k,v)->pw.println(String.format("%s=%s", k, v)));
        }
        usedFile = file;
    }

    public void save(String fileName) throws FileNotFoundException {
        save(new File(fileName));
    }

    public void save() throws FileNotFoundException {
        if (usedFile == null){
            throw new IllegalStateException("File wasn't initialized");
        }
        save(usedFile);
    }
}
