package com.nfym.setting;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.application.PathManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Mapper {

    public static ArrayList<Item> arraylist=null;
    public static Path path=Paths.get(PathManager.getConfigPath(), "plugins", "Template");
    public static String filename="data.json";
    public static void load() throws IOException {
        File file = path.toFile();
        if(!file.exists()){
            file.mkdirs();
        }
        File p=path.resolve(filename).toFile();
        if(!p.exists() || p.length()==0){
            arraylist=new ArrayList<>();
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        arraylist = objectMapper.readValue(
                path.resolve(filename).toFile(),
                new TypeReference<ArrayList<Item>>() {});
        String s = objectMapper.writeValueAsString(arraylist);
    }

    public static void save() {

        try {
            ArrayList<Item> validItems = new ArrayList<>();
            for (Item item : arraylist) {
                if (!item.title.trim().isEmpty() && !item.content.isEmpty()) {
                    validItems.add(item);
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(path.resolve(filename).toFile(), validItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
