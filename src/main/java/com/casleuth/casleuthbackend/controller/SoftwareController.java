package com.casleuth.casleuthbackend.controller;

import com.casleuth.casleuthbackend.entity.fasta_file;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/software")
public class SoftwareController {

    @PostMapping("/upload")
    @ApiOperation(notes = "返回.fasta文件的内容，如果上传文件不正确则返回上传文件不能为空或上传的文件不是.fasta文件", value = "上传.fasta文件,返回内容")
    public HashMap<String,Object> uploadFile(@RequestPart MultipartFile file) throws IOException {
        HashMap<String,Object> uploadContent=new HashMap<>();
        uploadContent.put("status",true);
        // 检查上传的文件是否为空
        if (file.isEmpty()) {
            uploadContent.put("content","上传的文件不能为空");
            uploadContent.put("status",false);
            return uploadContent;
        }
        // 获取上传文件的原始文件名
        String originalFilename = file.getOriginalFilename();

        // 检查文件扩展名是否为.fasta
        if (originalFilename != null && originalFilename.endsWith(".fasta")) {
            // 获取文件内容
            String fileContent = new String(file.getBytes());
            uploadContent.put("content",fileContent);
        } else {
            uploadContent.put("content","上传的文件不是.fasta文件");
            uploadContent.put("status",false);
        }
        return uploadContent;
    }

    @PostMapping("/process")
    @ApiOperation(notes = "以json形式上传.fasta文件内容（包含换行符）和模型种类，如果status为false则应在前端提示输入不正确,guide_seq表示页面下方列表中的GRNA_sequence，而show_seq表示上方一整个序列中高亮的部分，仅当type为cas12时二者互补，其余情况二者相同", value = "上传.fasta文件的内容,返回名称和序列以及处理过后得分前十的序列")
    public HashMap<String,Object> processFile(@RequestBody fasta_file file) throws IOException, InterruptedException {
        HashMap<String,Object> result=new HashMap<>();
        result.put("status",true);
        // 检查上传的文件是否为空
        if (file.getContent().isEmpty()) {
            result.put("status","false");
            result.put("errormessage","上传的文件不能为空");
            return result;
        }
        String uploadDirectory="";
        // 保存上传的文件到服务器上对应位置
        if (file.getType().equals("cas12")) {
            uploadDirectory = "/python/cas12/";
        } else if (file.getType().equals("cas9")) {
            uploadDirectory = "/python/cas9/";
        } else if(file.getType().equals("cas13a")){
            uploadDirectory = "/python/cas13a/";
        }

        String name="";
        String sequence="";
        //将文件内容拆分，获取病毒名称和序列
        int firstNewlineIndex = file.getContent().indexOf("\n");

        if (firstNewlineIndex != -1) {
            // 使用 substring 方法将字符串分成两部分
            name = file.getContent().substring(0, firstNewlineIndex); // 第一部分
            sequence = file.getContent().substring(firstNewlineIndex + 1); // 第二部分

        } else {
            result.put("errormessage","未找到换行符");
            result.put("status",false);
            return result;
        }
        // 使用 replaceAll 方法替换掉序列中的换行符
        sequence = sequence.replaceAll("\n", "");
        result.put("name",name);
        result.put("sequence",sequence);


        try {
            // 创建一个 FileWriter 对象，指定文件路径
            FileWriter fileWriter = new FileWriter(uploadDirectory+"cas.fasta",false);

            // 创建一个 BufferedWriter 对象，用于写入数据
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // 写入字符串内容
            bufferedWriter.write(file.getContent());
            // 关闭 BufferedWriter 和 FileWriter
            bufferedWriter.close();
            fileWriter.close();
            // 构建运行CAS12.py脚本的命令
            // 设置工作目录为/python/..
            ProcessBuilder processBuilder=new ProcessBuilder();
            if(file.getType().equals("cas12")) {
                processBuilder = new ProcessBuilder("python3", "CAS12.py", "cas.fasta", "cas.txt", "cas_out.txt");
            } else if (file.getType().equals("cas9")) {
                processBuilder = new ProcessBuilder("python3", "pipeline.py", "cas.fasta", "result.txt");
            } else if (file.getType().equals("cas13a")){
                processBuilder = new ProcessBuilder("python3", "model.py","cas.fasta", "output.txt");
            }

            processBuilder.directory(new File(uploadDirectory)); // 设置工作目录

            processBuilder.redirectErrorStream(true);

            // 执行命令
            Process process = processBuilder.start();
            process.waitFor();

            // 读取Python脚本的输出结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int order=1;
            List<HashMap<String,Object>> cas_result=new ArrayList<>();
            while ((line = reader.readLine()) != null) {

                // 只处理以"cas:"开头的行
                if (line.startsWith("cas:")) {
                    HashMap<String,Object> cas=new HashMap<>();
                    // 去掉行中的"cas:"部分
                    String trimmedLine = line.substring("cas:".length()).trim();
                    // 分割每行，使用制表符或空格作为分隔符
                    String[] parts = trimmedLine.split("[\\t\\s]+");
                    if (parts.length == 3) {
                        // 分别处理每个部分
                        cas.put("guide_seq",parts[0]);
                        cas.put("index",parts[1]);
                        cas.put("score",parts[2]);
                        cas.put("order",order);
                        order++;
                        int index = Integer.parseInt(parts[1]);
                        if (index != -1) {
                            // 如果找到了子字符串
                            String beforeSeq = sequence.substring(0, index); // 获取子字符串之前的所有字符串
                            String showSeq=sequence.substring(index,index+parts[0].length());
                            String afterSeq = sequence.substring(index + parts[0].length()); // 获取子字符串之后的所有字符串
                            cas.put("before_seq",beforeSeq);
                            cas.put("show_seq",showSeq);
                            cas.put("after_seq",afterSeq);
                            cas.put("percentage",(double)(beforeSeq.length())/(sequence.length()));
                        } else {
                            cas.put("status",false);
                        }
                    }
                    cas_result.add(cas);
                }

            }
            result.put("cas_result",cas_result);

        } catch (IOException e) {
            result.put("errormessage","上传失败:"+e.getMessage());
        }
         return result;
    }


}






