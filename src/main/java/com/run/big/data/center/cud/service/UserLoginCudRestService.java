/*
 * File name: UserLoginCudRestService.java
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
import com.alibaba.fastjson.JSONObject;
import com.run.big.data.center.util.CheckJsonParamUtils;
import com.run.common.util.ExceptionChecked;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;
import com.run.usc.api.base.crud.UserBaseCurdService;
import com.run.usc.api.constants.UscConstants;

/**
 * @Description:
 * @author: guofeilong
 * @version: 1.0, 2018年3月21日
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserLoginCudRestService {
	private static final Logger	logger	= Logger.getLogger(UserLoginCudRestService.class);

	@Autowired
	private UserBaseCurdService	userBaseCurdService;



	/**
	 * @Description
	 *
	 * @param userInfo
	 * @return
	 */
	public Result<Object> userRegister(String userInfo) {
		logger.info(String.format("[userRegister()->request param:%s]", userInfo));
		try {
			Result checResult = ExceptionChecked.checkRequestParam(userInfo);
			if (null != checResult) {
				logger.error(String.format("[userRegister()->fail:%s]", checResult.getException()));
				return checResult;
			}
			// 注册的json信息
			JSONObject json = JSON.parseObject(userInfo);
			RpcResponse<Object> checkRequestKey = CheckJsonParamUtils.checkRequestKey(logger, "userRegister", json,
					UscConstants.LOGIN_ACCOUNT, UscConstants.EMAIL, UscConstants.PASSWORD, UscConstants.USERTYPE);
			if (checkRequestKey != null) {
				logger.error("参数非法:" + checkRequestKey.getMessage());
				ResultBuilder.invalidResult();
			}
			RpcResponse res = userBaseCurdService.registerUser(json);
			if (res.isSuccess()) {
				logger.info(String.format("[userRegister()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[userRegister()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[userRegister()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param passwordInfo
	 * @return
	 */

	public Result<Object> changePassword(String passwordInfo) {
		logger.info(String.format("[changePassword()->request param:%s]", passwordInfo));
		try {
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(passwordInfo);
			if (null != checResult) {
				logger.error(String.format("[changePassword()->fail:%s]", checResult.getException()));
				return checResult;
			}
			JSONObject passJson = JSON.parseObject(passwordInfo);
			// 获取密码
			String password = passJson.getString(UscConstants.PASSWORD);
			if (StringUtils.isBlank(password)) {
				logger.error("password不能为空");
				ResultBuilder.invalidResult();
			}
			String token = passJson.getString(UscConstants.TOKEN);
			if (StringUtils.isBlank(token)) {
				logger.error("token不能为空");
				ResultBuilder.invalidResult();
			}
			RpcResponse res = userBaseCurdService.updatePassword(token, password);

			if (res.isSuccess()) {
				logger.info(String.format("[changePassword()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[changePassword()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[changePassword()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 修改手机号
	 *
	 * @param sendNumInfo
	 * @return
	 */

	public Result<Object> changePhoneNum(String sendNumInfo) {
		logger.info(String.format("[changePhoneNum()->request param:%s]", sendNumInfo));
		try {
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(sendNumInfo);
			if (null != checResult) {
				logger.error(String.format("[changePhoneNum()->fail:%s]", checResult.getException()));
				return checResult;
			}

			JSONObject json = JSON.parseObject(sendNumInfo);

			// 获取验证码
			String sendNum = json.getString(UscConstants.SEND_NUM);
			if (StringUtils.isBlank(sendNum)) {
				logger.error("验证码不能为空");
				ResultBuilder.invalidResult();
			}
			String token = json.getString(UscConstants.TOKEN);
			if (StringUtils.isBlank(token)) {
				logger.error("token不能为空");
				ResultBuilder.invalidResult();
			}
			RpcResponse res = userBaseCurdService.updatemobile(token, sendNum);

			if (res.isSuccess()) {
				logger.info(String.format("[changePhoneNum()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[changePhoneNum()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[changePhoneNum()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 退出登录
	 *
	 * @param token
	 * @return
	 */

	public Result<Object> logout(String token) {
		logger.info(String.format("[logout()->request param:%s]", token));
		try {
			if (StringUtils.isBlank(token)) {
				logger.error("logout()--->token不能为空");
				ResultBuilder.invalidResult();
			}
			RpcResponse res = userBaseCurdService.loginout(token);
			if (res.isSuccess()) {
				logger.info(String.format("[logout()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[logout()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[loginout()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 根据验证码重置密码
	 *
	 * @param passwordInfo
	 * @return
	 */

	public Result<Object> changePasswordBySendNum(String passwordInfo) {
		logger.info(String.format("[changePasswordBySendNum()->request param:%s]", passwordInfo));
		try {
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(passwordInfo);
			if (null != checResult) {
				logger.error(String.format("[changePasswordBySendNum()->fail:%s]", checResult.getException()));
				return checResult;
			}

			JSONObject sendfoJson = JSON.parseObject(passwordInfo);

			// 获取电话号码或者邮箱
			String newPassword = sendfoJson.getString(UscConstants.PASSWORD);
			if (StringUtils.isBlank(newPassword)) {
				logger.error("changePasswordBySendNum()--->密码不能为空");
				ResultBuilder.invalidResult();
			}
			String sendNum = sendfoJson.getString(UscConstants.SEND_NUM);
			if (StringUtils.isBlank(sendNum)) {
				logger.error("changePasswordBySendNum()--->验证码不能为空");
				ResultBuilder.invalidResult();
			}
			String type = sendfoJson.getString(UscConstants.TYPE);
			if (StringUtils.isBlank(type)) {
				logger.error("changePasswordBySendNum()--->手机还是邮箱类型不能为空");
				ResultBuilder.invalidResult();
			}
			RpcResponse res = userBaseCurdService.resetPasswordByAuthz(newPassword, sendNum, type);
			if (res.isSuccess()) {
				logger.info(String.format("[changePasswordBySendNum()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[changePasswordBySendNum()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[changePasswordBySendNum()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:通过用户id刷新登录时间
	 * @param userId
	 * @return
	 */
	public Result<Boolean> refreshLoginTime(String userId) {
		logger.info(String.format("[refreshLoginTime()->request param:%s]", userId));
		try {
			Result<Boolean> checResult = ExceptionChecked.checkRequestParam(userId);
			if (checResult != null) {
				logger.error(String.format("[refreshLoginTime()->fail:%s]", UscConstants.USER_ID));
				return checResult;
			}

			JSONObject userInfo = JSONObject.parseObject(userId);

			// 调用rpc
			RpcResponse<Boolean> res = userBaseCurdService.refreshLoginTime(userInfo.getString(UscConstants.USER_ID));
			if (res.isSuccess()) {
				logger.info(String.format("[refreshLoginTime()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[refreshLoginTime()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[refreshLoginTime()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
