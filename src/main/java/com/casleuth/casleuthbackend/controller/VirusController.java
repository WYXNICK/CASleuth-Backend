package com.casleuth.casleuthbackend.controller;

import com.casleuth.casleuthbackend.entity.virus;
import com.casleuth.casleuthbackend.service.VirusService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/virus")
public class VirusController {

    @Autowired
    private VirusService virusService;
//    @GetMapping
//    public HashMap<String,Object> index() {
//
//    }
    /**
     *分页查询接口
     * 接口路径,user/page
     * @RequestParam接受 ?pageNum=1&PageSize=10
     */
    @GetMapping("/page")
    @ApiOperation(notes = "name为病毒名称，pageNum为当前页数，pageSize为每页大小", value = "根据病毒名分页查询")
    public HashMap<String,Object> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String name) {
        List<virus> virusList=virusService.selectPage(pageNum,pageSize,name);
        Integer total=virusService.selectTotalNum(name);
        HashMap<String,Object> result=new HashMap<>();
        result.put("totalNum",total);
        List<HashMap<String,Object>> virusResult = new ArrayList<>();
        for(virus v1 : virusList){
            HashMap<String,Object> virusData=new HashMap<>();
            virusData.put("virus_id",v1.getVirusId());
            virusData.put("accession",v1.getAccession());
            virusData.put("organism_name",v1.getOrganismName());
            virusData.put("isolate",v1.getIsolate());
            virusData.put("species",v1.getSpecies());
            virusData.put("family",v1.getFamily());
            virusData.put("length",v1.getLength());
            virusData.put("segment",v1.getSegment());
            virusData.put("geo_location",v1.getGeoLocation());
            virusData.put("host",v1.getHost());
            virusResult.add(virusData);
        }
        result.put("virusResult",virusResult);
        return result;
    }

    /**
     *
     * @param virus_id
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation(notes = "", value = "根据id查看某个病毒的详细信息及病毒种类")
    public HashMap<String,Object> findDetails(@RequestParam int virus_id){
        virus v1=virusService.selectVirusById(virus_id);
        HashMap<String,Object> result=new HashMap<>();
        result.put("accession",v1.getAccession());
        result.put("organism_name",v1.getOrganismName());
        result.put("isolate",v1.getIsolate());
        result.put("species",v1.getSpecies());
        result.put("family",v1.getFamily());
        result.put("length",v1.getLength());
        result.put("segment",v1.getSegment());
        result.put("geo_location",v1.getGeoLocation());
        result.put("host",v1.getHost());
        result.put("type",v1.getVirusType()+" virus");
        return result;
    }

}
