package com.riyaz.banficotrainingprogram.system.service.impl;

import com.riyaz.banficotrainingprogram.system.dto.HealthResponse;
import com.riyaz.banficotrainingprogram.system.dto.InfoResponse;
import com.riyaz.banficotrainingprogram.system.metadata.GitInfoProvider;
import com.riyaz.banficotrainingprogram.system.service.SystemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SystemServiceImpl implements SystemService {
    private final GitInfoProvider gitInfoProvider;

    public SystemServiceImpl(GitInfoProvider gitInfoProvider) {
        this.gitInfoProvider = gitInfoProvider;
    }

    @Override
    public HealthResponse getHealth() {
        return new HealthResponse("UP", LocalDateTime.now());
    }

    @Override
    public InfoResponse getInfo() {
        return new InfoResponse("Banfico", gitInfoProvider.getCommitId(), gitInfoProvider.getBranch(), "v1", gitInfoProvider.getCommitTime());
    }
}
