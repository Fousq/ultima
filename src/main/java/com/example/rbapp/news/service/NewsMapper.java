package com.example.rbapp.news.service;

import com.example.rbapp.config.AppMapperConfig;
import com.example.rbapp.jooq.codegen.tables.records.NewsRecord;
import com.example.rbapp.news.entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = AppMapperConfig.class)
public interface NewsMapper {

    News mapToEntity(NewsRecord news);

    List<News> mapToEntity(List<NewsRecord> news);

    NewsRecord mapToRecord(News news);
}
