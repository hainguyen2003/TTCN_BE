package com.example.ttcn2etest.service.news;

import com.example.ttcn2etest.model.dto.NewsDTO;
import com.example.ttcn2etest.model.etity.News;
import com.example.ttcn2etest.request.news.CreateNewsRequest;
import com.example.ttcn2etest.request.news.FilterNewsRequest;
import com.example.ttcn2etest.request.news.UpdateNewsRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface NewsService {
    List<NewsDTO> getAllNews();

    NewsDTO getByIdNews(Long id);

    NewsDTO createNews(CreateNewsRequest request);

    NewsDTO updateNews(UpdateNewsRequest request, Long id);

    NewsDTO deleteByIdNews(Long id);

    List<NewsDTO> deleteAllIdNews(List<Long> ids);

    Page<News> filterNews(FilterNewsRequest request, Date dateFrom, Date dateTo);
}
