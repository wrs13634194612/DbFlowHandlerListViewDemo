package com.example.chat;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class TmpTest {
    static MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

    public static void main(String[] args) {
        multiValueMap.add("A", "1");
        multiValueMap.add("A", "2");
        multiValueMap.add("A", "3");

        multiValueMap.get("A").parallelStream().forEach(otherValue -> {

            otherValue += 1;
            System.out.println(otherValue);

        });

    }


}
