/*
 * File name: UserLoginRestQueryService.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 wangsheng 2018年1月16日 ...
 * ... ...
 *
 ***************************************************/
package com.run.big.data.center.query.service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.run.authz.api.constants.AuthzConstants;
import com.run.authz.base.query.AuthzBaseQueryService;
import com.run.big.data.center.util.Des3Util;
import com.run.common.util.ExceptionChecked;
import com.run.common.util.ParamChecker;
import com.run.encryt.util.MD5;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;
import com.run.usc.api.constants.UscConstants;
import com.run.usc.base.query.UserBaseQueryService;

/**
 * @Description: 用户登录
 * @author: 王胜
 * @version: 1.0, 2018年01月16日
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserLoginRestQueryService {

	private static final Logger		logger	= Logger.getLogger(UserLoginRestQueryService.class);

	@Autowired
	UserBaseQueryService			userBaseQueryService;

	@Autowired
	private AuthzBaseQueryService	authzBaseQueryService;

	/** 铁塔code appCode */
	@Value("${businessAppcode:chinatowerIoTplatform}")
	private String					businessAppcode;

	/** 本地code appCode */
	@Value("${businessAppcode:chinatowerLocman}")
	private String					appCode;

	@Value("${checkTokenUrl:http://nbp.chinatowercom.cn/cms-pad/pad/url/verifyCode}")
	private String					checkTokenUrl;



	/**
	 * 
	 * @Description:用户登录验证
	 * @param userInfo
	 *            用户名和密码
	 * @return Result<Map>
	 */
	public Result<Map<String, Object>> userLogin(String userInfo) {
		logger.info(String.format("[findDeviceShadowById()->request params:%s]", userInfo));
		try {
			// 参数验证
			if (StringUtils.isBlank(userInfo)) {
				logger.error("userLogin()--参数不能为空!");
				return ResultBuilder.failResult("参数不能为空!");
			}
			if (ParamChecker.isNotMatchJson(userInfo)) {
				logger.error("userLogin()--参数需json格式!");
				return ResultBuilder.failResult("参数需json格式!");
			}
			JSONObject json = JSONObject.parseObject(userInfo);
			String loginAccount = json.getString(UscConstants.LOGIN_ACCOUNT);
			if (StringUtils.isBlank(loginAccount)) {
				logger.error("userLogin()--用户登录名不能为空!");
				return ResultBuilder.failResult("用户登录名不能为空!");
			}
			String password = json.getString(UscConstants.PASSWORD);
			if (StringUtils.isBlank(password)) {
				logger.error("userLogin()--用户登录密码不能为空!");
				return ResultBuilder.failResult("用户登录密码不能为空!");
			}
			// 用户登录校验
			RpcResponse<Map<String, Object>> resutl = userBaseQueryService.userAuthz(loginAccount, password);
			if (!resutl.isSuccess()) {
				if (UscConstants.PASSWORD_TIMEOUT.equals(resutl.getMessage())) {
					logger.error("userLogin()--用户登录:" + resutl.getMessage());
					//状态码1001:密码有效期过期
					return ResultBuilder.getResult(null, "1001", UscConstants.PASSWORD_TIMEOUT, null);
				}
				logger.error("userLogin()--用户登录:" + resutl.getMessage());
				return ResultBuilder.failResult(resutl.getMessage());
			}
			return ResultBuilder.successResult(resutl.getSuccessValue(), resutl.getMessage());
		} catch (Exception e) {
			logger.error("userLogin()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * @Description:用户登录验证
	 * @param userInfo
	 *            用户名和密码
	 * @return Result<Map>
	 */
	public Map<String, String> userLoginByCode(String code) {
		try {
			// 参数验证
			// @TODO
			logger.error("成功接收参数：" + appCode);

			String data = Des3Util.des3DecodeCBC("chinatowerLocman", code);
			logger.info("调用工具解密,解密后的内容：" + data);

			String token = data.replaceFirst(businessAppcode + "_" + appCode + "_", "");
			String re = com.run.http.client.util.HttpClientUtil.getInstance()
					.doPost(checkTokenUrl + "?code=" + URLEncoder.encode(token, "UTF-8"), "", "");
			JSONObject json = JSONObject.parseObject(re).getJSONObject("data");
			Map<String, String> map = new HashMap<>();
			map.put("userName", json.getString("userName"));
			map.put("password", MD5.encrytMD5(json.getString("password")).toUpperCase());
			return map;
		} catch (Exception e) {
			logger.error("userLogin()->exception", e);
			return new HashMap<>();
		}

	}



	/**
	 * @Description 发送手机或者邮箱验证
	 *
	 * @param messageInfo
	 * @return
	 */

	public Result<Object> sendEmailOrMessage(String messageInfo) {
		logger.info(String.format("[sendEmailOrMessage()->request params:%s]", messageInfo));
		try {
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(messageInfo);
			if (null != checResult) {
				logger.error(String.format("[sendEmailOrMessage()->fail:%s]", checResult.getException()));
				return checResult;
			}

			JSONObject emailMobInfoJson = JSON.parseObject(messageInfo);

			// 获取电话号码或者邮箱
			String emailMob = emailMobInfoJson.getString(UscConstants.EMAIL_MOBILE);
			if (StringUtils.isBlank(emailMob)) {
				logger.error("sendEmailOrMessage()--电话号码或者邮箱不能为空!");
				return ResultBuilder.invalidResult();
			}
			String type = emailMobInfoJson.getString(UscConstants.TYPE);
			if (StringUtils.isBlank(type)) {
				logger.error("sendEmailOrMessage()--type不能为空!");
				return ResultBuilder.invalidResult();
			}
			String loginAccount = emailMobInfoJson.getString(UscConstants.LOGIN_ACCOUNT);
			RpcResponse res = userBaseQueryService.sendEmiMob(emailMob, type, loginAccount);
			if (res.isSuccess()) {
				logger.info(String.format("[sendEmailOrMessage()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[sendEmailOrMessage()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error(String.format("[sendEmailOrMessage()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 检查用户手机号或者邮箱是否存在
	 *
	 * @param checkInfo
	 * @return
	 */

	public Result<Object> checkUserExitByEmiMob(String emailMob) {
		logger.info(String.format("[sendEmailOrMessage()->request params:emailMob:%s]", emailMob));
		try {
			if (StringUtils.isBlank(emailMob)) {
				logger.error("sendEmailOrMessage()--电话号码或者邮箱不能为空!");
				return ResultBuilder.invalidResult();
			}
			RpcResponse res = userBaseQueryService.checkUserExitByEmiMob(emailMob);
			if (res.isSuccess()) {
				logger.info(String.format("[checkUserExitByEmiMob()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[checkUserExitByEmiMob()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error(String.format("[checkUserExitByEmiMob()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 手机验证码验证身份(通过手机修改密码时)
	 *
	 * @param verificationCode
	 * @param phoneNum
	 * @return
	 */

	public Result<Object> checkVerificationCodeExist(String verificationCode, String phoneNum) {
		logger.info("[checkVerificationCodeExist()->request param:验证码:" + verificationCode + "手机号:" + phoneNum + "]");
		try {
			if (StringUtils.isBlank(verificationCode)) {
				logger.error("checkVerificationCodeExist()--验证码不能为空!");
				return ResultBuilder.invalidResult();
			}
			if (StringUtils.isBlank(phoneNum)) {
				logger.error("checkVerificationCodeExist()--验证码不能为空!");
				return ResultBuilder.invalidResult();
			}
			JSONObject json = new JSONObject();

			json.put(AuthzConstants.TOKEN, verificationCode);
			json.put(AuthzConstants.USER_ID, phoneNum);
			JSONObject quInfo = new JSONObject();
			quInfo.put(AuthzConstants.QU_INFO, json);
			RpcResponse res = authzBaseQueryService.authenticate(quInfo);
			if (res.isSuccess()) {
				logger.info("[checkVerificationCodeExist()->success:" + res.getMessage() + "]");
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error("[checkVerificationCodeExist()->fail:" + res.getMessage() + "]");
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[checkVerificationCodeExist()->exception:]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}
	
	public Result<Object> getPublicKey(){
		try {
			logger.info(String.format("[getPublicKey()->获取登录加密公钥，进入方法]"));
			RpcResponse<String> findPublicKeyForLoginEncode = userBaseQueryService.findPublicKeyForLoginEncode();
			if (!findPublicKeyForLoginEncode.isSuccess()) {
				logger.error("getPublicKey()--获取公钥失败:" + findPublicKeyForLoginEncode.getMessage());
				return ResultBuilder.failResult(findPublicKeyForLoginEncode.getMessage());
			}
			return ResultBuilder.successResult(findPublicKeyForLoginEncode.getSuccessValue(), findPublicKeyForLoginEncode.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("[getPublicKey()->exception:]",e);
			return ResultBuilder.exceptionResult(e);
		}
	
	}
	
	
	public Result sendEmiMob(String emailMobInfo) {
		logger.info(String.format("[sendEmiMob()->request param:%s]", emailMobInfo));
		try {
			// 参数基础校验
			Result checResult = ExceptionChecked.checkRequestParam(emailMobInfo);
			if (null != checResult) {
				logger.error(String.format("[sendEmiMob()->fail:%s]", checResult.getException()));
				return checResult;
			}

			JSONObject emailMobInfoJson = JSON.parseObject(emailMobInfo);

			// 获取电话号码或者邮箱
			String emailMob = emailMobInfoJson.getString(UscConstants.EMAIL_MOBILE);
			String type = emailMobInfoJson.getString(UscConstants.TYPE);
			String loginAccount = emailMobInfoJson.getString(UscConstants.LOGIN_ACCOUNT);
			RpcResponse res = userBaseQueryService.sendEmiMob(emailMob, type, loginAccount);
			if (res.isSuccess()) {
				logger.info(String.format("[sendEmiMob()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[sendEmiMob()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error(String.format("[sendEmiMob()->exception:%s]", e.getMessage()));
			return ResultBuilder.exceptionResult(e);
		}
	}

}
