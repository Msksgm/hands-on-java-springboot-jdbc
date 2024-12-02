package com.example.hands_on_java_springboot_jdbc.infra;


import com.example.hands_on_java_springboot_jdbc.domain.ArticleRepository;
import com.example.hands_on_java_springboot_jdbc.domain.CreatedArticle;
import com.example.hands_on_java_springboot_jdbc.domain.Slug;
import org.springframework.stereotype.Repository;

@Repository
public final class ArticleRepositoryImpl implements ArticleRepository {
    @Override
    public CreatedArticle findBySlug(Slug slug) throws FindBySlugException {
        return null;
    }
}
