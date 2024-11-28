package com.example.hands_on_java_springboot_jdbc.usecase;

import com.example.hands_on_java_springboot_jdbc.domain.CreatedArticle;
import org.springframework.stereotype.Service;

@Service
public final class ShowArticleUseCaseImpl implements ShowArticleUseCase {
    @Override
    public CreatedArticle execute(String slug) throws ShowArticleUseCaseException {
        return null;
    }
}
