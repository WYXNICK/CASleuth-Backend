package com.casleuth.casleuthbackend.controller;

import com.casleuth.casleuthbackend.entity.cas_model;
import com.casleuth.casleuthbackend.entity.software_result;
import com.casleuth.casleuthbackend.entity.virus;
import com.casleuth.casleuthbackend.service.CasService;
import com.casleuth.casleuthbackend.service.VirusService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExcelController {

    @Autowired
    private VirusService virusService;

    @Autowired
    private CasService casService;


    @GetMapping("/virus-to-excel")
    public void virusExportToExcel(HttpServletResponse response, @RequestParam int virus_id) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // 创建数据
        virus v1 =virusService.selectVirusById(virus_id);

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Accession");
        headerRow.createCell(1).setCellValue("Organism_Name");
        headerRow.createCell(2).setCellValue("Isolate");
        headerRow.createCell(3).setCellValue("Species");
        headerRow.createCell(4).setCellValue("Family");
        headerRow.createCell(5).setCellValue("Length");
        headerRow.createCell(6).setCellValue("Segment");
        headerRow.createCell(7).setCellValue("Geo_Location");
        headerRow.createCell(8).setCellValue("Host");
        headerRow.createCell(9).setCellValue("Sequence");
        headerRow.createCell(10).setCellValue("Type");

        // 填充数据行
        int rowNum = 1;
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(v1.getAccession());
        row.createCell(1).setCellValue(v1.getOrganismName());
        row.createCell(2).setCellValue(v1.getIsolate());
        row.createCell(3).setCellValue(v1.getSpecies());
        row.createCell(4).setCellValue(v1.getFamily());
        row.createCell(5).setCellValue(v1.getLength());
        row.createCell(6).setCellValue(v1.getSegment());
        row.createCell(7).setCellValue(v1.getGeoLocation());
        row.createCell(8).setCellValue(v1.getHost());
        row.createCell(9).setCellValue(v1.getSequence());
        row.createCell(10).setCellValue(v1.getVirusType());
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+v1.getAccession()+".xlsx");

        // 将工作簿写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     *
     * @param response
     * @param virus_ids
     * @throws IOException
     * 请求参数书写方式:http://localhost:9090/virusList-to-excel?virus_ids=120&virus_ids=2&virus_ids=3
     */
    @GetMapping("/virusList-to-excel")
    public void virusListExportToExcel(HttpServletResponse response, @RequestParam int[] virus_ids) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");



        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Accession");
        headerRow.createCell(1).setCellValue("Organism_Name");
        headerRow.createCell(2).setCellValue("Isolate");
        headerRow.createCell(3).setCellValue("Species");
        headerRow.createCell(4).setCellValue("Family");
        headerRow.createCell(5).setCellValue("Length");
        headerRow.createCell(6).setCellValue("Segment");
        headerRow.createCell(7).setCellValue("Geo_Location");
        headerRow.createCell(8).setCellValue("Host");
        headerRow.createCell(9).setCellValue("Sequence");
        headerRow.createCell(10).setCellValue("Type");
        int rowNum = 1;
        for(int vid : virus_ids) {
            // 创建数据
            virus v1 = virusService.selectVirusById(vid);
            // 填充数据行
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(v1.getAccession());
            row.createCell(1).setCellValue(v1.getOrganismName());
            row.createCell(2).setCellValue(v1.getIsolate());
            row.createCell(3).setCellValue(v1.getSpecies());
            row.createCell(4).setCellValue(v1.getFamily());
            row.createCell(5).setCellValue(v1.getLength());
            row.createCell(6).setCellValue(v1.getSegment());
            row.createCell(7).setCellValue(v1.getGeoLocation());
            row.createCell(8).setCellValue(v1.getHost());
            row.createCell(9).setCellValue(v1.getSequence());
            row.createCell(10).setCellValue(v1.getVirusType());
        }
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=virus_list.xlsx");

        // 将工作簿写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/virusList-to-excel/all")
    public void virusListAllExportToExcel(HttpServletResponse response, @RequestParam String name) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Accession");
        headerRow.createCell(1).setCellValue("Organism_Name");
        headerRow.createCell(2).setCellValue("Isolate");
        headerRow.createCell(3).setCellValue("Species");
        headerRow.createCell(4).setCellValue("Family");
        headerRow.createCell(5).setCellValue("Length");
        headerRow.createCell(6).setCellValue("Segment");
        headerRow.createCell(7).setCellValue("Geo_Location");
        headerRow.createCell(8).setCellValue("Host");
        headerRow.createCell(9).setCellValue("Sequence");
        headerRow.createCell(10).setCellValue("Type");
        int rowNum = 1;
        List<virus> virusList=virusService.selectAllVirusByName(name);
        for(virus v1 : virusList) {
            // 填充数据行
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(v1.getAccession());
            row.createCell(1).setCellValue(v1.getOrganismName());
            row.createCell(2).setCellValue(v1.getIsolate());
            row.createCell(3).setCellValue(v1.getSpecies());
            row.createCell(4).setCellValue(v1.getFamily());
            row.createCell(5).setCellValue(v1.getLength());
            row.createCell(6).setCellValue(v1.getSegment());
            row.createCell(7).setCellValue(v1.getGeoLocation());
            row.createCell(8).setCellValue(v1.getHost());
            row.createCell(9).setCellValue(v1.getSequence());
            row.createCell(10).setCellValue(v1.getVirusType());
        }
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=virus_list_all.xlsx");

        // 将工作簿写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/virusList-to-excel/some")
    public void virusListSomeExportToExcel(HttpServletResponse response, @RequestParam String name,@RequestParam int num) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Accession");
        headerRow.createCell(1).setCellValue("Organism_Name");
        headerRow.createCell(2).setCellValue("Isolate");
        headerRow.createCell(3).setCellValue("Species");
        headerRow.createCell(4).setCellValue("Family");
        headerRow.createCell(5).setCellValue("Length");
        headerRow.createCell(6).setCellValue("Segment");
        headerRow.createCell(7).setCellValue("Geo_Location");
        headerRow.createCell(8).setCellValue("Host");
        headerRow.createCell(9).setCellValue("Sequence");
        headerRow.createCell(10).setCellValue("Type");
        int rowNum = 1;
        List<virus> virusList=virusService.selectSomeVirusByName(name,num);
        for(virus v1 : virusList) {
            // 填充数据行
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(v1.getAccession());
            row.createCell(1).setCellValue(v1.getOrganismName());
            row.createCell(2).setCellValue(v1.getIsolate());
            row.createCell(3).setCellValue(v1.getSpecies());
            row.createCell(4).setCellValue(v1.getFamily());
            row.createCell(5).setCellValue(v1.getLength());
            row.createCell(6).setCellValue(v1.getSegment());
            row.createCell(7).setCellValue(v1.getGeoLocation());
            row.createCell(8).setCellValue(v1.getHost());
            row.createCell(9).setCellValue(v1.getSequence());
            row.createCell(10).setCellValue(v1.getVirusType());
        }
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=virus_list_"+num+".xlsx");

        // 将工作簿写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/cas-to-excel")
    public void casExportToExcel(HttpServletResponse response, @RequestParam int virus_id,@RequestParam String type) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        virus v1=virusService.selectVirusById(virus_id);
        String accession=v1.getAccession();
        List<cas_model> casList=new ArrayList<>();
        casList=casService.findAllSeq(accession,type);

        //筛选掉完全相同元素
        List<cas_model> uniqueCasList = new ArrayList<>();

        for (cas_model cas : casList) {
            // 检查当前HashMap是否在uniqueCasResult中已存在
            if (!uniqueCasList.contains(cas)) {
                uniqueCasList.add(cas);
            }
        }
        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("fasta_id");
        headerRow.createCell(1).setCellValue("guide_seq");
        headerRow.createCell(2).setCellValue("index");
        headerRow.createCell(3).setCellValue("score");
        headerRow.createCell(4).setCellValue("model_type");

        // 填充数据行
        int rowNum = 1;

        for(cas_model cas : uniqueCasList){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cas.getFastaId());
            row.createCell(1).setCellValue(cas.getGuideSeq());
            row.createCell(2).setCellValue(cas.getIndex());
            row.createCell(3).setCellValue(cas.getScore());
            row.createCell(4).setCellValue(cas.getModelType());
        }
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+v1.getAccession()+"_"+type+".xlsx");

        // 将工作簿写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    @PostMapping("/software-to-excel")
    public void softwareExportToExcel(HttpServletResponse response, @RequestBody software_result s_result) throws IOException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        //筛选掉完全相同元素
        List<cas_model> uniqueCasList = new ArrayList<>();

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("fasta_id");
        headerRow.createCell(1).setCellValue("order");
        headerRow.createCell(2).setCellValue("guide_seq");
        headerRow.createCell(3).setCellValue("index");
        headerRow.createCell(4).setCellValue("score");
        headerRow.createCell(5).setCellValue("model_type");

        // 填充数据行
        int rowNum = 1;

        for(HashMap<String,Object> cas : s_result.getCas_result()){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(s_result.getName().substring(1, 12));
            row.createCell(1).setCellValue(cas.get("order").toString());
            row.createCell(2).setCellValue(cas.get("guide_seq").toString());
            row.createCell(3).setCellValue(cas.get("index").toString());
            row.createCell(4).setCellValue(cas.get("score").toString());
            row.createCell(5).setCellValue(s_result.getType());
        }
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+s_result.getName().substring(1, 12)+"_"+s_result.getType()+".xlsx");

        // 将工作簿写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
