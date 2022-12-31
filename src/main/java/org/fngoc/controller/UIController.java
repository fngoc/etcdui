package org.fngoc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.fngoc.model.Search;
import org.fngoc.repository.EtcdRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UIController {

    private final Logger LOGGER = LoggerFactory.getLogger(UIController.class);

    private final EtcdRepository repository;

    @Autowired
    public UIController(EtcdRepository repository) {
        this.repository = repository;
    }

    @Tag(name = "UIController", description = "Эндпоинты для наблюдателя")
    @GetMapping("/")
    public ModelAndView main(Model model) {
        Map<String, Object> map = new HashMap();

        map.put("result", repository.getAllFields());
        map.put("arrayTable", repository.getAllFieldList());

        model.addAllAttributes(map);
        model.addAttribute("search", new Search());
        return new ModelAndView("index");
    }

    @Tag(name = "UIController", description = "Эндпоинты для наблюдателя")
    @SneakyThrows
    @PostMapping("/search")
    public void search(@ModelAttribute Search search, HttpServletResponse response) {
        response.sendRedirect("/search/" + search.getFieldName() + "/" + search.getKey());
    }

    @Tag(name = "UIController", description = "Эндпоинты для наблюдателя")
    @SneakyThrows
    @GetMapping("/search/{fieldName}/{key}")
    public ModelAndView search(@PathVariable String fieldName, @PathVariable String key, Model model) {
        Map<String, Object> map = new HashMap();
        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser.parse(repository.getValueByFieldAndKey(fieldName, key));
        JSONObject jsonInside = (JSONObject) parser.parse(json.get("node").toString());
        JSONObject jsonValue = (JSONObject) parser.parse(jsonInside.get("value").toString());

        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonValue);

        map.put("node", json.get("node"));
        map.put("key", jsonInside.get("key"));
        map.put("value", jsonObject.toString(2));
        map.put("result", json);

        model.addAllAttributes(map);
        LOGGER.info("Response key: " + jsonInside.get("key"));
        return new ModelAndView("search");
    }
}
