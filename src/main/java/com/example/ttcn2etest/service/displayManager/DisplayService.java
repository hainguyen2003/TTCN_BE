package com.example.ttcn2etest.service.displayManager;

import com.example.ttcn2etest.model.dto.DisplayManagerDTO;
import com.example.ttcn2etest.model.etity.DisplayManager;
import com.example.ttcn2etest.request.displayManager.CreateDisplayRequest;
import com.example.ttcn2etest.request.displayManager.FilterDisplayRequest;
import com.example.ttcn2etest.request.displayManager.UpdateDisplayRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface DisplayService {
    List<DisplayManagerDTO> getAllDisplayManager();

    DisplayManagerDTO getByIdDisplay(Long id);

    DisplayManagerDTO createDisplay(CreateDisplayRequest request);

    DisplayManagerDTO updateDisplay(UpdateDisplayRequest request, Long id);

    DisplayManagerDTO deleteByIdDisplay(Long id);

    List<DisplayManagerDTO> deleteAllDisplay(List<Long> ids);

    List<DisplayManagerDTO> deleteAllDisplayManager(List<Long> ids);

    Page<DisplayManager> filterDisplay(FilterDisplayRequest request, Date dateFrom, Date dateTo);


}
