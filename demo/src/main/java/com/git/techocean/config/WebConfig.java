package com.git.techocean.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.method.HandlerTypePredicate;

/**
 * Integra o front embutido (static/) com a API:
 * - todos os controllers ficam sob /api (o front chama /api/...);
 * - rotas do SPA (/dashboard, /cadastro...) devolvem index.html em vez de 404.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", HandlerTypePredicate.forBasePackage("com.git.techocean.controller"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requested = location.createRelative(resourcePath);
                        if (requested.exists() && requested.isReadable()) return requested;
                        boolean arquivo = resourcePath.contains(".");
                        if (arquivo || resourcePath.startsWith("api/")) return null;
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}
