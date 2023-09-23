package com.casleuth.casleuthbackend.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class software_result {
    List<HashMap<String,Object>> cas_result;
    String name;
    String type;
}
