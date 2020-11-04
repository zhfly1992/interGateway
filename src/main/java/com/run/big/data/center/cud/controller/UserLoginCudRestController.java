/*
 * File name: UserLoginCudRestController.java
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

package com.run.big.data.center.cud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.run.big.data.center.cud.service.UserLoginCudRestService;
import com.run.big.data.center.entity.UrlConstant;
import com.run.entity.common.Result;

/**
 * @Description: 用户注册,修改信息
 * @author: guofeilong
 * @version: 1.0, 2018年3月21日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
@CrossOrigin(origins = "*")
public class UserLoginCudRestController {
	@Autowired
	private UserLoginCudRestService userLoginCudRestService;



	/**
	 * @param userInfo
	 *            {"loginAccount":"用户名","email":"邮箱地址","password":"密码","userType":"用户类型"}
	 * 
	 * 
	 * @return
	 * @Description: 用户注册
	 */

	@RequestMapping(value = UrlConstant.USER_REGISTER, method = RequestMethod.POST)
	public Result<Object> userRegister(@RequestBody String userInfo) {
		return userLoginCudRestService.userRegister(userInfo);
	}



	/**
	 * @param passwordInfo
	 *            {"password":"密码" token:token}
	 * @return
	 * @Description: 修改用户密码
	 */

	@RequestMapping(value = UrlConstant.CHANGE_PASSWORD, method = RequestMethod.PUT)
	public Result<Object> changePassword(@RequestBody String passwordInfo) {
		return userLoginCudRestService.changePassword(passwordInfo);
	}



	/**
	 * @param passwordInfo
	 *            {"password":"密码" "sendNum":验证码}
	 * @return
	 * @Description: 根据验证码重置密码
	 */

	@RequestMapping(value = UrlConstant.CHANGE_PASSWORD_BY_SENDNUM, method = RequestMethod.PUT)
	public Result<Object> changePasswordBySendNum(@RequestBody String passwordInfo) {
		return userLoginCudRestService.changePasswordBySendNum(passwordInfo);
	}



	/**
	 * @param changeInfo
	 *            验证码信息 {"sendNum":"验证码" token:token}
	 * @return
	 * @Description: 修改手机号
	 */

	@RequestMapping(value = UrlConstant.CHANGE_PHONE_NUM, method = RequestMethod.PUT)
	public Result<Object> changePhoneNum(@RequestBody String changeInfo) {
		return userLoginCudRestService.changePhoneNum(changeInfo);
	}



	/**
	 * @param token:token
	 * @return
	 * @Description: 用户退出
	 */

	@RequestMapping(value = UrlConstant.USER_LOGOUT, method = RequestMethod.DELETE)
	public Result<Object> logout(@PathVariable String token) {
		return userLoginCudRestService.logout(token);
	}



	@RequestMapping(value = UrlConstant.REFRESH_LOGIN_TIME, method = RequestMethod.POST)
	public Result<Boolean> refreshLoginTime(@RequestBody String userInfo) {
		return userLoginCudRestService.refreshLoginTime(userInfo);
	}

}
