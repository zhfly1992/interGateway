/*
 * File name: OrganizationCudRestService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2018年3月20日 ...
 * ... ...
 *
 ***************************************************/

package com.run.big.data.center.cud.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.run.authz.api.base.crud.UserRoleBaseCrudService;
import com.run.common.util.ExceptionChecked;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;
import com.run.usc.api.base.crud.AccSourceBaseCrudService;
import com.run.usc.api.constants.UscConstants;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2018年3月20日
 */
@Service
public class OrganizationCudRestService {
	private static final Logger			logger	= Logger.getLogger(OrganizationCudRestService.class);

	@Autowired
	private AccSourceBaseCrudService	accSourceBaseCrudService;

	@Autowired
	private UserRoleBaseCrudService		userRoleBaseCrudService;



	/**
	 * @Description
	 *
	 * @param organizationInfo
	 *            "accessSecret":"密钥", "organizationName":"组织名",
	 *            "organizationType":"组织资源类型", "organizationDecs":"备注"
	 *            "parentId":"父组织id",
	 * @return
	 */

	public Result<JSONObject> addOrganization(String organizationInfo) {
		logger.info("[addOrganization()->request param:%s]" + organizationInfo);
		try {
			// 参数基础校验
			Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(organizationInfo);
			if (null != checResult) {
				logger.error("参数不符合json格式" + organizationInfo);
				return checResult;
			}

			JSONObject info = JSON.parseObject(organizationInfo);
			String accessSecret = info.getString(UscConstants.ACCESS_SECRET);
			if (StringUtils.isBlank(accessSecret)) {
				logger.error("[addOrganization()->invalid：accessSecret不能为空！]");
				return ResultBuilder.invalidResult();
			}
			String organizationName = info.getString("organizationName");
			if (StringUtils.isBlank(organizationName)) {
				logger.error("[addOrganization()->invalid：organizationName不能为空！]");
				return ResultBuilder.invalidResult();
			}

			if (!info.containsKey("organizationDecs")) {
				logger.error("[addOrganization()->invalid：organizationDecs不能为空！]");
				return ResultBuilder.noBusinessResult();
			}
			String organizationDecs = info.getString("organizationDecs");

			JSONObject json = new JSONObject();
			json.put(UscConstants.ACCESS_SECRET, accessSecret);
			json.put(UscConstants.SOURCE_NAME, organizationName);
			json.put(UscConstants.SOURCE_TYPE, "sourceTypeOrganize"); // 新增组织,资源类型只能是组织资源类型
			json.put("sourceDecs", organizationDecs);
			String parentId = info.getString("parentId");
			if (parentId != null) {
				json.put(UscConstants.PARENT_ID, parentId);
			}
			RpcResponse<JSONObject> res = accSourceBaseCrudService.saveAccSourceInfoBySecret(json);
			if (res.isSuccess()) {
				logger.info("addOrganization--->" + res.getMessage());
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("addOrganization()-->" + res.getMessage());
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("addOrganization()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param accessSecret
	 * @param organizationInfo
	 * @param organizationInfo2
	 * @return
	 */

	public Result<JSONObject> updateOrganizationByAS(String accessSecret, String organizationid,
			String organizationInfo) {
		logger.info("[updateOrganizationByAS()->request param:%s]" + organizationInfo);
		try {
			// 参数基础校验
			Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(organizationInfo);
			if (null != checResult) {
				logger.error("参数不符合json格式" + organizationInfo);
				return checResult;
			}

			if (StringUtils.isBlank(UscConstants.ACCESS_SECRET)) {
				logger.error("[updateOrganizationByAS()->invalid：accessSecret不能为空！]");
				return ResultBuilder.invalidResult();
			}
			if (StringUtils.isBlank(organizationid)) {
				logger.error("[updateOrganizationByAS()->invalid：organizationid不能为空！]");
				return ResultBuilder.invalidResult();
			}
			JSONObject info = JSON.parseObject(organizationInfo);

			String organizationName = info.getString("organizationName");
			if (StringUtils.isBlank(organizationName)) {
				logger.error("[updateOrganizationByAS()->invalid：organizationName不能为空！]");
				return ResultBuilder.invalidResult();
			}
			if (!info.containsKey("organizationDecs")) {
				logger.error("[updateOrganizationByAS()->invalid：organizationDecs不能为空！]");
				return ResultBuilder.noBusinessResult();
			}
			String organizationDecs = info.getString("organizationDecs");

			JSONObject json = new JSONObject();
			json.put(UscConstants.ACCESS_SECRET, accessSecret);
			json.put(UscConstants.SOURCE_NAME, organizationName);
			json.put(UscConstants.SOURCE_TYPE, "sourceTypeOrganize"); // 修改组织,资源类型只能是组织资源类型
			json.put("sourceDecs", organizationDecs);
			json.put(UscConstants.ID, organizationid);
			RpcResponse<JSONObject> res = accSourceBaseCrudService.updateSourceBySecret(json);
			if (res.isSuccess()) {
				logger.info("updateOrganizationByAS--->" + res.getMessage());
				// 修改该组织人员下的名称
				RpcResponse<String> updateUserAsOrgInfo = userRoleBaseCrudService.updateUserAsOrgInfo(accessSecret,
						organizationName, organizationid);
				if (!updateUserAsOrgInfo.isSuccess()) {
					logger.error("updateOrganizationByAS--->" + updateUserAsOrgInfo.getMessage());
					return ResultBuilder.failResult(updateUserAsOrgInfo.getMessage());
				}

				logger.info("updateOrganizationByAS--->" + updateUserAsOrgInfo.getMessage());
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("updateOrganizationByAS()-->" + res.getMessage());
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error("updateOrganizationByAS()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param accessSecret
	 * @param manageInfo
	 * @return
	 */

	public Result<String> manageOrganizationState(String organizationId, String manageInfo) {
		logger.info(String.format("[manageOrganizationState()->request params:%s,%s]", organizationId, manageInfo));
		// 参数基础校验
		Result<String> checResult = ExceptionChecked.checkRequestParam(manageInfo);
		if (null != checResult) {
			logger.error("参数不符合json格式" + manageInfo);
			return checResult;
		}
		if (StringUtils.isBlank(organizationId)) {
			logger.error("[manageOrganizationState()->invalid：organizationId不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			JSONObject info = JSON.parseObject(manageInfo);
			String state = info.getString(UscConstants.STATE);
			RpcResponse<String> res = accSourceBaseCrudService.swateSourceState(organizationId, state);
			if (res.isSuccess()) {
				logger.info("manageOrganizationState--->" + res.getMessage());
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("manageOrganizationState--->" + res.getMessage());
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("manageOrganizationState()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}

	}

}
