package com.example.hands_on_java_springboot_jdbc.infra;


import com.example.hands_on_java_springboot_jdbc.domain.ArticleRepository;
import com.example.hands_on_java_springboot_jdbc.domain.Body;
import com.example.hands_on_java_springboot_jdbc.domain.CreatedArticle;
import com.example.hands_on_java_springboot_jdbc.domain.Slug;
import com.example.hands_on_java_springboot_jdbc.domain.Title;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    ArticleRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public CreatedArticle findBySlug(Slug slug) throws FindBySlugException {
        final String sql ="""
               SELECT
                   articles.slug
                   , articles.title
                   , articles.body
               FROM
                   articles
               WHERE
                   slug = :slug
               """;
        final List<Map<String, Object>> articleMapList = this.namedParameterJdbcTemplate.queryForList(sql, new MapSqlParameterSource().addValue("slug", slug.getValue()));
        if (articleMapList.isEmpty()) {
            throw new ArticleNotFound(slug);
        }

        final Map<String, Object> articleMap = articleMapList.getFirst();

        return CreatedArticle.newWithoutValidation(
            Slug.newWithoutValidation((String) articleMap.get("slug")),
            Title.newWithoutValidation((String) articleMap.get("title")),
            Body.newWithoutValidation((String) articleMap.get("body"))
        );
    }
}
