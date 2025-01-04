package com.example.ttcn2etest.controller;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.dto.NewsDTO;
import com.example.ttcn2etest.model.etity.News;
import com.example.ttcn2etest.request.news.CreateNewsRequest;
import com.example.ttcn2etest.request.news.FilterNewsRequest;
import com.example.ttcn2etest.request.news.UpdateNewsRequest;
import com.example.ttcn2etest.service.news.NewsService;
import com.example.ttcn2etest.utils.MyUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController extends BaseController {
    private final NewsService newsService;
    private final ModelMapper modelMapper;

    public NewsController(NewsService newsService, ModelMapper modelMapper) {
        this.newsService = newsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    ResponseEntity<?> getAllId() {
        try {
            List<NewsDTO> response = newsService.getAllNews();
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        NewsDTO response = newsService.getByIdNews(id);
        return buildItemResponse(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> createNews(@Validated @RequestBody CreateNewsRequest request) {
        NewsDTO response = newsService.createNews(request);
        return buildItemResponse(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> updateNews(@Validated @RequestBody UpdateNewsRequest request,
                                 @PathVariable("id") Long id) {
        NewsDTO response = newsService.updateNews(request, id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteById(@PathVariable Long id) {
        NewsDTO response = newsService.deleteByIdNews(id);
        return buildItemResponse(response);
    }

    @DeleteMapping("/delete/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    ResponseEntity<?> deleteAllById(@RequestBody List<Long> ids) {
        try {
            List<NewsDTO> response = newsService.deleteAllIdNews(ids);
            return buildListItemResponse(response, response.size());
        } catch (Exception ex) {
            return buildResponse();
        }
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> filter(@Validated @RequestBody FilterNewsRequest request) throws ParseException {
        Page<News> newsPage = newsService.filterNews(
                request,
                !Strings.isEmpty(request.getDateFrom()) ? MyUtils.convertDateFromString(request.getDateFrom(), DateTimeConstant.DATE_FORMAT) : null,
                !Strings.isEmpty(request.getDateTo()) ? MyUtils.convertDateFromString(request.getDateTo(), DateTimeConstant.DATE_FORMAT) : null
        );
        List<NewsDTO> newsDTOS = newsPage.getContent().stream().map(
                news -> modelMapper.map(news, NewsDTO.class)
        ).toList();
        return buildListItemResponse(newsDTOS, newsPage.getTotalElements());
    }

}
