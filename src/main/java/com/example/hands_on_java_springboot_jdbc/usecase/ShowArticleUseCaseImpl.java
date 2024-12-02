package com.example.hands_on_java_springboot_jdbc.usecase;

import com.example.hands_on_java_springboot_jdbc.domain.ArticleRepository;
import com.example.hands_on_java_springboot_jdbc.domain.CreatedArticle;
import com.example.hands_on_java_springboot_jdbc.domain.Slug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ShowArticleUseCaseImpl implements ShowArticleUseCase {
    private final ArticleRepository articleRepository;

    public ShowArticleUseCaseImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public CreatedArticle execute(String slug) throws ShowArticleUseCaseException {

        // Slug のバリデーションするときに、try のスコープ外なので、変数を一時的に置く必要がありそう
        // この Slug を nullable のまま利用するのはしんどいので、Optional で wrap するか、Repository 内で null のバリデーションが必要？
        Slug validatedSlug;
        try {
            validatedSlug = Slug.newSlug(slug);
        } catch (Slug.CreationErrorException e) {
            throw new ShowArticleUseCase.CreatedArticleValidationErrors(e, e.getMessage());
        }

        try {
            return this.articleRepository.findBySlug(validatedSlug);
        } catch (ArticleRepository.FindBySlugException e) {
            throw switch (e) {
                case ArticleRepository.ArticleNotFound ignored -> new ShowArticleUseCase.NotFoundArticleBySlug(e.getMessage());
            };
        }
    }
}
