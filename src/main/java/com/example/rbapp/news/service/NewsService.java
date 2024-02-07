package com.example.rbapp.news.service;

import com.example.rbapp.jooq.codegen.tables.records.NewsRecord;
import com.example.rbapp.news.entity.News;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    public List<News> getNews(boolean includeInternal) {
        List<NewsRecord> news = includeInternal ? newsRepository.findAllInternal() : newsRepository.findAllNoneInternal();
        return newsMapper.mapToEntity(news);
    }
}
