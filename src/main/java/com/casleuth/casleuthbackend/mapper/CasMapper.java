package com.casleuth.casleuthbackend.mapper;

import com.casleuth.casleuthbackend.entity.cas_model;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CasMapper {
    List<cas_model> findAllSeq(String accession,String type);
}
