package com.riyaz.banficotrainingprogram.system.dto;

import java.time.Instant;

public class InfoResponse {
    private String application;
    private String version;
    private String branch;
    private String commitId;
    private Instant commitTime;

    public InfoResponse(String application, String commitId, String branch, String version, Instant commitTime) {
        this.application = application;
        this.commitId = commitId;
        this.branch = branch;
        this.version = version;
        this.commitTime = commitTime;
    }

    public String getApplication() { return application; }
    public String getVersion() { return version; }
    public String getBranch() { return branch; }
    public String getCommitId() { return commitId; }
    public Instant getCommitTime() { return commitTime; }
}
