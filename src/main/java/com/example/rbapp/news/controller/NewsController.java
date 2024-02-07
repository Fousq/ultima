package com.example.rbapp.news.controller;

import com.example.rbapp.jooq.codegen.tables.records.NewsRecord;
import com.example.rbapp.news.entity.News;
import com.example.rbapp.api.exception.NotFoundException;
import com.example.rbapp.news.service.NewsMapper;
import com.example.rbapp.news.service.NewsRepository;
import com.example.rbapp.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final NewsService newsService;

    @GetMapping
    public List<News> newsList(
            @RequestParam(value = "only-internal", defaultValue = "false") Boolean onlyInternal
    ) {
        return newsService.getNews(onlyInternal);
    }

    @GetMapping("/{id}")
    public News getNews(@PathVariable("id") Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::mapToEntity)
                .orElseThrow(() -> new NotFoundException("News not found"));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody News news) {
        NewsRecord newsRecord = newsMapper.mapToRecord(news);
        newsRepository.create(newsRecord);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public News update(@RequestBody News news) {
        NewsRecord newsRecord = newsMapper.mapToRecord(news);
        return newsMapper.mapToEntity(newsRepository.update(newsRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        newsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
