package com.coelhovictor.localstorage.objs;

import com.coelhovictor.localstorage.controllers.LocalStorageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author VictorCoelho
 */
public class LocalStorage {
    
    /**
     * Database key.
     */
    private final String key;
    
    /**
     * Database value class.
     */
    private final Class targetClass;
    
    /**
     * <code>Gson</code>
     */
    private final Gson g;
    
    /**
     * <code>JsonParser</code>
     */
    private final JsonParser parser;
    
    /**
     * Class constructor.
     * 
     * @param key database key
     * @param targetClass database value class
     */
    public LocalStorage(String key, Class targetClass) {
        this.key = key;
        this.g = new Gson();
        this.parser = new JsonParser();
        this.targetClass = targetClass;
    }
    
    /**
     * Save a target value.
     * 
     * @param <T> value class 
     * @param id value id
     * @param obj value
     */
    public <T> void save(String id, T obj) {
        JsonObject json = new JsonObject();
        json.addProperty("id", id + "");
        json.add("value", toJson(obj));
        
        LocalStorageController controller = LocalStorageController.getInstance();
        DataKey dados = controller.get(this.key);
        dados.addObject(id + "", json);
        controller.save(dados);
    }
    
    /**
     * Delete a target value.
     * 
     * @param id value id
     */
    public void delete(String id) {        
        LocalStorageController controller = LocalStorageController.getInstance();
        DataKey dados = controller.get(this.key);
        if(dados != null) {
            if(dados.delete(id)) {
                controller.save(dados);
            }
        }
    }
    
    /**
     * Returns the target value.
     * 
     * @param <T> value class
     * @param id value id
     * @return <code>T</code> target value
     */
    public <T> T find(String id) {
        T obj = null;
        
        LocalStorageController controller = LocalStorageController.getInstance();
        DataKey dados = controller.get(this.key);
        JsonObject json = dados.getObject(id);
        if(json != null) {
            obj = fromJson(json.get("value").getAsJsonObject());
        }
        return obj;
    }
    
    /**
     * Returns all values.
     * 
     * @param <T> value class
     * @return <code>List</code> all values
     */
    public <T> List<T> findAll() {
        List<T> ls = new ArrayList<>();
        
        LocalStorageController controller = LocalStorageController.getInstance();
        DataKey dados = controller.get(this.key);
        for(JsonObject json : dados.getAll().values()) {
            if(json != null) {
                if(json.get("value") != null) {
                    JsonObject value = json.get("value").getAsJsonObject();
                    T obj = fromJson(value);
                    ls.add(obj);
                }
            }
        }
        return ls;
    }
    
    /**
     * Convert <code>Object</code> to <code>JsonObject</code>.
     * 
     * @param obj target object
     * @return <code>JsonObject</code>
     */
    private JsonObject toJson(Object obj) {
        String as = this.g.toJson(obj);
        if(!(as.contains("{") || as.contains("}"))) {
            JsonObject json = new JsonObject();
            json.addProperty("@", as);
            return json;
        }
        return this.parser.parse(as).getAsJsonObject();
    }
    
    /**
     * Convert <code>JsonObject</code> to <code>T</code>.
     * 
     * @param <T> value class
     * @param obj target jsonobject
     * @return <code>T</code>
     */
    private <T> T fromJson(JsonObject obj) {
        if(obj.get("@") != null) {
            return (T) this.g.fromJson(obj.get("@"), this.targetClass);
        }
        return (T)this.g.fromJson(obj, this.targetClass);
    } 
    
}

