/*
* File name: AppConfig.java								
*
* Purpose:
*
* Functions used and called:	
* Name			Purpose
* ...			...
*
* Additional Information:
*
* Development History:
* Revision No.	Author		Date
* 1.0			田明		2018年1月16日
* ...			...			...
*
***************************************************/
package com.run.big.data.center.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

@Configuration
@EnableApolloConfig
public class AppConfig {
	@Bean
	public ConfigBean javaConfigBean() {
		return new ConfigBean();
	}
}
