package com.riyaz.banficotrainingprogram.system.service;

import com.riyaz.banficotrainingprogram.system.dto.HealthResponse;
import com.riyaz.banficotrainingprogram.system.dto.InfoResponse;

public interface SystemService {
    HealthResponse getHealth();
    InfoResponse getInfo();
}
