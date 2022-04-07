package ru.example.repository;

import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EtcdRepository {

    @Value("${etcd}")
    private String URL_ETCD;

    @SneakyThrows
    public String getAllFields() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        return restTemplate.exchange(RequestEntity.get(
                new URI(URL_ETCD))
                        .headers(headers)
                        .build(),
                String.class).getBody();
    }

    @SneakyThrows
    public List getAllFieldList() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        JSONParser parser = new JSONParser();
        List<String> list = new ArrayList<>();

        String response = restTemplate.exchange(RequestEntity.get(
                new URI(URL_ETCD))
                        .headers(headers)
                        .build(),
                String.class).getBody();

        JSONObject jsonObject = (JSONObject) parser.parse(response);
        JSONObject jsonObjectNode = (JSONObject) jsonObject.get("node");
        JSONArray arr = (JSONArray) jsonObjectNode.get("nodes");

        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = (JSONObject) arr.get(i);
            JSONObject jsonValueUnit = (JSONObject) parser.parse(json.toString());
            String jsonValueString = (String) jsonValueUnit.get("key");

            list.add(jsonValueString);
        }

        Collections.sort(list);
        return list;
    }

    @SneakyThrows
    public String getValueByFieldAndKey(String tableName, String key) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        return restTemplate.exchange(RequestEntity.get(
                new URI(URL_ETCD + tableName + "/" + key))
                        .headers(headers)
                        .build(),
                String.class).getBody();
    }

    @SneakyThrows
    public List getAllKeyList(String fieldName) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        JSONParser parser = new JSONParser();
        List<String> list = new ArrayList<>();

        String response = restTemplate.exchange(RequestEntity.get(
                new URI(URL_ETCD + fieldName))
                        .headers(headers)
                        .build(),
                String.class).getBody();

        JSONObject jsonObject = (JSONObject) parser.parse(response);
        JSONObject jsonObjectNode = (JSONObject) jsonObject.get("node");
        JSONArray arr = (JSONArray) jsonObjectNode.get("nodes");

        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = (JSONObject) arr.get(i);
            JSONObject jsonValueUnit = (JSONObject) parser.parse(json.toString());
            String jsonValueString = (String) jsonValueUnit.get("key");

            list.add(jsonValueString);
        }

        Collections.sort(list);
        return list;
    }

    public List<String> getAllKeyInField(String fieldName) {
        List<String> list = this.getAllKeyList(fieldName);
        List<String> result = new ArrayList<>();
        String strForAdd;

        for (String str : list) {
            strForAdd = str.split("/" + fieldName + "/")[1];
            result.add(strForAdd);
        }
        return result;
    }

    @SneakyThrows
    public String postNewKeyInField(String fieldName, String key, String value) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("value", value);
        HttpEntity<String> httpEntity = new HttpEntity(params, headers);
        return restTemplate.exchange(URL_ETCD + fieldName + "/" + key,
                HttpMethod.PUT, httpEntity, String.class).getBody();
    }

    public String deleteKeyInField(String fieldName, String key) {
        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap paramsForDelete = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity(paramsForDelete, headers);
        return restTemplate.exchange(URL_ETCD + fieldName + "/" + key,
                HttpMethod.DELETE, httpEntity, String.class).getBody();
    }
}
