package com.casleuth.casleuthbackend.entity;

import lombok.Data;

@Data
public class fasta_file {
    String Content;
    String type;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }



}
