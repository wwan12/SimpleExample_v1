package com.aisino.independentmodule;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2017/12/6.
 */

public class ModuleManager {
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    private HashMap<Integer,Object> saveData;

    private static ModuleManager mapData;
    public final static int MODULE_CALLBACK = 101;

    private ModuleManager() {
        saveData = new HashMap();
    }

    public static ModuleManager getMap() {
        if (mapData == null) {
            mapData= new ModuleManager();
        }
        return mapData;
    }

    public void save(int key, Object val) {
        saveData.put(key, val);
    }

    public Object get(int key) {
        return saveData.get(key);
    }

    public void remove(int key) {
        saveData.remove(key);
    }

    public void addEvent(Runnable runnable){
        fixedThreadPool.execute(runnable);
    }
}
