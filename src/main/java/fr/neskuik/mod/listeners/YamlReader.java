package fr.neskuik.mod.listeners;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlReader {
    public void readYaml() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("sanctions.yml");
        Map<String, Object> obj = (Map<String, Object>) yaml.load(inputStream);
        System.out.println(obj);
    }
}