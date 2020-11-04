/*
* File name: DeviceShadowRestQueryController.java								
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
* 1.0			guofeilong		2018年1月18日
* ...			...			...
*
***************************************************/

package com.run.big.data.center.query.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.run.big.data.center.entity.UrlConstant;
import com.run.big.data.center.query.service.DeviceShadowRestQueryService;
import com.run.entity.common.Result;
import com.sefon.commons.ThingServiceResult;

/**
 * <class description> 设备详细信息查询
 * 
 * @author: 郭飞龙
 * @version: 1.0, 2018年1月18日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
public class DeviceShadowRestQueryController {
	@Autowired
	private DeviceShadowRestQueryService deviceShadowRestQueryService;

	/**
	 * <method description> 通过设备id查询设备状态信息(温度等)
	 *
	 * @param id
	 *            设备id
	 * @returnResult <ThingServiceResult> 自定义的HTTP StatusCode以及Body
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.FIND_DEVICE_STATE, method = RequestMethod.GET)
	public Result<ThingServiceResult> getDeviceShadow(@PathVariable("id") String id) {
		return deviceShadowRestQueryService.findDeviceShadowById(id);
	}
}
