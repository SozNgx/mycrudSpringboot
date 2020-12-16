package com.javaweb.myspringcrud.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class MyImportController {
    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Autowired
    public MyImportController(JobLauncher jobLauncher, Job importUserJob) {
        this.jobLauncher = jobLauncher;
        this.importUserJob = importUserJob;
    }

    @PostMapping(value="/import/file")
    public String create(@RequestParam("file") MultipartFile multipartFile) throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        //Save multipartFile file in a temporary physical folder
        String path = new ClassPathResource("tmpuploads/").getURL().getPath();//it's assumed you have a folder called tmpuploads in the resources folder
        File fileToImport = new File(path + multipartFile.getOriginalFilename());
        OutputStream outputStream = new FileOutputStream(fileToImport);
        IOUtils.copy(multipartFile.getInputStream(), outputStream);
        outputStream.flush();
        outputStream.close();

        //Launch the Batch Job
        JobExecution jobExecution = jobLauncher.run(importUserJob, new JobParametersBuilder()
                .addString("fullPathFileName", fileToImport.getAbsolutePath())
                .toJobParameters());

        return "OK";
    }

}
