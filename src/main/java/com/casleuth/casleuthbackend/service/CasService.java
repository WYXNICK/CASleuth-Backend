package com.casleuth.casleuthbackend.service;

import com.casleuth.casleuthbackend.entity.cas_model;
import com.casleuth.casleuthbackend.mapper.CasMapper;
import com.casleuth.casleuthbackend.mapper.VirusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasService {
    @Autowired
    private CasMapper casMapper;
    public List<cas_model> findAllSeq(String accession,String type) {
        return casMapper.findAllSeq(accession,type);
    }
}
