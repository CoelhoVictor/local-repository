package com.coelhovictor.localstorage.objs;

import com.google.gson.JsonObject;
import java.util.HashMap;

/**
 *
 * @author VictorCoelho
 */
public class DataKey {
    
    /**
     * Data key.
     */
    private final String key;
    
    /**
     * Data values.
     */
    private final HashMap<String, JsonObject> ls;
    
    /**
     * Class constructor.
     * 
     * @param k current data key
     */
    public DataKey(String k) {
        this.key = k.toLowerCase();
        this.ls = new HashMap<>();
    }
    
    /**
     * Returns the data key.
     * 
     * @return <code>String</code> data key
     */
    public String getKey() { return this.key; }
    
    /**
     * Delete a object.
     * 
     * @param id target id
     */
    public boolean delete(String id) { 
        if(this.ls.containsKey(id)) {
            this.ls.remove(id);
            return true;
        }
        return false;
    }
    
    /**
     * Returns all values.
     * 
     * @return <code>HashMap<String, JsonObject></code> all values
     */
    public HashMap<String, JsonObject> getAll() { return this.ls; }
    
    /**
     * Returns a target value.
     * 
     * @param id value id
     * @return <code>JsonObject</code> target value
     */
    public JsonObject getObject(String id) { return ls.get(id); }
    
    /**
     * Set a target value.
     * 
     * @param id target value id
     * @param obj target value
     */
    public void addObject(String id, JsonObject obj) {
        ls.put(id, obj);
    }
    
}
