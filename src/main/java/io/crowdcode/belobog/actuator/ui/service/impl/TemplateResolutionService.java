package io.crowdcode.belobog.actuator.ui.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class TemplateResolutionService{

    public static final Logger LOGGER=LoggerFactory.getLogger(TemplateResolutionService.class);

        @Autowired
        private ResourceLoader resourceLoader;

        @Autowired
        ConfigurationService configurationService;

        public void prepareImageTemplate(Model model) throws IOException {
            Resource resource = resourceLoader.getResource("classpath:"+configurationService.getTemplate());
            InputStream inputStream = resource.getInputStream();

            String template="";

            LOGGER.info("FOO");

            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
                template = buffer.lines().collect(Collectors.joining("\n"));
            }

            template = configurationService.prepareSvg(template);
            model.addAttribute("svg", template);
        }
}
