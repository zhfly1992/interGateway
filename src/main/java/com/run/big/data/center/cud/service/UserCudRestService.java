/*
 * File name: UserCudRestService.java
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
import com.run.big.data.center.util.CheckJsonParamUtils;
import com.run.common.util.ExceptionChecked;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;
import com.run.usc.api.base.crud.AccUserBaseCrudService;
import com.run.usc.api.base.crud.TenAccBaseCrudService;
import com.run.usc.api.constants.UscConstants;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2018年3月20日
 */
@Service
public class UserCudRestService {

	private static final Logger		logger	= Logger.getLogger(UserCudRestService.class);

	@Autowired
	private AccUserBaseCrudService	accUserBaseCrudService;

	@Autowired
	private TenAccBaseCrudService	accCrudService;



	/**
	 * @Description
	 *
	 * @param userInfo
	 *            { "userName":"用户名", "loginAccount":"账号", "mobile":"手机",
	 *            "accessSecret":"密钥",
	 *            "roleInfo":[{"organizeId":"组织id","sourceName":"组织名称","roleId":"角色id","roleName":"角色"}],
	 *            "factory":"厂家", "peopleType":"人员类型id", "receiveSms":"是否接收短信",
	 *            "remark":"备注", "password":"密码" }
	 * 
	 * @return
	 */

	public Result<JSONObject> addUser(String userInfo) {
		logger.info(String.format("[addUser()->request param:%s]", userInfo));
		try {
			// 参数基础校验
			Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(userInfo);
			if (null != checResult) {
				logger.error("参数不符合json格式" + userInfo);
				return checResult;
			}
			// 参数必填字段校验

			RpcResponse<JSONObject> rs = CheckJsonParamUtils.checkRequestKey(logger, "addUser", userInfo,
					UscConstants.ACCESS_SECRET, UscConstants.USERNAME, UscConstants.LOGIN_ACCOUNT,
					UscConstants.PASSWORD, UscConstants.MOBILE, UscConstants.ROLE_INFO);
			if (rs != null) {
				return ResultBuilder.failResult(rs.getMessage());
			}
			JSONObject info = JSON.parseObject(userInfo);
			if (!info.containsKey("factory")) {
				logger.error("[addUser()->invalid：factory字段不存在！]");
				return ResultBuilder.noBusinessResult();
			}
			if (!info.containsKey("remark")) {
				logger.error("[addUser()->invalid：remark字段不存在！]");
				return ResultBuilder.noBusinessResult();
			}

			RpcResponse<JSONObject> res = accUserBaseCrudService.saveUserRs(JSON.parseObject(userInfo));
			if (res.isSuccess()) {
				logger.info("addUser--->" + res.getMessage());
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("addUser()-->" + res.getMessage());
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("addUser()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param userInfo
	 * @param id
	 * @return
	 */

	public Result<JSONObject> updateUser(String userInfo, String id) {
		logger.info(String.format("[updateUser()->request param:%s]", userInfo));
		try {
			// 参数基础校验
			Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(userInfo);
			if (null != checResult) {
				logger.error("参数不符合json格式" + userInfo);
				return checResult;
			}
			// 参数必填字段校验
			RpcResponse<JSONObject> rs = CheckJsonParamUtils.checkRequestKey(logger, "updateUser", userInfo,
					UscConstants.ACCESS_SECRET, UscConstants.USERNAME, UscConstants.LOGIN_ACCOUNT,
					UscConstants.PASSWORD, UscConstants.MOBILE, UscConstants.ROLE_INFO);
			if (rs != null) {
				return ResultBuilder.failResult(rs.getMessage());
			}
			JSONObject info = JSON.parseObject(userInfo);
			if (!info.containsKey("factory")) {
				logger.error("[updateUser()->invalid：factory不能为空！]");
				return ResultBuilder.noBusinessResult();
			}
			if (!info.containsKey("remark")) {
				logger.error("[updateUser()->invalid：remark不能为空！]");
				return ResultBuilder.noBusinessResult();
			}
			info.put(UscConstants.ID, id);
			RpcResponse<JSONObject> res = accUserBaseCrudService.updateUserRs(info);
			if (res.isSuccess()) {
				logger.info("updateUser--->" + res.getMessage());
				return ResultBuilder.successResult(null, res.getMessage());
			} else {
				logger.error("updateUser()-->" + res.getMessage());
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("updateUser()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param id
	 * @param manageInfo
	 * @return
	 */

	public Result<JSONObject> manageUserState(String id, String manageInfo) {
		logger.info(String.format("[manageUserState()->request param:%s,%s]", id, manageInfo));
		// 参数基础校验
		Result<JSONObject> checResult = ExceptionChecked.checkRequestParam(manageInfo);
		if (null != checResult) {
			logger.error("参数不符合json格式" + manageInfo);
			return checResult;
		}
		if (StringUtils.isBlank(id)) {
			logger.error("[manageUserState()->invalid：id不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			JSONObject info = JSON.parseObject(manageInfo);
			String state = info.getString(UscConstants.STATE);
			RpcResponse<JSONObject> res = accUserBaseCrudService.swateUserState(id, state);
			if (res.isSuccess()) {
				logger.info("manageUserState--->" + res.getMessage());
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("manageUserState--->" + res.getMessage());
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("manageUserState()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param id
	 * @param manageInfo
	 * @return
	 */

	public Result<String> updateUserCompany(String id, String companyInfo) {
		logger.info(String.format("[updateUserCompany()->request param:%s,%s]", id, companyInfo));
		// 参数基础校验
		try {
			Result<String> checResult = ExceptionChecked.checkRequestParam(companyInfo);
			if (null != checResult) {
				logger.error("参数不符合json格式" + companyInfo);
				return checResult;
			}
			if (StringUtils.isBlank(id)) {
				logger.error("[updateUserCompany()->invalid：id不能为空！]");
				return ResultBuilder.invalidResult();
			}
			JSONObject info = JSON.parseObject(companyInfo);
			info.put("_id", id);
			RpcResponse<String> res = accCrudService.updateAccessInfo(info);
			if (res.isSuccess()) {
				logger.info("updateUserCompany--->" + res.getMessage());
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("updateUserCompany--->" + res.getMessage());
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("updateUserCompany()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
