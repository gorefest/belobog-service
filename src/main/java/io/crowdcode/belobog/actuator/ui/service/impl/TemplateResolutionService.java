package io.crowdcode.belobog.actuator.ui.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class TemplateResolutionService{

    private String template;

    @Autowired
    ConfigurationService configurationService;

    @PostConstruct
    public void postConstruct() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:"+configurationService.getTemplate());
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            template = buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public static final Logger LOGGER=LoggerFactory.getLogger(TemplateResolutionService.class);

        @Autowired
        private ResourceLoader resourceLoader;

        @Autowired
        SvgService svgService;

        public void prepareImageTemplate(Model model) throws IOException {

            String template="";

            template = svgService.prepareSvg(this.template);
            model.addAttribute("svg", template);
        }
}
