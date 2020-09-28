package com.whitedwaft.devservice.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Service
public class GitService {

    public void cloneBranch(String REMOTE_URL, File localPath) throws GitAPIException, IOException
    {
        Git result = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(localPath)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider( "88613a2c7197ca7146d2a6d76035bda064dd6230", "" ))
                .call();
    }
}
