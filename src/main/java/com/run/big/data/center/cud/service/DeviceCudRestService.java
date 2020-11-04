/*
 * File name: DeviceCudRestService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2018年01月16日 ...
 * ... ...
 *
 ***************************************************/
package com.run.big.data.center.cud.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.run.big.data.center.entity.BigDataCenterConstants;
import com.run.big.data.center.entity.LogConstants;
import com.run.common.util.ParamChecker;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.DateUtils;
import com.run.entity.tool.ResultBuilder;
import com.run.gathering.center.api.crud.service.DeviceCrudService;
import com.run.gathering.center.api.crud.service.er.DevicePeopleCrudService;
import com.run.gathering.center.api.entity.Device;
import com.run.gathering.center.api.entity.DeviceDTO;
import com.run.gathering.center.api.entity.DevicePeople;
import com.run.gathering.center.api.query.service.DeviceQueryService;
import com.run.gathering.center.constants.ParamsConstants;
import com.run.gathering.center.constants.UrlPathConstants;
import com.run.usc.base.query.UserBaseQueryService;
import com.sefon.commons.ThingServiceResult;
import com.sefon.cud.PolicyCudService;
import com.sefon.cud.RegisterCudService;
import com.sefon.cud.ShadowCudService;
import com.sefon.read.RegisterReadService;

/**
 * @Description: 设备增删改service
 * @author: 郭飞龙
 * @version: 1.0, 2018年01月16日
 */
@Service
public class DeviceCudRestService {

	private static final Logger		logger	= Logger.getLogger(DeviceCudRestService.class);

	@Autowired
	private DeviceCrudService		deviceCrudService;

	@Autowired
	private UserBaseQueryService	userQueryRpcService;

	@Autowired
	private RegisterCudService		registerCudService;

	@Autowired
	private PolicyCudService		policyCudService;

	@Autowired
	private DevicePeopleCrudService	devicePeopleCrudService;

	@Autowired
	private DeviceQueryService		deviceQueryService;

	@Autowired
	private RegisterReadService		registerReadService;

	@Autowired
	private ShadowCudService		shadowCudService;



