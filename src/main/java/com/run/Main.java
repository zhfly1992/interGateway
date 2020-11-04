package com.run;

/*
 * File name: Main.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 qulong 2017年5月25日 ... ...
 * ...
 *
 ***************************************************/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

/**
 * @Description:
 * @author: qulong
 * @version: 1.0, 2017年5月25日
 */
@SpringBootApplication
@EnableEurekaClient
@Configuration
@EnableApolloConfig(value = { "00001.common", " 00001.middleware" }, order = 1)
@ImportResource("classpath:config/spring-context.xml")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
