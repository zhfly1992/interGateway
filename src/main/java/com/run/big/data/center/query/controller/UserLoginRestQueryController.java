/*
* File name: UserLoginRestQueryController.java								
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
* 1.0			王胜		2018年1月16日
* ...			...			...
*
***************************************************/
package com.run.big.data.center.query.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.run.big.data.center.entity.UrlConstant;
import com.run.big.data.center.query.service.UserLoginRestQueryService;
import com.run.entity.common.Result;

/**
 * @Description:用户登录平台Cotr
 * @author: 王胜
 * @version: 1.0, 2018年01月16日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
@CrossOrigin(origins = "*")
public class UserLoginRestQueryController {
	

	@Autowired
	private UserLoginRestQueryService userLoginRestQueryService;

	@Value("${redictAddress:http://131.10.10.140:8000/#/ironLoginJump}")
	private String	redictAddress;


	/**
	 * 
	 * @Description:用户登录验证
	 * @param userInfo
	 *            
	 *            {
					"loginAccount":"登录名",
					"password":"密码",
					"userType":"用户类型"
					}

	 * @return Result<Map>
	 */
	@RequestMapping(value = UrlConstant.USER_LOGIN, method = RequestMethod.POST)
	public Result<Map<String, Object>> userLogin(@RequestBody String userInfo) {
		return userLoginRestQueryService.userLogin(userInfo);
	}
	
	/**
	 * 
	 * @Description:用户登录验证
	 * @param userInfo
	 *            
	 *            {
					"loginAccount":"登录名",
					"password":"密码",
					"userType":"用户类型"
					}

	 * @return Result<Map>
	 */
	@RequestMapping(value = UrlConstant.USER_LOGIN_APPCODE, method = RequestMethod.GET)
	public ModelAndView userLoginByAppcode(@RequestParam(value = "code",required = true) String code) {
		 Map<String,String> map=userLoginRestQueryService.userLoginByCode(code);
		 ModelAndView modelAndView = new ModelAndView();
		 String address="redirect:"+redictAddress+"?username="+map.get("userName")+"&password="+map.get("password");
		 System.out.println(address);
		 modelAndView.setViewName(address);
		 return modelAndView;
	}
	
	
	/**
	 * 
	 * @Description:发送手机或者邮箱验证
	 * @param messageInfo{"emailMob":"tangheng@sefon.com","type":"email"}
	 * @return 
	 */
	@RequestMapping(value = UrlConstant.SENDEMIMOB, method = RequestMethod.POST)
	public Result<Object> sendEmailOrMessage(@RequestBody String messageInfo) {
		return userLoginRestQueryService.sendEmailOrMessage(messageInfo);
	}
	
	/**
	 * 
	 * @Description:检查用户手机号或者邮箱是否存在
	 * @param checkInfo{"emailMob":"邮箱或手机号码"}
	 * @return 
	 */
	@RequestMapping(value = UrlConstant.CHECK_USER_EXIT, method = RequestMethod.GET)
	public Result<Object> checkUserExitByEmiMob(@RequestParam(value = "emailMob",required = true) String emailMob) {
		return userLoginRestQueryService.checkUserExitByEmiMob(emailMob);
	}
	
	/**
	 * 
	 * @Description:手机验证码验证身份(通过手机修改密码时)
	 * @param checkInfo{"verificationCode":"验证码 ","phoneNum":"手机号"}
	 * @return 
	 */
	@RequestMapping(value = UrlConstant.CHECK_VERIFICATION_CODE_EXIT, method = RequestMethod.GET)
	public Result<Object> checkVerificationCodeExist(@PathVariable String verificationCode, @PathVariable String phoneNum) {
		return userLoginRestQueryService.checkVerificationCodeExist(verificationCode,phoneNum);
	}
	/**
	 * 
	 * @Description:获取密码加密时的公共密匙
	 * @return
	 * @author :zh
	 * @version 2020年10月20日
	 */
	@RequestMapping(value = UrlConstant.GET_PUBLIC_KEY,method = RequestMethod.GET)
	public Result<Object> getPublicKeyForLoginEncode(){
		return userLoginRestQueryService.getPublicKey();
	}
	
	/**
	 * 
	 * @Description:发送手机或者短信验证(新)
	 * @param emailMobInfo
	 *            手机或者邮箱信息，type emial or phone
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.GET_SENDEMIMOB, method = RequestMethod.POST)
	public Result sendEmiMob(@RequestBody String emailMobInfo) {
		return	userLoginRestQueryService.sendEmiMob(emailMobInfo);
	}
}
