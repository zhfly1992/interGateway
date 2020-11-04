/*
* File name: ConfigBean.java								
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

import org.springframework.beans.factory.annotation.Value;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

public class ConfigBean {
	@ApolloConfig()
	private Config	config	= ConfigService.getConfig("00001.middleware");

	@Value("${elasticSearch.cluster.port:9300}")
	private int		port;

	@Value("${elasticSearch.cluster.host:localhost}")
	private String	host;

	@Value("${elasticSearch.cluster.name:elasticsearch}")
	private String	esName;



	@ApolloConfigChangeListener
	private void someOnChange(ConfigChangeEvent changeEvent) {
		if (changeEvent.isChanged("elasticSearch.cluster.port")) {
			port = config.getIntProperty("elasticSearch.cluster.port", 9300);
		}
		if (changeEvent.isChanged("elasticSearch.cluster.host")) {
			host = config.getProperty("elasticSearch.cluster.host", "localhost");
		}
		if (changeEvent.isChanged("elasticSearch.cluster.name")) {
			esName = config.getProperty("elasticSearch.cluster.name", "elasticsearch");
		}
	}



	public int getPort() {
		return config.getIntProperty("elasticSearch.cluster.port", 9300);
	}



	public void setPort(int port) {
		this.port = port;
	}



	public String getHost() {
		return config.getProperty("elasticSearch.cluster.host", "localhost");
	}



	public void setHost(String host) {
		this.host = host;
	}



	public String getEsName() {
		return config.getProperty("elasticSearch.cluster.name", "elasticsearch");
	}



	public void setEsName(String esName) {
		this.esName = esName;
	}

}