	/**
	 * @Description:添加设备
	 * @param devices
	 *            参数格式{"deviceInfo":{"deviceName":"","deviceTypeId":"","protocolType":"","openProtocols":"enabled/disabled"},
	 *            "userId":""} 注：deviceTypeId 为二级类型ID
	 * @return
	 */
	public Result<Device> addDevice(String deviceInfo) {
		logger.info(String.format("[addDevice()->request params:%s]", deviceInfo));
		try {
			// 获取验证结果
			Result<Device> validateResult = this.validateDevice(deviceInfo);
			// 验证不通过
			if (!ParamsConstants.SUCCESS.equals(validateResult.getResultStatus().getResultCode())) {
				logger.error("参数验证:" + LogConstants.PARAMETERS_OF_ILLEGAL);
				return validateResult;
			}
			// 通过验证
			Device device = validateResult.getValue();
			Map<String, String> map = Maps.newHashMap();
			map.put(ParamsConstants.THING_NAME, device.getDeviceName());
			map.put(ParamsConstants.DEVICE_PROTOCOLTYPE, device.getProtocolType().toUpperCase());
			// 调用采集接口入库
			ThingServiceResult createResult = registerCudService.createThing(JSON.toJSONString(map));

			// 采6集端入库成功，再将数据写入北端数据库
			if (createResult.getStatus() == HttpStatus.SC_OK) {
				// 解析返回数据，取出设备ID
				logger.info("--采集端入库成功,正在写入北端数据库--");
				JsonNode response = createResult.getResponse();
				if (response != null) {
					String deviceId = response.findValue(ParamsConstants.THING_ID).asText();
					// 根据设备编号，从南段获取设备秘钥
					ThingServiceResult deviceInfoById = registerReadService.getThing(deviceId);
					if (deviceInfoById.getStatus() == 200 && deviceInfoById.getResponse() != null) {
						device.setDeviceKey(deviceInfoById.getResponse().findValue(ParamsConstants.KEY_THING).asText());
					} else {
						return ResultBuilder.failResult("根据设备编号,从南段获取设备秘钥失败!");
					}
					// 北端入库设备信息
					device.setDeviceId(deviceId);
					device.setCreationTime(DateUtils.formatDate(new Date()));
					device.setUpdateTime(DateUtils.formatDate(new Date()));
					device.setDeviceStatus(ParamsConstants.DEVICE_ENABLE);
					RpcResponse<Device> saveDevice = deviceCrudService.saveDevice(device);
					if (!saveDevice.isSuccess()) {
						logger.error("写入数据库失败" + saveDevice.getMessage());
						return ResultBuilder.failResult(saveDevice.getMessage());
					}

					// 设备策略信息
					List<String> readList = new ArrayList<String>();
					List<String> writeList = new ArrayList<String>();
					readList.add(UrlPathConstants.DEVICE_REGISTER_POLICY_READ + deviceId);
					writeList.add(UrlPathConstants.DEVICE_REGISTER_POLICY_WRITE + deviceId);
					LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap<String, List<String>>();
					linkedHashMap.put(ParamsConstants.READ, readList);
					linkedHashMap.put(ParamsConstants.WRITE, writeList);
					String json = JSON.toJSONString(linkedHashMap);

					// 调用设备策略服务

					ThingServiceResult policyResult = policyCudService.updatePolicy(deviceId, json);

					// 南端设备策略入库成功，写入北端数据库
					if (policyResult.getStatus() == HttpStatus.SC_OK) {
						DevicePeople devicePeople = new DevicePeople();
						devicePeople.setDeviceId(deviceId);
						devicePeople.setUserId(device.getUserId());
						devicePeople.setId(UUID.randomUUID().toString().replaceAll("-", ""));
						RpcResponse<DevicePeople> saveDevicePeople = devicePeopleCrudService
								.saveDevicePeople(devicePeople);
						if (saveDevicePeople.isSuccess()) {
							logger.info(saveDevice.getMessage());
							return ResultBuilder.successResult(saveDevice.getSuccessValue(), saveDevice.getMessage());
						} else {
							logger.error("设备新增失败，北端数据库写入失败！");
							return ResultBuilder.failResult("设备新增失败，写入北端数据库失败！");
						}
					} else {
						logger.error("设备新增失败，南端设备策略入库失败！");
						return ResultBuilder.failResult("设备新增失败，南端设备策略入库失败！");
					}
				} else {
					logger.error("采集端入库失败");
					return ResultBuilder.failResult("设备新增失败，采集端响应值为空！");
				}
			} else {
				logger.error("采集端入库失败");
				return ResultBuilder.failResult("设备新增失败，数据库异常！");
			}

		} catch (Exception e) {
			logger.error("addDevice()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	private Result<Device> validateDevice(String deviceInfo) throws Exception {
		logger.info(String.format("[validateDevice()->request params:%s]", deviceInfo));
		// 判断是否为空
		if (ParamChecker.isBlank(deviceInfo)) {
			logger.error("deviceInfo--参数不能为空!");
			return ResultBuilder.emptyResult();
		}
		// 判断是否是json格式
		if (ParamChecker.isNotMatchJson(deviceInfo)) {
			logger.error(LogConstants.PARAMETERS_OF_ILLEGAL);
			return ResultBuilder.invalidResult();
		}
		JSONObject deviceInfoJson = JSON.parseObject(deviceInfo);
		// 判断是否是业务数据
		if (!deviceInfoJson.containsKey(ParamsConstants.DEVICE_INFO)
				|| !deviceInfoJson.containsKey(ParamsConstants.USER_ID)) {
			logger.error(LogConstants.PARAMETERS_OF_ILLEGAL);
			return ResultBuilder.noBusinessResult();
		}
		String userId = deviceInfoJson.getString(ParamsConstants.USER_ID);
		// 校验用户是否存在
		RpcResponse<?> getUserResult = userQueryRpcService.getUserByUserId(userId);
		if (!getUserResult.isSuccess()) {
			logger.error("用户不存在!");
			return ResultBuilder.failResult(getUserResult.getMessage());
		}

		String deviceStr = deviceInfoJson.getString(ParamsConstants.DEVICE_INFO);
		// 判断设备信息是否为空
		if (ParamChecker.isBlank(deviceStr)) {
			logger.error("设备信息为空!");
			return ResultBuilder.emptyResult();
		}
		// 检验设备信息参数合法性
		if (ParamChecker.isNotMatchJson(deviceStr)) {
			logger.error(LogConstants.PARAMETERS_OF_ILLEGAL);
			return ResultBuilder.invalidResult();
		}
		JSONObject deviceJson = JSON.parseObject(deviceStr);
		if (!deviceJson.containsKey(ParamsConstants.DEVICE_NAME)) {
			logger.error(LogConstants.NO_PARAMETER_EXISTS + ParamsConstants.DEVICE_NAME);
			return ResultBuilder.noBusinessResult();
		}
		String deviceName = deviceJson.getString(ParamsConstants.DEVICE_NAME);
		if (!deviceJson.containsKey(ParamsConstants.DEVICE_PROTOCOLTYPE)) {
			logger.error(LogConstants.NO_PARAMETER_EXISTS + ParamsConstants.DEVICE_PROTOCOLTYPE);
			return ResultBuilder.noBusinessResult();
		}
		String protocolType = deviceJson.getString(ParamsConstants.DEVICE_PROTOCOLTYPE);
		if (!deviceJson.containsKey(ParamsConstants.DEVICE_TYPE_ID)) {
			logger.error(LogConstants.NO_PARAMETER_EXISTS + ParamsConstants.DEVICE_TYPE_ID);
			return ResultBuilder.noBusinessResult();
		}
		String deviceTypeId = deviceJson.getString(ParamsConstants.DEVICE_TYPE_ID);
		if (!deviceJson.containsKey(ParamsConstants.OPEN_PROTOCOLS)) {
			logger.error(LogConstants.NO_PARAMETER_EXISTS + ParamsConstants.OPEN_PROTOCOLS);
			return ResultBuilder.noBusinessResult();
		}
		String openProtocols = deviceJson.getString(ParamsConstants.OPEN_PROTOCOLS);
		// 组装设备新增信息
		Device device = new Device();
		device.setDeviceName(deviceName);
		device.setProtocolType(protocolType);
		device.setDeviceType(deviceTypeId);
		device.setUserId(userId);
		device.setOpenProtocols(openProtocols);
		if (deviceJson.containsKey(ParamsConstants.DEVICE_ID)) {
			device.setDeviceId(deviceJson.getString(ParamsConstants.DEVICE_ID));
		}
		if (deviceJson.containsKey(ParamsConstants.DEVICE_STATUS)) {
			device.setDeviceStatus(deviceJson.getString(ParamsConstants.DEVICE_STATUS));
		}
		logger.info("设备信息验证成功");
		return ResultBuilder.successResult(device, "");
	}



	/**
	 * @param deviceId
	 *            设备id 状态删除(disabled 删除/停用)
	 * @return
	 */
	public Result<String> deviceDelete(String deviceId) {
		logger.info(String.format("[validateDevice()->request params:%s]", deviceId));
		try {
			if (StringUtils.isEmpty(deviceId)) {
				logger.error(LogConstants.NO_PARAMETER_EXISTS + ParamsConstants.DEVICE_ID);
				return ResultBuilder.invalidResult();
			}
			RpcResponse<Device> device = deviceQueryService.getDeviceById(deviceId);
			if (!device.isSuccess() || device.getSuccessValue() == null) {
				logger.error(LogConstants.PARAMETERS_OF_ILLEGAL);
				return ResultBuilder.failResult("设备不存在!");
			}

			String deviceStatus = device.getSuccessValue().getDeviceStatus();

			if (ParamsConstants.DEVICE_DISABLE.equals(deviceStatus)) {
				logger.error(LogConstants.PARAMETERS_OF_ILLEGAL);
				return ResultBuilder.failResult("该设备已被删除/停用!");
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("option", BigDataCenterConstants.DISABLE_FRO_SOUTH);
			ThingServiceResult thingResult = registerCudService.updateThing(deviceId, jsonObject.toJSONString());
			if (thingResult.getStatus() != HttpStatus.SC_OK) {
				logger.error("更改设备状态:" + ParamsConstants.DEVICE_OPTERA_FAIL);
				return ResultBuilder.failResult(ParamsConstants.DEVICE_OPTERA_FAIL);
			}
			RpcResponse<Device> rpcResponse = deviceCrudService.switchDeviceState(deviceId,
					ParamsConstants.DEVICE_DISABLE);
			if (rpcResponse.isSuccess()) {
				logger.info("设备状态更改" + ParamsConstants.DEVICE_OPTERA_SUCCESS);
				return ResultBuilder.successResult(null, ParamsConstants.DEVICE_OPTERA_SUCCESS);
			}
			logger.error(rpcResponse.getMessage());
			return ResultBuilder.failResult(rpcResponse.getMessage());
		} catch (Exception e) {
			logger.error("deviceDelete()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @param deviceId
	 *            设备ID
	 * @param device
	 *            设备实体对象
	 * @return
	 */
	public Result<DeviceDTO> updateDevice(String deviceId, Device device) {
		logger.info(String.format("[updateDevice()->request params:%s]", deviceId));
		try {
			// 参数校验
			if (StringUtils.isBlank(deviceId)) {
				logger.error(LogConstants.NO_PARAMETER_EXISTS + ParamsConstants.DEVICE_ID);
				return ResultBuilder.emptyResult();
			}
			RpcResponse<Device> deviceById = deviceQueryService.getDeviceById(deviceId);
			if (!deviceById.isSuccess() || deviceById.getSuccessValue() == null) {
				logger.error(LogConstants.NO_PARAMETER_EXISTS);
				return ResultBuilder.failResult("此设备不存在!");
			}
			// 以URL的设备id为准进行修改
			device.setDeviceId(deviceId);
			// 设备更新
			RpcResponse<Device> response = deviceCrudService.updateDevice(device);
			if (response != null && response.isSuccess() && response.getSuccessValue() != null) {
				DeviceDTO deviceDTO = new DeviceDTO();
				BeanUtils.copyProperties(response.getSuccessValue(), deviceDTO);
				ThingServiceResult rpcResponse = registerReadService.getThingStatus(deviceId);
				if (rpcResponse != null && rpcResponse.getStatus() == ParamsConstants.STATUS_OK
						&& rpcResponse.getResponse() != null) {
					String onlineState = rpcResponse.getResponse().get(ParamsConstants.KEY_THING_STATUS).asText();
					deviceDTO.setOnlineState(onlineState);
				}
				logger.info(ParamsConstants.DEVICE_UPDATE_SUCCESS);
				return ResultBuilder.successResult(deviceDTO, ParamsConstants.DEVICE_UPDATE_SUCCESS);
			}
			// 更新失败
			logger.info(response + ParamsConstants.DEVICE_UPDATE_FAIL);
			return ResultBuilder.failResult(ParamsConstants.DEVICE_UPDATE_FAIL);
		} catch (Exception e) {
			logger.error("updateDevice()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	public Result<Boolean> remoteControl(String id, String desiredInfo) {
		logger.info(String.format("[remoteControl()->request params:%s,%s]", id, desiredInfo));
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(desiredInfo)) {
			logger.error(LogConstants.NO_PARAMETER_EXISTS);
			return ResultBuilder.noBusinessResult();
		}

		try {
			ThingServiceResult updateThingShadowResult = shadowCudService.updateThingShadowByConsole(id, desiredInfo);
			// 控制命令下发成功，保存记录
			if (updateThingShadowResult.getStatus() == 200) {
				logger.info(LogConstants.SAVE_SUCCESS);
				return ResultBuilder.successResult(Boolean.TRUE, LogConstants.SAVE_SUCCESS);
			} else if (updateThingShadowResult.getStatus() == 409) {
				logger.error(LogConstants.SAVE_FAIL + "：设备未接入");
				return ResultBuilder.failResult("设备未接入");
			} else {
				logger.error(LogConstants.SAVE_FAIL);
				return ResultBuilder.failResult(LogConstants.SAVE_FAIL);
			}
		} catch (Exception e) {
			logger.error("remoteControl()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
