/*
* File name: DeviceQueryRestController.java								
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
package com.run.big.data.center.query.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.run.big.data.center.entity.UrlConstant;
import com.run.big.data.center.query.service.DeviceQueryRestService;
import com.run.entity.common.Result;

/**
 * @Description:
 * @author: 田明
 * @version: 1.0, 2018年01月16日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
public class DeviceQueryRestController {

	@Autowired
	private DeviceQueryRestService deviceQueryRestService;



	/**
	 * @Description: 根据设备id查询设备信息
	 * @param id 设备id
	 * @return 设备信息
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.QUERY_DEVICEINFO, method = RequestMethod.GET)
	public Result<Map<String, Object>> queryDeviceInfoById(@PathVariable String id) {
		return deviceQueryRestService.queryDeviceInfoById(id);
	}



	/**
	 * @Description: 根据用户id查询设备集合信息
	 * @param userId
	 *            用户id
	 * @param pageNum
	 *            页码
	 * @param pageSize
	 *            分页大小
	 * @return 设备集合（分页）
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.QUERY_DEVICELIST, method = RequestMethod.GET)
	public Result<PageInfo<Map<String, Object>>> queryDeviceListByUserId(@PathVariable String userId,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		return deviceQueryRestService.queryDeviceListByUserId(userId, pageNum, pageSize);
	}



	/**
	 * 
	 * @Description:根据appTag获取设备集合
	 * @param appTags
	 *            单个或多个appTag
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            分页大小
	 * @return Result<Map<String, Object>>
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.QUERY_DEVICELIST_BY_APPTAGS, method = RequestMethod.POST)
	public Result<Map<String, Object>> queryDeviceListByAppTags(@RequestBody String requestParams) {
		return deviceQueryRestService.queryDeviceListByAppTags(requestParams);
	}
}
