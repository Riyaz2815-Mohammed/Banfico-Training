package com.riyaz.banficotrainingprogram.Service;

import com.riyaz.banficotrainingprogram.dto.Healthresponse;
import com.riyaz.banficotrainingprogram.dto.InfoResponse;

public interface SystemService {
    Healthresponse getHealth();
    InfoResponse getInfo();
}
