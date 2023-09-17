package com.casleuth.casleuthbackend.controller;

import com.casleuth.casleuthbackend.entity.cas_model;
import com.casleuth.casleuthbackend.entity.virus;
import com.casleuth.casleuthbackend.service.CasService;
import com.casleuth.casleuthbackend.service.VirusService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cas")
public class CasController {
    @Autowired
    private VirusService virusService;
    @Autowired
    private CasService casService;

    /**
     *
     * @param virus_id
     * @param type
     * @return
     * 测试：virus_id=6046 type=cas13a
     */
    @GetMapping("/findAllSeq")
    @ApiOperation(notes = "序列包括guide_seq及其对应的前置序列和后置序列，同时显示得分和index,order表示该序列编号，percentage表示该序列位置占整体的百分比", value = "根据id和模型种类查看某个病毒的所有序列信息")
    public HashMap<String,Object> findAllSeq(@RequestParam int virus_id,@RequestParam String type){
        virus v1=virusService.selectVirusById(virus_id);
        String accession=v1.getAccession();
        String sequence=v1.getSequence();
        List<cas_model> casList=new ArrayList<>();
        casList=casService.findAllSeq(accession,type);
        List<HashMap<String,Object>> casresult=new ArrayList<>();
        HashMap<String,Object> result=new HashMap<>();
        result.put("accession",accession);
        result.put("cas_num",casList.size());

        int order=1;
        for(cas_model cas : casList){
            HashMap<String,Object> cas1=new HashMap<>();
            cas1.put("guide_seq",cas.getGuideSeq());
            cas1.put("index",cas.getIndex());
            cas1.put("score",cas.getScore());
            cas1.put("order",order);
            cas1.put("model_type",type);
            int index = sequence.indexOf(cas.getGuideSeq());
            if (index != -1) {
                // 如果找到了子字符串
                String beforeSeq = sequence.substring(0, index); // 获取子字符串之前的所有字符串
                String afterSeq = sequence.substring(index + cas.getGuideSeq().length()); // 获取子字符串之后的所有字符串
                cas1.put("before_seq",beforeSeq);
                cas1.put("after_seq",afterSeq);
                cas1.put("status",true);
                cas1.put("percentage",(double)(beforeSeq.length())/(sequence.length()));
            } else {
                cas1.put("status",false);
            }
            casresult.add(cas1);
        }
        result.put("cas_result",casresult);
        return result;
    }
}
