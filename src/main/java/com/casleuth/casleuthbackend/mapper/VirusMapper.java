package com.casleuth.casleuthbackend.mapper;

import com.casleuth.casleuthbackend.entity.virus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VirusMapper {
    List<virus> selectPage(Integer pageNum, Integer pageSize, String name);

    Integer selectTotalNum(String name);

    virus selectVirusById(int virusId);

    String selectAccession(int virusId,String type);
}
