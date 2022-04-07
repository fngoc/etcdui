package ru.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.example.repository.EtcdRepository;

import java.util.List;

@RestController("/api")
public class DevController {

    private final Logger LOGGER = LoggerFactory.getLogger(DevController.class);

    private final EtcdRepository REPO;

    @Autowired
    public DevController(EtcdRepository repository) {
        this.REPO = repository;
    }

    @Tag(name = "DevController", description = "Эндпоинты для разработчика")
    @PostMapping("/postNewKeyInField")
    public String postNewKeyInField(String fieldName, String key, @RequestBody String value) {
        LOGGER.info("Response postNewKeyInField");
        return REPO.postNewKeyInField(fieldName, key, value);
    }

    @Tag(name = "DevController", description = "Эндпоинты для разработчика")
    @GetMapping("/getValueByFieldAndKey")
    public String getValueByFieldAndKey(String fieldName, String key) {
        LOGGER.info("Response by getValueByFieldAndKey with: " + fieldName + "/" + key);
        return REPO.getValueByFieldAndKey(fieldName, key);
    }

    @Tag(name = "DevController", description = "Эндпоинты для разработчика")
    @GetMapping("/getAllKeyInField")
    public List<String> getAllKeyInField(String fieldName) {
        LOGGER.info("Response getAllKeyInField");
        return REPO.getAllKeyInField(fieldName);
    }

    @Tag(name = "DevController", description = "Эндпоинты для разработчика")
    @GetMapping("/getAllFields")
    public String getAllFields() {
        LOGGER.info("Response getAllFields");
        return REPO.getAllFields();
    }

    @Tag(name = "DevController", description = "Эндпоинты для разработчика")
    @DeleteMapping("/deleteKeyInField")
    public String deleteKeyInField(String fieldName, String key) {
        LOGGER.info("Response postDeleteKeyInField");
        return REPO.deleteKeyInField(fieldName, key);
    }

}
