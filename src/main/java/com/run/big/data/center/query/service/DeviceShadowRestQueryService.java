/*
 * File name: DeviceShadowRestQueryService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2018年1月18日 ...
 * ... ...
 *
 ***************************************************/

package com.run.big.data.center.query.service;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.run.big.data.center.entity.LogConstants;
import com.run.entity.common.Result;
import com.run.entity.tool.ResultBuilder;
import com.sefon.commons.ThingServiceResult;
import com.sefon.read.ShadowReadService;

/**
 * <class description> 设备当前状态信息查询
 * 
 * @author: 郭飞龙
 * @version: 1.0, 2018年1月18日
 */
@Service
public class DeviceShadowRestQueryService {
	private static final Logger	logger	= Logger.getLogger(DeviceShadowRestQueryService.class);

	@Autowired
	private ShadowReadService	shadowReadService;



	/**
	 * <method description> 通过设备id查询设备状态信息(温度等)
	 *
	 * @param deviceId
	 *            设备id
	 * @returnResult <ThingServiceResult> 自定义的HTTP StatusCode以及Body
	 */

	public Result<ThingServiceResult> findDeviceShadowById(String deviceId) {
		logger.info(String.format("[findDeviceShadowById()->request params:deviceId:%s]", deviceId));
		try {
			if (StringUtils.isBlank(deviceId)) {
				logger.error(LogConstants.NO_PARAMETER_EXISTS);
				return ResultBuilder.emptyResult();
			}
			ThingServiceResult deviceShadow = shadowReadService.getThingShadow(deviceId);
			int status = deviceShadow.getStatus();
			// 查询成功,返回状态码200
			if (status == HttpStatus.SC_OK) {
				logger.info(LogConstants.QUERY_SUCCESS);
				return ResultBuilder.successResult(deviceShadow, "");
			} else {
				logger.error(deviceShadow.getMessage());
				return ResultBuilder.failResult(deviceShadow.getMessage());
			}
		} catch (Exception e) {
			logger.error("findDeviceShadowById()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}

	}

}
