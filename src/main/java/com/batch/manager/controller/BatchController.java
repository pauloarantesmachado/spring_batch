package com.batch.manager.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
@CrossOrigin
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importPersons;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV() {
        JobParameters jobParameters=new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(importPersons, jobParameters);
            while (jobExecution.isRunning()){
                System.out.println("..................");
            }
            return  ResponseEntity.ok("Process completed");
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().body("There was a problem with the process.");
        }
    }
}
