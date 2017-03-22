/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * PetClinic Spring Boot Application.
 * 
 * @author Dave Syer
 *
 */
@SpringBootApplication
public class PetClinicApplication extends WebMvcConfigurerAdapter {

    @Autowired
    private ResourceProperties resourceProperties;

    @Autowired
    private WebMvcProperties mvcProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Integer cachePeriod = this.resourceProperties.getCachePeriod();
        ResourceProperties.Chain properties = this.resourceProperties.getChain();
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(cachePeriod).resourceChain(properties.isCache());
        registry.addResourceHandler(this.mvcProperties.getStaticPathPattern())
                .addResourceLocations(this.resourceProperties.getStaticLocations())
                .setCachePeriod(cachePeriod).resourceChain(properties.isCache())
                .addResolver(getResourceResolver());
    }

    private VersionResourceResolver getResourceResolver() {
        VersionResourceResolver resolver = new VersionResourceResolver();
        if (resourceProperties.getChain().getStrategy().getContent().isEnabled()) {
            String[] paths = resourceProperties.getChain().getStrategy().getContent()
                    .getPaths();
            resolver.addContentVersionStrategy(paths);
        }
        return resolver;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PetClinicApplication.class, args);
    }

}
