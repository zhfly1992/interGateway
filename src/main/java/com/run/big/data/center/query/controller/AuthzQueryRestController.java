/*
 * File name: AuthzQueryRestController.java
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

package com.run.big.data.center.query.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.run.big.data.center.entity.UrlConstant;
import com.run.big.data.center.query.service.AuthzQueryRestService;
import com.run.entity.common.Pagination;
import com.run.entity.common.Result;

/**
 * @Description: 权限查询相关
 * @author: guofeilong
 * @version: 1.0, 2018年3月20日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
@CrossOrigin(origins = "*")
public class AuthzQueryRestController {

	@Autowired
	private AuthzQueryRestService authzQueryRestService;



	/**
	 * @Description: 根据接入方密钥分页查询岗位权限
	 * @param "accessSecret":"密钥",
	 *            "pageNo":页码, "pageSize":分页大小, "organizeId":"组织id",
	 *            "roleName":"角色/岗位名称"
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.QUERY_AS_ROLE, method = RequestMethod.GET)
	public Result<Pagination<Map<String, Object>>> queryRolePage(@PathVariable("accessSecret") String accessSecret,
			@RequestParam(value = "pageNo", required = true) Integer pageNum,
			@RequestParam(value = "pageSize", required = true) Integer pageSize,
			@RequestParam(value = "organizeId", required = false) String organizeId,
			@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "state", required = false) String state) {
		return authzQueryRestService.queryRolePage(accessSecret, pageNum, pageSize, organizeId, roleName, state);
	}



	/**
	 * @Description: 查询角色已经拥有的或者未拥有的权限信息
	 * @param "accessSecret":"密钥",
	 *            "roleId":"角色/岗位id", "applicationType":"PC 应用类型PC/app"
	 *            "permissionName":"权限名称"
	 * 
	 * 
	 * 
	 * @param pageNum
	 *            页码
	 * @param pageSize
	 *            分页大小
	 * @return
	 */
	@RequestMapping(value = UrlConstant.QUERY_PERMISSIONS, method = RequestMethod.GET)
	public Result<Pagination<Map<String, Object>>> queryRoleAuthz(@PathVariable("accessSecret") String accessSecret,
			@PathVariable("applicationType") String applicationType,
			@RequestParam(value = "pageNo", required = true) Integer pageNum,
			@RequestParam(value = "pageSize", required = true) Integer pageSize,
			@RequestParam(value = "roleId", required = false) String roleId,
			@RequestParam(value = "permissionName", required = false) String permissionName) {
		return authzQueryRestService.queryRoleAuthz(accessSecret, pageNum, pageSize, roleId, applicationType,
				permissionName);
	}



	/**
	 * @Description: 根据角色id和菜单id查询共有的功能类型
	 * @param "roleId":"角色id",
	 *            "menuId":"菜单id"
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.QUERY_BY_RID_MID, method = RequestMethod.GET)
	public Result<List<String>> getItemListByRoleIdAndMenuId(@PathVariable String roleId, @PathVariable String menuId) {
		return authzQueryRestService.getItemListByRoleIdAndMenuId(roleId, menuId);
	}



	/**
	 * @Description: 校验岗位名称是否重复
	 * @param "organizeId":"组织id"
	 *            "roleName":"岗位名"
	 * @return
	 */
	@RequestMapping(value = UrlConstant.CHECK_ORG_ROLE_NAME, method = RequestMethod.GET)
	public Result<Boolean> checkOrgRoleName(@PathVariable String organizeId,
			@RequestParam(value = "roleName", required = true) String roleName) {
		return authzQueryRestService.checkOrgRoleName(organizeId, roleName);
	}



	/**
	 * @Description: 根据用户id和接入方秘钥查询角色信息
	 * @param "accessSecret":"密钥",
	 *            "userId":"用户id"
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.FIND_USERINFO_BY_UID_AS, method = RequestMethod.GET)
	public Result<List<Map>> queryRoleInfoByUserIdAndAS(@PathVariable String accessSecret,
			@PathVariable String userId) {
		return authzQueryRestService.queryRoleInfoByUserIdAndAS(accessSecret, userId);
	}



	/**
	 * @Description: 根据角色id查询角色所绑定的菜单信息
	 * @param "
	 *            roleId ":"角色id", " applicationType ":"应用类型"
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.GET_MENUINFO_BY_RID, method = RequestMethod.GET)
	public Result<List<Map>> getMenuInfoByRoleIdAndAT(@PathVariable String roleId,
			@PathVariable String applicationType) {
		return authzQueryRestService.getMenuInfoByRoleIdAndAT(roleId, applicationType);
	}



	/**
	 * @Description: 根据组织id查询组织下的岗位信息
	 * @param "organizeId":"组织id"
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.GET_PERMISSIONS_BY_OID, method = RequestMethod.GET)
	public Result<List<Map>> getRoleListByorganizeId(@PathVariable("organizeId") String organizeId) {
		return authzQueryRestService.getRoleListByorganizeId(organizeId);
	}
}
