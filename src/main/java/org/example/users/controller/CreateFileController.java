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
public class CreateFileController {

    @Autowired
    private CreateFileService createFileService;

    Logger LOGGER = LogManager.getLogger(CreateFileController.class);

    public CreateFileController() {
    }

    public CreateFileController(CreateFileService createFileService) {
        this.createFileService = createFileService;
    }

    @PostMapping("files")
    public ResponseEntity<String> createFile(@RequestParam(name = "files") MultipartFile[] files, @RequestParam(name = "rfc") String rfc) throws IOException {
        String re =createFileService.createFile(files, rfc);
        if (!re.isEmpty()) {
            return new ResponseEntity<>(re,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
