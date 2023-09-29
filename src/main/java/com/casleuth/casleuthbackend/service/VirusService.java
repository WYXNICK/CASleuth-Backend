package com.casleuth.casleuthbackend.service;

import com.casleuth.casleuthbackend.entity.virus;
import com.casleuth.casleuthbackend.mapper.VirusMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VirusService {

    @Autowired
    private VirusMapper virusMapper;
   public List selectPage(Integer pageNum, Integer pageSize, String name){
       Integer start=(pageNum-1)*pageSize;
       return virusMapper.selectPage(start, pageSize, name);
   }

    public Integer selectTotalNum(String name) {
       return virusMapper.selectTotalNum(name);
    }

    public virus selectVirusById(int virusId) {
       return virusMapper.selectVirusById(virusId);
    }

    public String selectAccession(int virusId,String type) {
       return virusMapper.selectAccession(virusId,type);
    }

    public List<virus> selectAllVirusByName(String name) {
       return virusMapper.selectAllVirusByName(name);
    }

    public List<virus> selectSomeVirusByName(String name, int num) {
       return virusMapper.selectSomeVirusByName(name,num);
    }
}
