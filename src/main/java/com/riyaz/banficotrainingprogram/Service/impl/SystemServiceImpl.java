package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Service.SystemService;
import com.riyaz.banficotrainingprogram.dto.Healthresponse;
import com.riyaz.banficotrainingprogram.dto.InfoResponse;
import com.riyaz.banficotrainingprogram.metadata.GitInfoProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
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
        InfoResponse response = new InfoResponse("Banfico",gitInfoProvider.getCommitId(),gitInfoProvider.getBranch(),"v1",gitInfoProvider.getCommitTime());

        return response ;
    }
}
