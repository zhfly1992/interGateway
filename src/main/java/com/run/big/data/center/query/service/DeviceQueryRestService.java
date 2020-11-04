/*
 * File name: DeviceQueryRestService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 田明 2018年1月16日 ... ... ...
 *
 ***************************************************/
package com.run.big.data.center.query.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.run.big.data.center.util.ConvertUtil;
import com.run.common.util.ExceptionChecked;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;
import com.run.gathering.center.api.entity.Device;
import com.run.gathering.center.api.query.service.DeviceQueryService;

/**
 * @Description:
 * @author: 田明
 * @version: 1.0, 2018年01月16日
 */
@Service
public class DeviceQueryRestService {

	private static final Logger	logger	= Logger.getLogger(DeviceQueryRestService.class);

	@Autowired
	private DeviceQueryService	deviceQueryService;



	public Result<Map<String, Object>> queryDeviceInfoById(String id) {
		logger.info(String.format("[queryAppTagsByUserId()->request params:id:%s]", id));
		if (id == null || "".equals(id)) {
			logger.error("[queryDeviceInfoById()->invalid：设备id不能为空！]");
			return ResultBuilder.noBusinessResult();
		}
		try {
			// 根据设备id查询设备信息
			RpcResponse<Device> deviceRpcResponse = deviceQueryService.getDeviceById(id);
			if (!deviceRpcResponse.isSuccess()) {
				logger.error("[queryDeviceInfoById()->invalid：" + deviceRpcResponse.getMessage() + "]");
				return ResultBuilder.failResult(deviceRpcResponse.getMessage());
			}
			Map<String, Object> map = new HashMap<>();
			if (deviceRpcResponse.getSuccessValue() != null) {
				Device device = deviceRpcResponse.getSuccessValue();
				map = ConvertUtil.beanToMap(device);
				// 移除用户信息
				map.remove("userId");
				logger.info("[queryDeviceInfoById()->invalid：" + deviceRpcResponse.getMessage() + "]");
			}
			return ResultBuilder.successResult(map, deviceRpcResponse.getMessage());
		} catch (Exception e) {
			logger.error("queryDeviceInfoById()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	public Result<PageInfo<Map<String, Object>>> queryDeviceListByUserId(String userId, Integer pageNum,
			Integer pageSize) {
		logger.info(String.format("[queryAppTagsByUserId()->request params:userId:%s]", userId));
		// 判断用户id是否为空
		if (null == userId) {
			logger.error("[queryDeviceListByUserId()->invalid：用户id不能为空！]");
			return ResultBuilder.invalidResult();
		}
		// 判断pageNo是否为空
		if (null == pageNum) {
			pageNum = 1;
		}
		// 判断pageSize是否为空
		if (null == pageSize) {
			pageSize = 10;
		}
		try {
			RpcResponse<PageInfo<Map<String, Object>>> deviceList = deviceQueryService.getDeviceListByUserId(userId,
					pageNum, pageSize, null, null);
			if (deviceList.isSuccess()) {
				logger.info("[queryDeviceListByUserId()->invalid：" + deviceList.getMessage() + "]");
				return ResultBuilder.successResult(deviceList.getSuccessValue(), deviceList.getMessage());
			} else {
				logger.info("[queryDeviceListByUserId()->invalid：" + deviceList.getMessage() + "]");
				return ResultBuilder.failResult(deviceList.getMessage());
			}
		} catch (Exception e) {
			logger.error("queryDeviceListByUserId()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	public Result<Map<String, Object>> queryDeviceListByAppTags(String requestParams) {
		logger.info(String.format("[queryAppTagsByUserId()->request params:%s]", requestParams));
		try {
			// 参数验证
			if (ExceptionChecked.checkRequestParam(requestParams) != null) {
				return ExceptionChecked.checkRequestParam(requestParams);
			}
			JSONObject paramJson = JSON.parseObject(requestParams);
			String appTagsStr = paramJson.getString("appTags");
			// String pageNumStr = paramJson.getString("pageNum");
			// String pageSizeStr = paramJson.getString("pageSize");
			// // 页大小，页码默认为0
			// int pageNum = 0;
			// int pageSize = 0;
			//
			// if (pageNumStr != null && !StringUtils.isNumeric(pageNumStr)) {
			// logger.error("queryDeviceListByAppTags()--页码格式非法!");
			// return ResultBuilder.failResult("页码格式非法!");
			// }
			// if (pageSizeStr != null && !StringUtils.isNumeric(pageSizeStr)) {
			// logger.error("queryDeviceListByAppTags()--页大小格式非法!");
			// return ResultBuilder.failResult("页大小格式非法!");
			// }

			if (StringUtils.isBlank(appTagsStr)) {
				logger.error("queryDeviceListByAppTags()--请至少传递一个appTag!");
				return ResultBuilder.failResult("请至少传递一个appTag!");
			}

			String[] appTags = appTagsStr.split(",");
			List<String> appTagList = Arrays.asList(appTags);

			// // 根据appTag查询设备信息
			// RpcResponse<Map<String, Object>> deviceList =
			// deviceEsQueryService.queryDeviceList(appTags, null, null,
			// null, pageNum, pageSize);

			// 根据appTag查询设备信息
			long s1 = System.currentTimeMillis();
			RpcResponse<Map<String, Object>> deviceList = deviceQueryService.getDevicesByAppTags(appTagList);
			long s2 = System.currentTimeMillis();
			long time1 = s2 - s1;
			if (!deviceList.isSuccess()) {
				logger.error("queryDeviceListByAppTags()--" + deviceList.getMessage());
				return ResultBuilder.failResult("interGateway查询时间:" + time1 + deviceList.getMessage());
			}
			return ResultBuilder.successResult(deviceList.getSuccessValue(),
					deviceList.getMessage() + "interGateway查询时间:" + time1);

		} catch (Exception e) {
			logger.error("queryDeviceListByAppTags()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}
}
