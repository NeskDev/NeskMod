package fr.neskuik.mod.listeners;

import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlWriter {
    public void writeYaml() {
        Yaml yaml = new Yaml();
        Map<String, Object> data = new HashMap<>();

        data.put("exampleKey", "exampleValue");

        FileWriter writer;
        try {
            writer = new FileWriter("sanctions.yml");
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
