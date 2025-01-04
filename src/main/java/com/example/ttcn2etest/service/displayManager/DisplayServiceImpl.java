package com.example.ttcn2etest.service.displayManager;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.DisplayManagerDTO;
import com.example.ttcn2etest.model.etity.DisplayManager;
import com.example.ttcn2etest.repository.displayManager.CustomDisplayManagerRepository;
import com.example.ttcn2etest.repository.displayManager.DisplayManagerRepository;
import com.example.ttcn2etest.request.displayManager.CreateDisplayRequest;
import com.example.ttcn2etest.request.displayManager.FilterDisplayRequest;
import com.example.ttcn2etest.request.displayManager.UpdateDisplayRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DisplayServiceImpl implements DisplayService {
    private final DisplayManagerRepository displayManagerRepository;
    private final ModelMapper modelMapper;

    public DisplayServiceImpl(DisplayManagerRepository displayManagerRepository, ModelMapper modelMapper) {
        this.displayManagerRepository = displayManagerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DisplayManagerDTO> getAllDisplayManager() {
        return displayManagerRepository.findAll().stream().map(
                displayManager -> modelMapper.map(displayManager, DisplayManagerDTO.class)
        ).toList();
    }

    @Override
    public DisplayManagerDTO getByIdDisplay(Long id) {
        Optional<DisplayManager> displayManagerOptional = displayManagerRepository.findById(id);
        if (displayManagerOptional.isPresent()) {
            return modelMapper.map(displayManagerOptional.get(), DisplayManagerDTO.class);
        } else {
            throw new MyCustomException("Id không tồn tại trong hệ thống!");
        }
    }

    @Override
    public DisplayManagerDTO createDisplay(CreateDisplayRequest request) {
        try {
        DisplayManager displayManager = DisplayManager.builder()
                .title(request.getTitle())
                .image(request.getImage())
                .description(request.getDescription())
                .location(request.getLocation())
                .type(request.getType())
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .build();
        displayManager = displayManagerRepository.saveAndFlush(displayManager);
        return modelMapper.map(displayManager, DisplayManagerDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm mới!" + ex.getMessage());
        }
    }

    @Override
    public DisplayManagerDTO updateDisplay(UpdateDisplayRequest request, Long id) {
        Optional<DisplayManager> displayManagerOptional = displayManagerRepository.findById(id);
        if (displayManagerOptional.isPresent()) {
            DisplayManager displayManager = displayManagerOptional.get();
            displayManager.setTitle(request.getTitle());
            displayManager.setImage(request.getImage());
            displayManager.setDescription(request.getDescription());
            displayManager.setLocation(request.getLocation());
            displayManager.setType(request.getType());
            displayManager.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(displayManagerRepository.saveAndFlush(displayManager), DisplayManagerDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhât!");
    }

    @Override
    @Transactional
    public DisplayManagerDTO deleteByIdDisplay(Long id) {
        if (!displayManagerRepository.existsById(id)) {
            throw new MyCustomException("Id: " + id + " cần xóa không tồn tại trong hệ thống!");
        }
        Optional<DisplayManager> displayManagerOptional = displayManagerRepository.findById(id);
        if (displayManagerOptional.isPresent()) {
            displayManagerRepository.deleteById(id);
            return modelMapper.map(displayManagerOptional, DisplayManagerDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trinh xóa!");
    }

    //317ms => xóa 3 ptu
    @Override
    @Transactional
    public List<DisplayManagerDTO> deleteAllDisplay(List<Long> ids) {
        List<DisplayManagerDTO> displayManagerDTOS = new ArrayList<>();
        for (DisplayManager displayManager : displayManagerRepository.findAllById(ids)) {
            displayManagerDTOS.add(modelMapper.map(displayManager, DisplayManagerDTO.class));
        }
        displayManagerRepository.deleteAllByIdInBatch(ids);
        return displayManagerDTOS;
    }


    //sử dụng các này thì thời gian xóa sẽ nhanh hơn 48ms => xóa 3 ptu
    @Override
    public List<DisplayManagerDTO> deleteAllDisplayManager(List<Long> ids) {
        List<DisplayManagerDTO> displayManagerDTOS = new ArrayList<>();
        for (Long id : ids) {
            Optional<DisplayManager> displayManagerOptional = displayManagerRepository.findById(id);
            if (displayManagerOptional.isPresent()) {
                DisplayManager displayManager = displayManagerOptional.get();
                displayManagerDTOS.add(modelMapper.map(displayManager, DisplayManagerDTO.class));
                displayManagerRepository.delete(displayManager);
            } else {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa danh sách các màn hiển thị!");
            }
        }
        return displayManagerDTOS;
    }

    @Override
    public Page<DisplayManager> filterDisplay(FilterDisplayRequest request, Date dateFrom, Date dateTo) {
        Specification<DisplayManager> specification = CustomDisplayManagerRepository.filterSpecification(dateFrom, dateTo, request);
        return displayManagerRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }
}
