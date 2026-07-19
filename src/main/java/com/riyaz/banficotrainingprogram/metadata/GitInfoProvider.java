package com.riyaz.banficotrainingprogram.metadata;

import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GitInfoProvider {
    private GitProperties gitProperties;
    public GitInfoProvider(GitProperties gitProperties) {
        this.gitProperties = gitProperties;

    }
    public String getBranch(){
        return gitProperties.getBranch();
    }
    public String getCommitId(){
        return gitProperties.getCommitId();
    }
    public Instant getCommitTime(){
        return gitProperties.getCommitTime();
    }
//    public String getBuildTime(){
//        return gitProperties
//    }
}
