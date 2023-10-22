package com.nus.dhbrand.util;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public class HttpUtil {

    //MultiValueMap  HttpEntity中的参数就是这个，表示一个键对应多个值，value是一个list集合
    public static String postRequestByUrlencoded(String url , MultiValueMap<String , String> params) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(url , HttpMethod.POST , httpEntity , String.class);
        if (response!=null && response.getStatusCode()==HttpStatus.OK){
            String body = response.getBody();
            if (body != null){
                return body;
            }
            return  null;
        }
        return null;
    }
    public static String postRequestByUrlencoded(String url , MultiValueMap<String , String> params, HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(url , HttpMethod.POST , httpEntity , String.class);
        if (response!=null && response.getStatusCode()==HttpStatus.OK){
            String body = response.getBody();
            if (body != null){
                return body;
            }
            return  null;
        }
        return null;
    }

    public static String getRequestByUrlencoded(String url , HashMap<String , Object> params){
        RestTemplate restTemplate = new RestTemplate();

        //params 请求参数拼接
        if (params!=null && params.size()>0){
                StringBuilder urlbuilder = new StringBuilder(url);
            urlbuilder.append("?");
            for (Map.Entry<String , Object> param : params.entrySet()){
                urlbuilder.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
            url = urlbuilder.substring(0 , urlbuilder.length() - 1);
        }

        String result = restTemplate.getForObject(url , String.class);

        if (result != null){
            return result;
        }
        return null;
    }


    /*****************************json*******************************/

    public static String postRequestByJson(String url , String jsonObject) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(jsonObject, headers);
        ResponseEntity<String> response = restTemplate.exchange(url , HttpMethod.POST , httpEntity , String.class);
        if (response!=null && response.getStatusCode()==HttpStatus.OK){
            String body = response.getBody();
            if (body != null){
                return body;
            }
            return  null;
        }
        return null;
    }

}
