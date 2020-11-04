/*
* File name: UserCudRestController.java								
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
* 1.0			guofeilong		2018年3月20日
* ...			...			...
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

import com.alibaba.fastjson.JSONObject;
import com.run.big.data.center.cud.service.UserCudRestService;
import com.run.big.data.center.entity.UrlConstant;
import com.run.entity.common.Result;

/**
 * @Description: 人员管理cud
 * @author: guofeilong
 * @version: 1.0, 2018年3月20日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
public class UserCudRestController {

	@Autowired
	private UserCudRestService userCudRestService;

	/**
	 * @param userInfo
	 *            新增人员的信息 { "userName":"用户名", "loginAccount":"账号",
	 *            "mobile":"手机", "accessSecret":"密钥",
	 *            "roleInfo":[{"organizeId":"组织id","organizationName":"组织名称","roleId":"角色id","roleName":"角色名称"}],
	 *            "factory":"厂家", "peopleType":"人员类型id", "receiveSms":"是否接收短信",
	 *            "remark":"备注", "password":"密码" }
	 * 
	 * 
	 * 
	 * @return
	 * @Description: 新增人员(关联组织，关联角色)RS
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.USER_SAVE, method = RequestMethod.POST)
	public Result<JSONObject> addUser(@RequestBody String userInfo) {
		return userCudRestService.addUser(userInfo);
	}
	
	/**
	 * @param userInfo
	 *            修改人员的信息 { "userName":"用户名", "loginAccount":"账号",
	 *            "mobile":"手机", "accessSecret":"密钥",
	 *            "roleInfo":[{"organizeId":"组织id","organizationName":"组织名称","roleId":"角色id","roleName":"角色名称"}],
	 *            "factory":"厂家", "peopleType":"人员类型id", "receiveSms":"是否接收短信",
	 *            "remark":"备注", "password":"密码" }
	 * 		id 人员id
	 * 
	 * 
	 * @return
	 * @Description: 修改人员(关联组织，关联角色)RS
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.USER_UPDATE_CONNECTION, method = RequestMethod.PUT)
	public Result<JSONObject> updateUser(@RequestBody String userInfo,@PathVariable String id) {
		return userCudRestService.updateUser(userInfo,id);
	}
	
	/**
	 * 
	 * @Description:管理组织启/停用状态
	 * @param "organizationId":"组织id", 
	 * manageInfo 更新的组织信息
	 * 	{"state":"invalid/valid停/启用"}
     * 		
			
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.USER_MANAGER, method = RequestMethod.PUT)
	public Result<JSONObject> manageOrganizationState(@PathVariable String id,@RequestBody String manageInfo) {
		return userCudRestService.manageUserState(id,manageInfo);
	}
	
	
	/**
	 * 
	* @Description:修改公司信息
	* @param id locman应用id
	* @param companyInfo 公司信息
	* @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.USER_COMPANY, method = RequestMethod.PUT)
	public Result<String> updateUserCompany(@PathVariable String id,@RequestBody String companyInfo) {
		return userCudRestService.updateUserCompany(id,companyInfo);
	}
	
}
