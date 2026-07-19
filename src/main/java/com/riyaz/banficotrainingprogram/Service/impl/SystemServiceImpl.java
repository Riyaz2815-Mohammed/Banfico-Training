package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Service.SystemService;
import com.riyaz.banficotrainingprogram.dto.Healthresponse;
import com.riyaz.banficotrainingprogram.dto.InfoResponse;
import com.riyaz.banficotrainingprogram.metadata.GitInfoProvider;

import java.time.LocalDateTime;

public class SystemServiceImpl implements SystemService {
    private final GitInfoProvider gitInfoProvider;

    public SystemServiceImpl(GitInfoProvider gitInfoProvider) {
        this.gitInfoProvider = gitInfoProvider;
    }

    @Override
    public Healthresponse getHealth() {
        Healthresponse response = new Healthresponse("UP", LocalDateTime.now());
        return response;
    }

    @Override
    public InfoResponse getInfo() {
        InfoResponse response = new InfoResponse("Banfico",gitInfoProvider.getCommitId(),gitInfoProvider.getBranch(),"v1",gitInfoProvider.getC
        return new InfoResponse(
                "Mini Banking System",
                "1.0.0",
                gitInfoProvider.getBranch(),
                gitInfoProvider.getCommitId(),
                gitInfoProvider.get(),
                gitInfoProvider.getCommitMessage(),
                gitInfoProvider.getBuildTime()
        );
        return ;
    }
}
