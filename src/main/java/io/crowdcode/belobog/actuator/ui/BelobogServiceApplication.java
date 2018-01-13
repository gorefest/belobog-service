/*
 * Copyright 2012-2013 the original author or authors.
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

package io.crowdcode.belobog.actuator.ui;

import io.crowdcode.belobog.actuator.ui.service.GPIOService;
import io.crowdcode.belobog.actuator.ui.service.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;

@EnableAutoConfiguration
@ComponentScan()
@Controller
@EnableScheduling

public class BelobogServiceApplication {

	@Value("${application.mode}")
	ApplicationMode applicationMode;

	@RequestMapping("/foo")
	public String foo() {
		throw new RuntimeException("Expected exception in controller");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BelobogServiceApplication.class, args);
	}

	@Bean
	public SecurityProperties securityProperties() {
		SecurityProperties security = new SecurityProperties();
		security.getBasic().setPath(""); // empty so home page is unsecured
		return security;
	}


	@Bean
	public GPIOService gpioService(){
		if (applicationMode.equals(ApplicationMode.milkrun)) {
			return new GPIOFakeServiceImpl();
		} else if (applicationMode.equals(ApplicationMode.live)) {
			GPIOServiceImpl gpioService = new GPIOServiceImpl();
			gpioService.postConstruct();
			return gpioService;
		} else {
			throw new IllegalArgumentException("UNSUPPORTED MODE "+applicationMode);
		}
	}

}
