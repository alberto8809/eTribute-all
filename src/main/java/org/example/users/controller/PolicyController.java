package org.example.users.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.users.model.Response;
import org.example.users.service.CreateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@ResponseBody
@RequestMapping("/")
public class PolicyController {

    @Autowired
    private CreateFileService createFileService;

    Logger LOGGER = LogManager.getLogger(PolicyController.class);

    public PolicyController() {
    }

    public PolicyController(CreateFileService createFileService) {
        this.createFileService = createFileService;
    }


    @PostMapping("policy/")
    public ResponseEntity<Map<String, List<Response>>> getDescription(@RequestParam(name = "rfc") String rfc) {
        Map<String, List<Response>> descriptions = createFileService.getDescriptionPolicy(rfc);
        if (!descriptions.isEmpty()) {
            return new ResponseEntity<>(descriptions, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    @GetMapping("policy/{rfc}/{fileName}")
    public ResponseEntity<HttpStatus> createFileByNameOfFile(@PathVariable(name = "rfc") String rfc, @PathVariable(name = "fileName") String fileName) {
        if (createFileService.createPolicyByFileName(rfc, fileName)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    @GetMapping("policies/{rfc}/{account_id}")
    public ResponseEntity<HttpStatus> createPolicy(@PathVariable(name = "rfc") String rfc, @PathVariable(name = "account_id") int account_id) {
        if (createFileService.createPolicy(rfc, account_id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    @PostMapping("policy/upload")
    public ResponseEntity<HttpStatus> uploadFilesToS3(@RequestParam(name = "files") MultipartFile[] files, @RequestParam(name = "rfc") String rfc) throws IOException {
        if (createFileService.uploadToS3(files, rfc)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


}
