/*
 * File name: UserQueryRestService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2018年3月16日 ...
 * ... ...
 *
 ***************************************************/

package com.run.big.data.center.query.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.run.common.util.ExceptionChecked;
import com.run.common.util.ParamChecker;
import com.run.entity.common.Pagination;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;
import com.run.gathering.center.api.utils.CheckParameterUtil;
import com.run.usc.api.constants.UscConstants;
import com.run.usc.base.query.AccSourceBaseQueryService;
import com.run.usc.base.query.AccUserBaseQueryService;
import com.run.usc.base.query.TenAccBaseQueryService;
import com.run.usc.base.query.UserBaseQueryService;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2018年3月16日
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserQueryRestService {
	private static final Logger			logger	= Logger.getLogger(UserQueryRestService.class);

	@Autowired
	private AccSourceBaseQueryService	accSourceBaseQueryService;

	@Autowired
	private UserBaseQueryService		userQueryRpcService;

	@Autowired
	private AccUserBaseQueryService		accUserBaseQueryService;

	@Autowired
	private TenAccBaseQueryService		accService;



	/**
	 * @Description 查询组织架构树
	 *
	 * @return
	 */

	public Result<List<Map>> getAccSourceInfoByType(String accessSecret, String organizationName) {
		logger.info(String.format("[getAccSourceInfoByType()->request params:accessSecret:%s,organizationName:%s]",
				accessSecret, organizationName));
		if (StringUtils.isBlank(accessSecret)) {
			logger.error("[getAccSourceInfoByType()->invalid：密钥accessSecret不能为空！]");
			return ResultBuilder.noBusinessResult();
		}
		JSONObject json = new JSONObject();

		json.put(UscConstants.ACCESS_SECRET, accessSecret);
		json.put(UscConstants.SOURCE_TYPE, "sourceTypeOrganize"); // sourceTypeOrganize
																	// 组织资源类型,在组织查询中是固定的
		json.put(UscConstants.SOURCE_NAME, organizationName);
		try {
			RpcResponse<List<Map>> accSourceInfoByType = accSourceBaseQueryService.getAccSourceInfoByType(json);
			if (!accSourceInfoByType.isSuccess() || accSourceInfoByType.getSuccessValue() == null) {
				logger.error("getAccSourceInfoByType()--" + accSourceInfoByType.getMessage());
				return ResultBuilder.failResult(accSourceInfoByType.getMessage());

			} else {
				logger.info("getAccSourceInfoByType()--" + accSourceInfoByType.getMessage());
				return ResultBuilder.successResult(accSourceInfoByType.getSuccessValue(),
						accSourceInfoByType.getMessage());
			}
		} catch (Exception e) {
			logger.error("getAccSourceInfoByType()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 分页查询组织人员
	 *
	 * @param accessSecret
	 * @param organizationName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */

	public Result<Pagination<Map<String, Object>>> getOrganizationPersonPageByType(String accessSecret,
			String organizationId, String organizationName, Integer pageNum, Integer pageSize, String state) {
		logger.info(String.format(
				"[getOrganizationPersonPageByType()->request params:accessSecret:%s,organizationName:%s,organizationId:%s]",
				accessSecret, organizationName, organizationId));
		if (StringUtils.isBlank(accessSecret)) {
			logger.error("[getOrganizationPersonPageByType()->invalid：密钥accessSecret不能为空！]");
			return ResultBuilder.noBusinessResult();
		}
		JSONObject json = new JSONObject();

		json.put(UscConstants.SOURCE_ID, organizationId);
		json.put(UscConstants.SOURCE_NAME, organizationName);
		json.put(UscConstants.ACCESS_SECRET, accessSecret);
		json.put(UscConstants.SOURCE_TYPE, "sourceTypeOrganize");// sourceTypeOrganize
																	// 组织资源类型,在组织查询中是固定的
		json.put("state", state);
		json.put(UscConstants.PAGENUMBER, pageNum);
		json.put(UscConstants.PAGESIZE, pageSize);

		try {
			RpcResponse<Pagination<Map<String, Object>>> parentAccSourcePageByType = accSourceBaseQueryService
					.getParentAccSourcePageByType(json);
			if (!parentAccSourcePageByType.isSuccess() || parentAccSourcePageByType.getSuccessValue() == null) {
				logger.error("getOrganizationPersonPageByType()--" + parentAccSourcePageByType.getMessage());
				return ResultBuilder.failResult(parentAccSourcePageByType.getMessage());

			} else {
				logger.info("getOrganizationPersonPageByType()--" + parentAccSourcePageByType.getMessage());
				return ResultBuilder.successResult(parentAccSourcePageByType.getSuccessValue(),
						parentAccSourcePageByType.getMessage());
			}
		} catch (Exception e) {
			logger.error("getOrganizationPersonPageByType()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 根据组织id查詢组织信息
	 *
	 * @param organizationId
	 * @return
	 */

	public Result<Map<String, Object>> queryOrganizationById(String organizationId) {
		logger.info(String.format("[queryOrganizationById()->request params:organizationId:%s]", organizationId));
		if (StringUtils.isBlank(organizationId)) {
			logger.error("[queryOrganizationById()->invalid：密钥accessSecret或组织类型organType不能为空！]");
			return ResultBuilder.noBusinessResult();
		}
		try {
			RpcResponse<Map> sourceMessageById = accSourceBaseQueryService.getSourceMessageById(organizationId);
			if (!sourceMessageById.isSuccess() || sourceMessageById.getSuccessValue() == null) {
				logger.error("queryOrganizationById()-->查询失败");
				return ResultBuilder.failResult(sourceMessageById.getMessage());

			} else {
				logger.info("queryOrganizationById()-->查询成功");
				return ResultBuilder.successResult(sourceMessageById.getSuccessValue(), sourceMessageById.getMessage());
			}
		} catch (Exception e) {
			logger.error("queryOrganizationById()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}

	}



	/**
	 * @Description 通过token查询个人中心基本信息
	 *
	 * @param token
	 * @return
	 */

	public Result<Object> queryUserByToken(String token) {
		logger.info(String.format("[queryUserByToken()->request params:token:%s]", token));
		if (StringUtils.isBlank(token)) {
			logger.error("[queryUserByToken()->invalid：token不能为空！]");
			return ResultBuilder.noBusinessResult();
		}
		try {
			RpcResponse userByToken = userQueryRpcService.getUserByToken(token);

			if (!userByToken.isSuccess() || userByToken.getSuccessValue() == null) {
				logger.error("queryUserByToken()-->查询失败");
				return ResultBuilder.failResult(userByToken.getMessage());

			} else {
				logger.info("queryUserByToken()-->查询成功" + token);
				return ResultBuilder.successResult(userByToken.getSuccessValue(), userByToken.getMessage());
			}
		} catch (Exception e) {
			logger.error("queryUserByToken()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 查询接入方下面的所有人员信息
	 *
	 * @param accessSecret
	 * @param pageNum
	 * @param pageSize
	 * @param organizationId
	 * @param roleName
	 * @param nameKey
	 * @return
	 */

	public Result<Object> getAllOrganizationPersonPage(String accessSecret, Integer pageNum, Integer pageSize,
			String organizationId, String nameKey, String roleName, String peopleType, String receiveSms,
			String state) {
		logger.info(String.format(
				"[getAllOrganizationPersonPage()->request params:accessSecret:%s,organizationId:%s,nameKey:%s,roleName:%s,peopleType:%s,receiveSms:%s]",
				accessSecret, organizationId, nameKey, roleName, peopleType, receiveSms));
		if (StringUtils.isBlank(accessSecret)) {
			logger.error("[getAllOrganizationPersonPage()->invalid：密钥accessSecret不能为空！]");
			return ResultBuilder.noBusinessResult();
		}

		JSONObject json = new JSONObject();

		json.put(UscConstants.ORGANIZED_ID, organizationId);
		json.put("nameKey", nameKey);
		json.put("roleName", roleName);
		json.put(UscConstants.ACCESS_SECRET, accessSecret);
		json.put(UscConstants.PAGENUMBER, pageNum);
		json.put(UscConstants.PAGESIZE, pageSize);
		json.put(UscConstants.PEOPLE_TYPE, peopleType);
		json.put(UscConstants.RECEIVESMS, receiveSms);
		json.put("state", state);

		try {
			RpcResponse pageAllUserByKey = userQueryRpcService.getPageAllUserByKey(json);
			if (!pageAllUserByKey.isSuccess() || pageAllUserByKey.getSuccessValue() == null) {
				logger.error("getAllOrganizationPersonPage()-->查询失败");
				return ResultBuilder.failResult(pageAllUserByKey.getMessage());

			} else {
				logger.info("getAllOrganizationPersonPage()-->查询成功");
				return ResultBuilder.successResult(pageAllUserByKey.getSuccessValue(), pageAllUserByKey.getMessage());
			}
		} catch (Exception e) {
			logger.error("getAllOrganizationPersonPage()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 根据用户id查询用户所属接入方信息
	 *
	 * @param userId
	 * @return
	 */

	public Result<List<Map>> queryAccessInfoByUserId(String userId) {
		logger.info(String.format("[queryAccessInfoByUserId()->request params:userId:%s]", userId));
		try {

			if (StringUtils.isBlank(userId)) {
				logger.error("[queryUserByToken()->invalid：userId不能为空！]");
				return ResultBuilder.noBusinessResult();
			}
			RpcResponse<List<Map>> res = accUserBaseQueryService.getListAccessByUserId(userId);
			if (res.isSuccess() && res.getSuccessValue() != null) {
				logger.info(String.format("[getAccinfoByUserId()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[getAccinfoByUserId()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error(String.format("[getAccinfoByUserId()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 检测组织下面是否存在子类
	 *
	 * @param organizationId
	 * @return
	 */

	public Result<Boolean> checkChildByOrganId(String organizationId) {
		logger.info(String.format("[queryUserByToken()->request params:organizationId:%s]", organizationId));
		try {

			if (StringUtils.isBlank(organizationId)) {
				logger.error("[checkChildByOrganId()->invalid：organizationId不能为空！]");
				return ResultBuilder.noBusinessResult();
			}
			RpcResponse<Boolean> res = accSourceBaseQueryService.checkOrgHasChild(organizationId);
			if (res.isSuccess() && res.getSuccessValue() != null) {
				logger.info(String.format("[checkOrgName()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[checkOrgName()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error(String.format("[checkOrgName()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param accessSecret
	 * @param organizationType
	 * @param checkInfo
	 * @return
	 */

	public Result<Boolean> checkOrgName(String orgInfo) {
		logger.info(String.format("[checkOrgName()->request orgInfo:%s]", orgInfo));
		try {

			// 参数基础校验

			if (ParamChecker.isNotMatchJson(orgInfo)) {
				logger.error("[checkOrgName()->invalid：传入参数不是json格式！]");
				return ResultBuilder.invalidResult();
			}
			JSONObject json = JSON.parseObject(orgInfo);
			RpcResponse<Boolean> checkBusinessKey = CheckParameterUtil.checkBusinessKey(logger, "checkOrgName", json,
					"accessSecret", "organizationName");
			if (null != checkBusinessKey) {
				logger.error("[checkOrgName()->invalid：" + checkBusinessKey.getMessage() + "]");
				return ResultBuilder.failResult(checkBusinessKey.getMessage());
			}
			if (!json.containsKey("parentId")) {
				logger.error("[checkOrgName()->invalid：父组织id参数名不存在]");
				return ResultBuilder.failResult("父组织id参数名不存在");
			}
			/*
			 * if (!json.containsKey("id")) {
			 * logger.error("[checkOrgName()->invalid：组织id参数名不存在]"); return
			 * ResultBuilder.failResult("组织id参数名不存在"); }
			 */
			String organizationName = json.getString("organizationName");
			String accessSecret = json.getString("accessSecret");
			String parentId = json.getString("parentId");
			String id = json.getString("id");
			RpcResponse<Boolean> res = accSourceBaseQueryService.checkOrgName(organizationName, accessSecret, parentId,
					"sourceTypeOrganize", "LOCMAN", id);
			// sourceTypeOrganize 组织资源类型,在组织查询中是固定的,LOCMAN 接入方类型,在在查询组织时也是固定的
			if (res.isSuccess() && res.getSuccessValue() != null) {
				logger.info(String.format("[checkOrgName()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[checkOrgName()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error(String.format("[checkOrgName()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 根据用户id查询用户所属接入方信息
	 *
	 * @param userId
	 * @return
	 */

	public Result<List<Map>> queryAccessInfoByUserInfo(String userInfo) {
		logger.info(String.format("[queryAccessInfoByUserInfo()->request params:%s]", userInfo));
		try {

			if (StringUtils.isBlank(userInfo)) {
				logger.error("[queryUserByToken()->invalid：userId不能为空！]");
				return ResultBuilder.noBusinessResult();
			}

			RpcResponse<List<Map>> res = accUserBaseQueryService.getListAccessByUserInfo(userInfo);
			if (res.isSuccess() && res.getSuccessValue() != null) {
				logger.info(String.format("[getAccinfoByUserId()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[getAccinfoByUserId()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error(String.format("[getAccinfoByUserId()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}
	}



	public Result<List<Map>> ownAndSonOrganizationInfo(String organizationId) {
		logger.info(String.format("[ownAndSonOrganizationInfo()->request params:organizationId:%s]", organizationId));
		if (StringUtils.isBlank(organizationId)) {
			logger.error("[ownAndSonOrganizationInfo()->invalid：organizationId不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			RpcResponse<Map> sourceMessageById = accSourceBaseQueryService.getSourceMessageById(organizationId);
			if (!sourceMessageById.isSuccess()) {
				logger.error(String.format("[ownAndSonOrganizationInfo()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(sourceMessageById.getMessage());
			}
			List<Map> resultList = Lists.newArrayList();
			Map organizationInfo = sourceMessageById.getSuccessValue();
			String sourceInfo = "" + organizationInfo.get("sourceInfo");
			JSONObject json = JSON.parseObject(sourceInfo);
			json.put("sourceInfo", "own");
			RpcResponse<List<Map>> modelByParentId = accSourceBaseQueryService.getModelByParentId(organizationId);
			resultList.add(json);
			if (modelByParentId.isSuccess() && modelByParentId.getSuccessValue() != null) {
				logger.info(String.format("[ownAndSonOrganizationInfo()->success:%s]", modelByParentId.getMessage()));
				List<Map> allOrganizationInfo = modelByParentId.getSuccessValue();
				resultList.addAll(allOrganizationInfo);

				return ResultBuilder.successResult(resultList, "查询成功!");
			} else {
				logger.error(String.format("[ownAndSonOrganizationInfo()->fail:%s]", "查询失败!"));
				return ResultBuilder.successResult(resultList, "查询成功!");
			}
		} catch (Exception e) {
			logger.error("ownAndSonOrganizationInfo()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	public Result<String> getParentByOrganizationId(String organizationId) {
		logger.info(String.format("[getParentByOrganizationId()->request params:organizationId:%s]", organizationId));
		if (StringUtils.isBlank(organizationId)) {
			logger.error("[ownAndSonOrganizationInfo()->invalid：organizationId不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			RpcResponse<String> modelByParentId = accSourceBaseQueryService.findOrgParentId(organizationId);
			if (modelByParentId.isSuccess() && !StringUtils.isBlank(modelByParentId.getSuccessValue())) {
				logger.info(String.format("[getParentByOrganizationId()->success:%s]", modelByParentId.getMessage()));
				return ResultBuilder.successResult(modelByParentId.getSuccessValue(), "查询成功!");
			} else {
				logger.error(String.format("[getParentByOrganizationId()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(modelByParentId.getMessage());
			}
		} catch (Exception e) {
			logger.error("ownAndSonOrganizationInfo()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	public Result<Map<String, String>> getOrganizationNamesByIds(String organizationIds) {
		logger.info(String.format("[getOrganizationNamesByIds()->request params:organizationIds:%s]", organizationIds));
		Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(organizationIds);
		if (null != checResult) {
			logger.error("参数不符合json格式" + organizationIds);
			return ResultBuilder.failResult("参数不符合json格式");
		}
		try {
			JSONObject json = JSON.parseObject(organizationIds);
			String organizationIdStr = json.getString("organizationIds");
			if (StringUtils.isBlank(organizationIdStr)) {
				return ResultBuilder.failResult("组织id为空!");
			}
			String[] organizationIdArray = organizationIdStr.split(",");
			List<String> organizationIdList = Arrays.asList(organizationIdArray);
			RpcResponse<Map> organizationIdAndNameList = accSourceBaseQueryService.findSourceInfo(organizationIdList);
			if (!organizationIdAndNameList.isSuccess() || organizationIdAndNameList.getSuccessValue() == null) {
				logger.error(String.format("[getOrganizationsByIds()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(organizationIdAndNameList.getMessage());
			} else {
				logger.info(
						String.format("[getOrganizationsByIds()->success:%s]", organizationIdAndNameList.getMessage()));
				return ResultBuilder.successResult(organizationIdAndNameList.getSuccessValue(), "查询成功!");
			}
		} catch (Exception e) {
			logger.error("getOrganizationsByIds()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:根据接入方密钥查询接入方信息
	 * @param
	 * @return
	 */

	public Result<Map> getAccessInfoByAS(String accessSecret) {
		logger.info(String.format("[getAccessInfoByAS()->request params:accessSecret:%s]", accessSecret));
		if (StringUtils.isBlank(accessSecret)) {
			logger.error("[getAccessInfoByAS()->invalid：accessSecret不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			RpcResponse<Map> findAccInfoById = accSourceBaseQueryService.findAccInfoById(accessSecret);
			if (!findAccInfoById.isSuccess() || findAccInfoById.getSuccessValue() == null) {
				logger.error(String.format("[getAccessInfoByAS()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(findAccInfoById.getMessage());
			} else {
				logger.info(String.format("[getAccessInfoByAS()->success:%s]", findAccInfoById.getMessage()));
				return ResultBuilder.successResult(findAccInfoById.getSuccessValue(), "查询成功!");
			}
		} catch (Exception e) {
			logger.error("getAccessInfoByAS()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}

	}



	/**
	 * 
	 * @Description:根据接入方密钥查询当前接入方下用户数量
	 * @param
	 * @return
	 */

	public Result<String> getUserNumByAS(String accessSecret) {
		logger.info(String.format("[getUserNumByAS()->request params:accessSecret:%s]", accessSecret));
		if (StringUtils.isBlank(accessSecret)) {
			logger.error("[getUserNumByAS()->invalid：accessSecret不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			RpcResponse<List<String>> userIdByAccId = userQueryRpcService.getUserIdByAccId(accessSecret);
			if (!userIdByAccId.isSuccess() || userIdByAccId.getSuccessValue() == null) {
				logger.error(String.format("[getUserNumByAS()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(userIdByAccId.getMessage());
			} else {
				logger.info(String.format("[getUserNumByAS()->success:%s]", userIdByAccId.getMessage()));
				List<String> successValue = userIdByAccId.getSuccessValue();
				return ResultBuilder.successResult("" + successValue.size(), "查询成功!");
			}
		} catch (Exception e) {
			logger.error("getUserNumByAS()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:根据接入方密钥查询最近一个月的用户使用率
	 * @param
	 * @return
	 */

	public Result<String> getUserUsageByAS(String accessSecret) {
		logger.info(String.format("[getUserUsageByAS()->request params:accessSecret:%s]", accessSecret));
		if (StringUtils.isBlank(accessSecret)) {
			logger.error("[getUserUsageByAS()->invalid：accessSecret不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			RpcResponse<String> usageRate = userQueryRpcService.getUsageRate(accessSecret);
			if (!usageRate.isSuccess() || StringUtils.isBlank(usageRate.getSuccessValue())) {
				logger.error(String.format("[getUserUsageByAS()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(usageRate.getMessage());
			} else {
				logger.info(String.format("[getUserUsageByAS()->success:%s]", usageRate.getMessage()));
				return ResultBuilder.successResult(usageRate.getSuccessValue(), "查询成功!");
			}
		} catch (Exception e) {
			logger.error("getUserUsageByAS()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:
	 * @param
	 * @return
	 */

	public Result<Map<String, Object>> getFactoryIdsByPersonId(String factoryIds) {
		logger.info(String.format("[getFactoryIdsByPersonId()->request params:%s]", factoryIds));
		Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(factoryIds);
		if (null != checResult) {
			logger.error("参数不符合json格式" + factoryIds);
			return ResultBuilder.failResult("参数不符合json格式");
		}
		try {
			JSONObject json = JSON.parseObject(factoryIds);
			JSONArray jsonArray = json.getJSONArray("uscIds");
			if (null == jsonArray) {
				logger.error("人员id为空!");
				return ResultBuilder.failResult("人员id为空!");
			}
			List<String> personList = jsonArray.toJavaList(String.class);
			if (null == personList || personList.isEmpty()) {
				logger.error("人员id为空!");
				return ResultBuilder.failResult("人员id为空!");
			}

			RpcResponse<List<Map<String, Object>>> findFactoryByIds = userQueryRpcService.findFactoryByIds(personList);
			if (!findFactoryByIds.isSuccess() || findFactoryByIds.getSuccessValue() == null) {
				logger.error(String.format("[getFactoryIdsByPersonId()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(findFactoryByIds.getMessage());
			} else {
				logger.info(String.format("[getFactoryIdsByPersonId()->success:%s]", findFactoryByIds.getMessage()));
				List<Map<String, Object>> successValue = findFactoryByIds.getSuccessValue();
				Map<String, Object> resultMap = Maps.newHashMap();
				for (Map<String, Object> map : successValue) {
					resultMap.putAll(map);
				}
				return ResultBuilder.successResult(resultMap, "查询成功!");
			}
		} catch (Exception e) {
			logger.error("getFactoryIdsByPersonId()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	public Result<Map<String, Object>> getUserByUserId(String userId) {
		logger.info(String.format("[getFactoryIdsByPersonId()->request params:userId:%s]", userId));
		if (StringUtils.isBlank(userId)) {
			logger.error("人员id不能为空！]");
			return ResultBuilder.noBusinessResult();
		}
		try {
			RpcResponse userByUserId = userQueryRpcService.getUserByUserId(userId);
			Map<String, Object> resultMap = (Map<String, Object>) userByUserId.getSuccessValue();
			if (!userByUserId.isSuccess() || userByUserId.getSuccessValue() == null) {
				logger.error(String.format("[getUserByUserId()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(userByUserId.getMessage());
			} else {
				return ResultBuilder.successResult(resultMap, "查询成功!");
			}
		} catch (Exception e) {
			logger.error("getUserByUserId()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:根据用户id查询用户接入方信息
	 * @param id
	 * @return
	 */
	public Result<Map<String, Object>> getUserCompany(String id) {
		logger.info(String.format("[getFactoryIdsByPersonId()->request params:id:%s]", id));
		try {
			if (StringUtils.isBlank(id)) {
				logger.error("人员id为空!");
				return ResultBuilder.failResult("人员id为空!");
			}
			RpcResponse<Map<String, Object>> res = accService.getAccessInfoById(id);
			if (!res.isSuccess()) {
				logger.error(String.format("[getUserUsageByAS()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(res.getMessage());
			} else {
				logger.info(String.format("[getUserUsageByAS()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), "查询成功!");
			}
		} catch (Exception e) {
			logger.error("getFactoryIdsByPersonId()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:
	 * @param
	 * @return
	 */

	public Result<List<Map<String, Object>>> getUserInfoByIds(String userIds) {
		logger.info(String.format("[getFactoryIdsByPersonId()->request params:%s]", userIds));
		Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(userIds);
		if (null != checResult) {
			logger.error("参数不符合json格式" + userIds);
			return ResultBuilder.failResult("参数不符合json格式");
		}
		try {
			JSONObject json = JSON.parseObject(userIds);
			JSONArray jsonArray = json.getJSONArray("userIds");
			if (null == jsonArray) {
				logger.error("人员id为空!");
				return ResultBuilder.failResult("人员id为空!");
			}
			List<String> personList = jsonArray.toJavaList(String.class);
			if (null == personList || personList.isEmpty()) {
				logger.error("人员id为空!");
				return ResultBuilder.failResult("人员id为空!");
			}
			RpcResponse<List> findUserInfoByIds = userQueryRpcService.findUserInfoByIds(personList);
			if (!findUserInfoByIds.isSuccess() || findUserInfoByIds.getSuccessValue() == null) {
				logger.error(String.format("[getUserInfoByIds()->fail:%s]", "查询失败!"));
				return ResultBuilder.failResult(findUserInfoByIds.getMessage());
			} else {
				logger.info(String.format("[getUserInfoByIds()->success:%s]", findUserInfoByIds.getMessage()));
				List<Map<String, Object>> successValue = findUserInfoByIds.getSuccessValue();
				return ResultBuilder.successResult(successValue, "查询成功!");
			}

		} catch (Exception e) {
			logger.error("getFactoryIdsByPersonId()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:通过关键字查询用户的id
	 * @param userKeyWord
	 * @return
	 */
	public Result<List<String>> findUserIdsByKey(JSONObject userKeyWord) {
		logger.info(String.format("[findUserIdsByKey()->request param:%s]", userKeyWord));
		try {

			if (userKeyWord == null) {
				logger.error(String.format("[findUserIdsByKey()->error:%s]", UscConstants.NO_BUSINESS));
				return ResultBuilder.failResult(UscConstants.NO_BUSINESS);
			}

			String keyword = userKeyWord.getString(UscConstants.USC_KEY_WORD);
			String accessSecret = userKeyWord.getString(UscConstants.ACCESS_SECRET);

			RpcResponse<List<String>> res = userQueryRpcService.findUserIdsByKey(keyword, accessSecret);
			if (res.isSuccess()) {
				logger.info("[findUserIdsByKey()->success:" + res.getMessage() + "]");
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("[findUserIdsByKey()->fail:" + res.getMessage() + "]");
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[findUserIdsByKey()->exception:" + e.getMessage() + "]");
			return ResultBuilder.exceptionResult(e);
		}

	}



	/**
	 * 
	 * @Description:查询用户的名称->ids
	 * @param userIdsJson
	 * @return
	 */
	public Result<Map<String, String>> findUserLoginAccout(JSONObject userIdsJson) {
		logger.info(String.format("[findUserLoginAccout()->request param:%s]", userIdsJson));
		try {

			if (userIdsJson == null) {
				logger.error(String.format("[findUserLoginAccout()->error:%s]", UscConstants.NO_BUSINESS));
				return ResultBuilder.failResult(UscConstants.NO_BUSINESS);
			}

			List<String> userIds = userIdsJson.getObject(UscConstants.USC_USER_IDS, List.class);

			RpcResponse<List> res = userQueryRpcService.findUserInfoByIds(userIds);
			if (res.isSuccess()) {

				logger.info("[findUserIdsByKey()->success:" + res.getMessage() + "]");
				Map<String, String> useridsMap = Maps.newHashMap();
				List successValue = res.getSuccessValue();
				for (Object object : successValue) {
					Map<String, String> userMap = (Map) object;
					useridsMap.put(userMap.get(UscConstants.ID_), userMap.get(UscConstants.USERNAME));
				}
				return ResultBuilder.successResult(useridsMap, res.getMessage());

			} else {
				logger.error("[findUserIdsByKey()->fail:" + res.getMessage() + "]");
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error("[findUserIdsByKey()->exception:" + e.getMessage() + "]");
			return ResultBuilder.exceptionResult(e);
		}

	}



	/**
	 * 
	 * @Description: 通过组织id获取所有的子类组织
	 * @param orgJson
	 * @return
	 */
	public Result<List<String>> getModelByParentId(JSONObject orgJson) {
		logger.info(String.format("[getModelByParentId()->request param:%s]", orgJson));
		try {
			if (orgJson == null || StringUtils.isBlank(orgJson.getString(UscConstants.ORGANIZED_ID))) {
				logger.error(String.format("[getModelByParentId()->error:%s]", UscConstants.NO_BUSINESS));
				return ResultBuilder.failResult(UscConstants.NO_BUSINESS);
			}

			RpcResponse<List<Map>> res = accSourceBaseQueryService
					.getModelByParentId(orgJson.getString(UscConstants.ORGANIZED_ID));

			if (res.isSuccess()) {

				List<Map> successValue = res.getSuccessValue();
				List<String> orgIds = Lists.newArrayList();
				for (Map orgMap : successValue) {
					orgIds.add(orgMap.get(UscConstants.ID_) + "");
				}

				logger.error(String.format("[getModelByParentId()->suc:%s]", res.getMessage()));
				return ResultBuilder.successResult(orgIds, res.getMessage());
			} else {
				logger.error(String.format("[getModelByParentId()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error(String.format("[getModelByParentId()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}

	}



	/**
	 * 
	 * @Description:根据资源id集合查询资源信息
	 * @param orgJsonIds
	 * @return
	 */
	public Result<Map<String, String>> findOrgName(JSONObject orgJsonIds) {
		logger.info(String.format("[findOrgName()->request param:%s]", orgJsonIds));
		try {

			if (orgJsonIds == null) {
				logger.error(String.format("[findOrgName()->error:%s]", UscConstants.NO_BUSINESS));
				return ResultBuilder.failResult(UscConstants.NO_BUSINESS);
			}

			List<String> sourceIds = orgJsonIds.getObject(UscConstants.USC_ORG_IDS, List.class);

			RpcResponse<List<Map<String, Object>>> res = accSourceBaseQueryService.getSourceMessByIds(sourceIds);

			if (res.isSuccess()) {

				logger.info("[findUserIdsByKey()->success:" + res.getMessage() + "]");
				Map<String, String> orgidsMap = Maps.newHashMap();

				List<Map<String, Object>> successValue = res.getSuccessValue();
				for (Map<String, Object> map : successValue) {
					orgidsMap.put(map.get(UscConstants.ID_) + "", map.get(UscConstants.SOURCE_NAME) + "");
				}

				return ResultBuilder.successResult(orgidsMap, res.getMessage());

			} else {
				logger.error("[findUserIdsByKey()->fail:" + res.getMessage() + "]");
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error(String.format("[findOrgName()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}

	}

}
