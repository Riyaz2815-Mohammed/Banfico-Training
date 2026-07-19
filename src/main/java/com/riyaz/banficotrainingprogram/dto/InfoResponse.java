package com.riyaz.banficotrainingprogram.dto;

import java.time.Instant;

public class InfoResponse {
    private String application;
    private String version;
    private String branch;
    private String commitId;
    private Instant commitTime;
//    private String commitMessage;
//    private String buildTime;

    public InfoResponse(String application, String commitId, String branch, String version, Instant commitTime) {
        this.application = application;
//        this.buildTime = buildTime;
        this.commitId = commitId;
        this.branch = branch;
        this.version = version;
//        this.commitMessage = commitMessage;
        this.commitTime = commitTime;
    }

    public String getApplication() {
        return application;
    }

//    public void setApplication(String application) {
//        this.application = application;
//    }

    public String getVersion() {
        return version;
    }

//    public void setVersion(String version) {
//        this.version = version;
//    }

    public String getBranch() {
        return branch;
    }

//    public void setBranch(String branch) {
//        this.branch = branch;
//    }

    public String getCommitId() {
        return commitId;
    }

//    public void setCommitId(String commitId) {
//        this.commitId = commitId;
//    }

    public Instant getCommitTime() {
        return commitTime;
    }

//    public void setCommitTime(String commitTime) {
//        this.commitTime = commitTime;
//    }

//    public String getCommitMessage() {
//        return commitMessage;
//    }

//    public void setCommitMessage(String commitMessage) {
//        this.commitMessage = commitMessage;
//    }

//    public String getBuildTime() {
//        return buildTime;
//    }
//
//    public void setBuildTime(String buildTime) {
//        this.buildTime = buildTime;
//    }
}
