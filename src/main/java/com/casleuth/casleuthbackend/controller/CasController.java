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

import java.util.*;

@RestController
@RequestMapping("/api/cas")
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
        String sequence= v1.getSequence();
        List<cas_model> casList=new ArrayList<>();
        casList=casService.findAllSeq(accession,type);

        // 使用匿名Comparator对casList进行按score降序排序
        Collections.sort(casList, new Comparator<cas_model>() {
            @Override
            public int compare(cas_model cas1, cas_model cas2) {
                return Double.compare(cas2.getScore(), cas1.getScore()); //降序排序
            }
        });

        List<HashMap<String,Object>> casresult=new ArrayList<>();
        HashMap<String,Object> result=new HashMap<>();
        result.put("accession",accession);
        result.put("cas_num",casList.size());
        result.put("sequence",sequence);

        int order=1;
        for(cas_model cas : casList){
            HashMap<String,Object> cas1=new HashMap<>();
            cas1.put("guide_seq",cas.getGuideSeq());
            cas1.put("index",cas.getIndex());
            cas1.put("score",cas.getScore());
            cas1.put("order",order);
            cas1.put("model_type",type);
            int index = cas.getIndex();
            if (index <= sequence.length()) {
                // 如果找到了子字符串
                String beforeSeq = sequence.substring(0, index); // 获取子字符串之前的所有字符串
                String showSeq=sequence.substring(index,index+cas.getGuideSeq().length());
                cas1.put("show_seq",showSeq);
                String afterSeq = sequence.substring(index + cas.getGuideSeq().length()); // 获取子字符串之后的所有字符串
                cas1.put("before_seq",beforeSeq);
                cas1.put("after_seq",afterSeq);
                cas1.put("status",true);
                cas1.put("percentage",(double)(index)/(v1.getLength()));
            } else {
                cas1.put("before_seq","...");
                cas1.put("after_seq","...");

                String show_seq;
                if(cas.getModelType()=="cas12") {
                    show_seq = cas.getGuideSeq()
                            .replace('A', 'T')
                            .replace('T', 'A')
                            .replace('C', 'G')
                            .replace('G', 'C');
                }
                else{
                    show_seq= cas.getGuideSeq();
                }
                cas1.put("show_seq",show_seq);
                cas1.put("percentage",(double)(index)/(v1.getLength()));
                cas1.put("status",true);
            }
            casresult.add(cas1);
            order++;
        }
        //筛选掉完全相同的记录
        List<HashMap<String, Object>> uniqueCasResult = new ArrayList<>();
        for (HashMap<String, Object> hashMap : casresult) {
            // 检查每一个HashMap是否在uniqueCasResult中已存在
            if (!uniqueCasResult.contains(hashMap)) {
                uniqueCasResult.add(hashMap);
            }
        }
        result.put("cas_result",uniqueCasResult);
        return result;
    }
}
