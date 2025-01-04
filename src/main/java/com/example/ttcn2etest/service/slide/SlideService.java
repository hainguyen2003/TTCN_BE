package com.example.ttcn2etest.service.slide;

import com.example.ttcn2etest.model.dto.SlideDTO;
import com.example.ttcn2etest.model.etity.Slide;
import com.example.ttcn2etest.request.slide.CreateSlideRequest;
import com.example.ttcn2etest.request.slide.FilterSlideRequest;
import com.example.ttcn2etest.request.slide.UpdateSlideRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface SlideService {
    List<SlideDTO> getAllSlide();

    SlideDTO getById(Long id);

    SlideDTO createSlide(CreateSlideRequest request);

    SlideDTO update(UpdateSlideRequest request, Long id);

    SlideDTO deleteByIdService(Long id);

    List<SlideDTO> deleteAllId(List<Long> ids);

    Page<Slide> filterService(FilterSlideRequest request, Date dateFrom, Date dateTo);

}
