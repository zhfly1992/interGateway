/*
* File name: ESFactory.java								
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

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ESFactory {
	private static TransportClient client;

	static {
		if (client == null) {
			try {
				AppConfig appconfig = new AppConfig();
				Settings settings = Settings.builder().put("cluster.name", appconfig.javaConfigBean().getEsName())
						.put("client.transport.sniff", true).build();
				client = new PreBuiltTransportClient(settings);
				client.addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(appconfig.javaConfigBean().getHost()),
								appconfig.javaConfigBean().getPort()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	public static synchronized TransportClient getTransportClient() {
		return client;
	}

}
