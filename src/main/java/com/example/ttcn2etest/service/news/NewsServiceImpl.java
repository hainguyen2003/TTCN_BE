package com.example.ttcn2etest.service.news;

import com.example.ttcn2etest.exception.MyCustomException;
import com.example.ttcn2etest.model.dto.NewsDTO;
import com.example.ttcn2etest.model.etity.News;
import com.example.ttcn2etest.repository.news.CustomNewsRepository;
import com.example.ttcn2etest.repository.news.NewsRepository;
import com.example.ttcn2etest.request.news.CreateNewsRequest;
import com.example.ttcn2etest.request.news.FilterNewsRequest;
import com.example.ttcn2etest.request.news.UpdateNewsRequest;
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
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final ModelMapper modelMapper;

    public NewsServiceImpl(NewsRepository newsRepository, ModelMapper modelMapper) {
        this.newsRepository = newsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<NewsDTO> getAllNews() {
        return newsRepository.findAll().stream().map(
                news -> modelMapper.map(news, NewsDTO.class)
        ).toList();
    }

    @Override
    public NewsDTO getByIdNews(Long id) {
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            return modelMapper.map(newsOptional.get(), NewsDTO.class);
        } else {
            throw new MyCustomException("Id tin tức không tồn tại trong hệ thống!");
        }
    }

    @Override
    @Transactional
    public NewsDTO createNews(CreateNewsRequest request) {
        try {
            News news = News.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .content(request.getContent())
                    .image(request.getImage())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .updateDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            news = newsRepository.saveAndFlush(news);
            return modelMapper.map(news, NewsDTO.class);
        } catch (Exception ex) {
            throw new MyCustomException("Có lỗi xảy ra trong quá trình thêm tin tức mới!");
        }
    }

    @Override
    public NewsDTO updateNews(UpdateNewsRequest request, Long id) {
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            news.setName(request.getName());
            news.setDescription(request.getDescription());
            news.setContent(request.getContent());
            news.setImage(request.getImage());
            news.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            return modelMapper.map(newsRepository.saveAndFlush(news), NewsDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trình cập nhật tin tức!");
    }

    @Override
    @Transactional
    public NewsDTO deleteByIdNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new MyCustomException("Tin tức có id: " + id + " cần xóa không tồn tại trong hệ thống!");
        }
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            newsRepository.deleteById(id);
            return modelMapper.map(newsOptional, NewsDTO.class);
        }
        throw new MyCustomException("Có lỗi xảy ra trong quá trinh xóa tin tức!");
    }

    @Override
    public List<NewsDTO> deleteAllIdNews(List<Long> ids) {
        List<NewsDTO> newsDTOS = new ArrayList<>();
        for (Long id : ids) {
            Optional<News> optionalNews = newsRepository.findById(id);
            if (optionalNews.isPresent()) {
                News news = optionalNews.get();
                newsDTOS.add(modelMapper.map(news, NewsDTO.class));
                newsRepository.delete(news);
            } else {
                throw new MyCustomException("Có lỗi xảy ra trong quá trình xóa danh sách tin tức!");
            }
        }
        return newsDTOS;
    }

    @Override
    public Page<News> filterNews(FilterNewsRequest request, Date dateFrom, Date dateTo) {
        Specification<News> specification = CustomNewsRepository.filterSpecification(dateFrom, dateTo, request);
        return newsRepository.findAll(specification, PageRequest.of(request.getStart(), request.getLimit()));
    }
}
