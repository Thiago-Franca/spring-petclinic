package org.springframework.samples.petclinic.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Controller
class WelcomeController extends WebMvcConfigurerAdapter {

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
                .addResolver(new VersionResourceResolver()
                        .addContentVersionStrategy(resourceProperties.getChain()
                                .getStrategy().getContent().getPaths()));
    }

    @RequestMapping("/")
    public String welcome() {
        return "welcome";
    }
}
