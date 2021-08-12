package com.coelhovictor.localstorage.controllers;

import com.coelhovictor.localstorage.objs.DataKey;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author VictorCoelho
 */
public class LocalStorageController {
    
    /**
     * Database cache.
     */
    protected HashMap<String, DataKey> dbCache = new HashMap<>();
    
    /**
     * Database path.
     */
    protected String dbPath;
    
    /**
     * Database file extension.
     */
    protected String fileExtension;
    
    /**
     * Class constructor.
     * 
     * @param dbPath the database path
     */
    public LocalStorageController(String dbPath) {
        this.dbCache.clear();
        if(!dbPath.endsWith(File.separator)) {
            dbPath = dbPath + File.separator;
        }
        this.dbPath = dbPath;
        this.fileExtension = "database";
        
        Path path = Paths.get(this.dbPath);
        try {
            Files.createDirectories(path);
        } catch(IOException ex) {
        }
        
        for (final File fileEntry : new File(this.dbPath).listFiles()) {
            if(fileEntry.isFile()) {
                if(fileEntry.getName().toLowerCase().endsWith("." + this.fileExtension)) {
                    load(fileEntry);
                }
            }
        }
        
    }
    
    /**
     * Load a file.
     * 
     * @param file target file
     */
    public void load(File file) {
        DataKey obj = null;
        String content = "";
        
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();
            while(line != null) {
                content = content + line;
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        
        if(!content.isEmpty()) {
            try {
                JsonArray json = new JsonParser().parse(content).getAsJsonArray();
                if(json != null) {
                    String key = file.getName().replace("." + this.fileExtension, "");
                    obj = new DataKey(key);
                    for(JsonElement as : json) {
                        try {
                            JsonObject jsonObj = as.getAsJsonObject();
                            String id = jsonObj.get("id").getAsString();
                            obj.addObject(id, jsonObj);
                        } catch(Exception ex) {
                        }
                    }
                }
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
            }
        }
        
        if(obj != null) {
            if(this.dbCache.containsKey(obj.getKey().toLowerCase())) {
                this.dbCache.replace(obj.getKey().toLowerCase(), obj);
            } else {
                this.dbCache.put(obj.getKey().toLowerCase(), obj);
            }
        }
    }
    
    /**
     * Returns the target datakey.
     * 
     * @param key target key
     * @return <code>DataKey</code> target datakey
     */
    public DataKey get(String key) {
        if(this.dbCache.containsKey(key.toLowerCase())) {
            return this.dbCache.get(key.toLowerCase());
        }
        return new DataKey(key);
    }
    
    /**
     * Save a datakey.
     * 
     * @param data target datakey
     */
    public void save(DataKey data) {
        this.dbCache.put(data.getKey().toLowerCase(), data);
        
        File file = new File(this.dbPath + data.getKey().toLowerCase() + "." + this.fileExtension);
        try(FileWriter fileWriter = new FileWriter(file)) {
            
            JsonArray json = new JsonArray();
            for(JsonObject obj : data.getAll().values()) {
                json.add(obj);
            }
            fileWriter.write(json.toString());
            
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Database controller.
     */
    private static LocalStorageController controller;
    
    /**
     * Returns the database controller.
     * 
     * @return <code>LocalStorageController</code> database controller
     */
    public static LocalStorageController getInstance() {
        return controller;
    }
    
    /**
     * Inicialize the database controller.
     * 
     * @param dbPath database path
     * @return <code>LocalStorageController</code> database controller
     */
    public static LocalStorageController inicialize(String dbPath) {
        controller = new LocalStorageController(dbPath);
        return controller;
    }
    
}
