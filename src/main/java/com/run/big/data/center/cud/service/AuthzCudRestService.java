/*
 * File name: AuthzCudRestService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2018年3月21日 ...
 * ... ...
 *
 ***************************************************/

package com.run.big.data.center.cud.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.run.authz.api.base.crud.UserRoleBaseCrudService;
import com.run.authz.api.constants.AuthzConstants;
import com.run.authz.base.query.UserRoleBaseQueryService;
import com.run.big.data.center.util.CheckJsonParamUtils;
import com.run.common.util.ExceptionChecked;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2018年3月21日
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AuthzCudRestService {
	private static final Logger			logger	= Logger.getLogger(AuthzCudRestService.class);

	@Autowired
	private UserRoleBaseCrudService		userRoleBaseCrudService;

	@Autowired
	private UserRoleBaseQueryService	userRoleBaseQueryService;



	/**
	 * @Description 保存用户角色以及他的关联信息（组织以及菜单权限）
	 *
	 * @param userRoleRSInfo
	 * @return
	 */

	public Result<JSONObject> addUserRoleRS(String userRoleRSInfo) {
		logger.info(String.format("[addUserRoleRS()->request params:%s]", userRoleRSInfo));
		try {
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(userRoleRSInfo);
			if (null != checResult) {
				logger.error(String.format("[addUserRoleRS()->fail:%s]", userRoleRSInfo));
				return checResult;
			}
			JSONObject json = JSON.parseObject(userRoleRSInfo);

			JSONArray orgArray = json.getJSONArray(AuthzConstants.ORG_MESS);
			if (StringUtils.isBlank(json.getString("roleName"))) {
				logger.error(String.format("[addUserRoleRS()->fail:%s]", "岗位名称roleName不能为空!"));
				return ResultBuilder.failResult("组织id不能为空!");
			}
			if (orgArray == null || orgArray.isEmpty()) {
				logger.error(String.format("[addUserRoleRS()->fail:%s]", "岗位名称roleName不能为空!"));
				return ResultBuilder.failResult("组织id不能为空!");
			}

			// 岗位在同组织下名称重复校验
			for (int i = 0; i < orgArray.size(); i++) {
				RpcResponse<Boolean> res = userRoleBaseQueryService.checkOrgRoleName(orgArray.get(i) + "",
						json.getString("roleName"));
				if (res.isSuccess() && res.getSuccessValue()) {
					logger.error(String.format("[checkOrgRoleName()->valid:%s]",
							"该岗位名称在组织id为" + orgArray.get(i) + "下已经存在,不能添加!"));
					return ResultBuilder.failResult("该岗位名称在组织id为" + orgArray.get(i) + "下已经存在,不能添加!");
				} else if (!res.isSuccess()) {
					logger.error(String.format("[checkOrgRoleName()->fail:%s]", res.getMessage()));
					return ResultBuilder.failResult(res.getMessage());
				}
			}

			if (!json.containsKey(AuthzConstants.REMARK)) {
				logger.error("[addUserRoleRS()->invalid：" + AuthzConstants.REMARK + "字段不存在！]");
				return ResultBuilder.noBusinessResult();
			}

			RpcResponse<Object> checkRequestKey = CheckJsonParamUtils.checkRequestKey(logger, "addUserRoleRS", json,
					AuthzConstants.ACCESS_SECRET, AuthzConstants.ROLE_NAME, AuthzConstants.ORG_MESS,
					AuthzConstants.PERMI_ARRAY);
			if (checkRequestKey != null) {
				return ResultBuilder.invalidResult();
			}
			RpcResponse<JSONObject> res = userRoleBaseCrudService.saveUserRoleRsInfo(json);
			if (res.isSuccess()) {
				logger.info(String.format("[addUserRoleRS()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[addUserRoleRS()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[addUserRoleRS()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 修改用户角色以及他的关联信息（组织以及菜单权限）
	 *
	 * @param userRoleRSInfo
	 * @param roleId
	 * @return
	 */

	public Result<JSONObject> updateUserRoleRS(String userRoleRSInfo, String roleId) {
		logger.info(String.format("[addUserRoleRS()->request params:%s,%s]", userRoleRSInfo, roleId));
		try {
			if (StringUtils.isBlank(roleId)) {
				logger.error(String.format("[updateUserRoleRS()->fail:%s]", roleId));
				return ResultBuilder.invalidResult();
			}
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(userRoleRSInfo);
			if (null != checResult) {
				logger.error(String.format("[updateUserRoleRS()->fail:%s]", userRoleRSInfo));
				return checResult;
			}
			JSONObject json = JSON.parseObject(userRoleRSInfo);
			if (!json.containsKey(AuthzConstants.REMARK)) {
				logger.error("[addUserRoleRS()->invalid：" + AuthzConstants.REMARK + "字段不存在！]");
				return ResultBuilder.noBusinessResult();
			}
			RpcResponse<Object> checkRequestKey = CheckJsonParamUtils.checkRequestKey(logger, "addUserRoleRS", json,
					AuthzConstants.ACCESS_SECRET, AuthzConstants.ROLE_NAME, AuthzConstants.OLD_ORG_MESS,
					AuthzConstants.NEW_ORG_MESS, AuthzConstants.PERMI_ARRAY);
			if (checkRequestKey != null) {
				return ResultBuilder.invalidResult();
			}
			json.put(AuthzConstants.ROLE_ID, roleId);
			RpcResponse<JSONObject> res = userRoleBaseCrudService.updateUserRoleRsInfo(json);
			if (res.isSuccess()) {
				logger.info(String.format("[updateUserRoleRS()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[updateUserRoleRS()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[updateUserRoleRS()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 管理组织启/停用状态
	 *
	 * @param roleId
	 * @param manageInfo
	 * @return
	 */

	public Result<String> managerRoleState(String roleId, String manageInfo) {
		logger.info(String.format("[addUserRoleRS()->request params:%s,%s]", roleId, manageInfo));
		try {
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(manageInfo);
			if (null != checResult) {
				logger.error(String.format("[managerRoleState()->fail:%s]", manageInfo));
				return checResult;
			}

			JSONObject swateInfo = JSON.parseObject(manageInfo);
			String state = swateInfo.getString(AuthzConstants.STATE);

			RpcResponse<String> res = userRoleBaseCrudService.swateUserRoleState(roleId, state);
			if (res.isSuccess()) {
				logger.info(String.format("[managerRoleState()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[managerRoleState()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[managerRoleState()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}
}
