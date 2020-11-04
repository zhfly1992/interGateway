/*
* File name: AuthzCudRestController.java								
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
* 1.0			guofeilong		2018年3月21日
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
import com.run.big.data.center.cud.service.AuthzCudRestService;
import com.run.big.data.center.entity.UrlConstant;
import com.run.entity.common.Result;

/**
 * @Description: 权限中心,岗位管理cud相关
 * @author: guofeilong
 * @version: 1.0, 2018年3月21日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
@CrossOrigin(origins = "*")
public class AuthzCudRestController {

	@Autowired
	private AuthzCudRestService authzCudRestService;

	/**
	 * @param userRoleRSInfo
	 *            新增角色的信息 { "accessSecret":"密钥",
	 *            "roleName":"角色名",
	 *            "orgMess":["组织id"],
	 *            "permiArray":["PC职能","","","",""],
	 *            "remark":"备注", "app":"app职能" }
	 * 
	 * 
	 * 
	 * @return
	 * @Description: 保存用户角色以及他的关联信息（组织以及菜单权限）
	 */
	@RequestMapping(value = UrlConstant.SAVE_ROLE_INFO, method = RequestMethod.POST)
	public Result<JSONObject> addUser(@RequestBody String userRoleRSInfo) {
		return authzCudRestService.addUserRoleRS(userRoleRSInfo);
	}
	
	/**
	 * @param userRoleRSInfo
	 * 				roleId:"角色id"
	 *           	 修改角色的信息 { "accessSecret":"密钥",
	 *           	 "roleName":"角色名",
	 *            	"orgMess":["组织id"],
	 *            	"permiArray":["PC职能","","","",""],
	 *            	"remark":"备注", "app":"app职能" }
	 * 
	 * 
	 * 
	 * @return
	 * @Description: 修改用户角色以及他的关联信息（组织以及菜单权限）
	 */
	@RequestMapping(value = UrlConstant.UPDATE_ROLE_INFO, method = RequestMethod.PUT)
	public Result<JSONObject> updateUser(@RequestBody String userRoleRSInfo,@PathVariable("roleId") String roleId) {
		return authzCudRestService.updateUserRoleRS(userRoleRSInfo,roleId);
	}
	
	/**
	 * 
	 * @Description:转换用户角色状态
	 * @param "roleId":"角色id", 
	 * manageInfo 更新的状态信息
	 * 	{"state":"invalid/valid停/启用"}
     * 		
			
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MANAGER_ROLE_STATE, method = RequestMethod.PUT)
	public Result<String> managerRoleState(@PathVariable("roleId") String roleId,@RequestBody String manageInfo) {
		return authzCudRestService.managerRoleState(roleId,manageInfo);
	}
}
