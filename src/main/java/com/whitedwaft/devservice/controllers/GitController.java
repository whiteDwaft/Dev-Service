package com.whitedwaft.devservice.controllers;

import com.whitedwaft.devservice.services.GitService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class GitController {

    @Autowired
    private GitService gitService;

    @GetMapping("/clone")
    public ResponseEntity cloneRep(@RequestParam("name") String name, @RequestParam("clone") String clone_url) throws GitAPIException, IOException {
        gitService.cloneBranch(clone_url,new File(System.getProperty("user.home")+File.separator+"Downloads"+File.separator+name));
        return new ResponseEntity(HttpStatus.OK);
    }
}
