/*
 * File name: AuthzQueryRestService.java
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

package com.run.big.data.center.query.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.run.authz.api.constants.AuthzConstants;
import com.run.authz.base.query.FunctionItemBaseQueryService;
import com.run.authz.base.query.PermiBaseQueryService;
import com.run.authz.base.query.UserRoleBaseQueryService;
import com.run.entity.common.Pagination;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;

/**
 * @Description: 权限查询
 * @author: guofeilong
 * @version: 1.0, 2018年3月20日
 */

@Service
@SuppressWarnings({ "rawtypes" })
public class AuthzQueryRestService {

	private static final Logger				logger	= Logger.getLogger(AuthzQueryRestService.class);

	@Autowired
	private UserRoleBaseQueryService		userRoleBaseQueryService;

	@Autowired
	private PermiBaseQueryService			permiBaseQueryService;

	@Autowired
	private FunctionItemBaseQueryService	functionItemBaseQueryService;



	/**
	 * @Description 根据接入方密钥分页查询岗位权限
	 *
	 * @param accessSecret
	 * @param pageNum
	 * @param pageSize
	 * @param organizeId
	 * @param roleName
	 * @return
	 */

	public Result<Pagination<Map<String, Object>>> queryRolePage(String accessSecret, Integer pageNum, Integer pageSize,
			String organizeId, String roleName, String state) {
		logger.info(String.format("[queryRolePage()->request param:接入方密钥：%s,页码：%s,每页大小：%s,组织id：%s,roleName：%s]",
				accessSecret, pageNum, pageSize, organizeId, roleName));
		try {
			if (StringUtils.isBlank(accessSecret)) {
				logger.error("[queryRolePage()->invalid：accessSecret不能为空！]");
				return ResultBuilder.invalidResult();
			}
			/*
			 * if (null == pageNum) { pageNum = 1; } if (null == pageSize) {
			 * pageSize = 10; }
			 */
			JSONObject json = new JSONObject();
			json.put(AuthzConstants.ACCESS_SECRET, accessSecret);
			json.put(AuthzConstants.PAGENUMBER, pageNum);
			json.put(AuthzConstants.PAGESIZE, pageSize);
			json.put(AuthzConstants.ORGANIZED_ID, organizeId);
			json.put(AuthzConstants.ROLE_NAME, roleName);
			json.put("state", state);
			RpcResponse<Pagination<Map<String, Object>>> res = userRoleBaseQueryService.getAccUserRoleInfoByPage(json);
			if (res.isSuccess()) {
				logger.info(String.format("[queryRolePage()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[queryRolePage()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[queryRolePage()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 查询角色已经拥有的(有角色id时)或者所有的权限信息
	 *
	 * @param accessSecret
	 * @param pageNum
	 * @param pageSize
	 * @param permissionName
	 * @param queryInfo
	 * @param applicationType2
	 * @param roleId2
	 * @return
	 */

	public Result<Pagination<Map<String, Object>>> queryRoleAuthz(String accessSecret, Integer pageNum,
			Integer pageSize, String roleId, String applicationType, String permissionName) {
		logger.info(String.format(
				"[queryRoleAuthz()->request params:accessSecret:%s,pageNum:%s,pageSize:%s,roleId:%s,applicationType:%s,permissionName:%s]",
				accessSecret, pageNum, pageSize, roleId, applicationType, permissionName));
		try {
			// 参数基础校验
			if (StringUtils.isBlank(accessSecret)) {
				logger.error("[queryRoleAuthz()->invalid：accessSecret不能为空！]");
				return ResultBuilder.invalidResult();
			}
			if (StringUtils.isBlank(applicationType)) {
				logger.error("[queryRoleAuthz()->invalid：applicationType不能为空！]");
				return ResultBuilder.invalidResult();
			}
			JSONObject json = new JSONObject();
			json.put(AuthzConstants.ACCESS_SECRET, accessSecret);
			json.put(AuthzConstants.ROLE_ID, roleId);
			json.put(AuthzConstants.APPLICATION_TYPE, applicationType);
			json.put(AuthzConstants.PAGENUMBER, pageNum);
			json.put(AuthzConstants.PAGESIZE, pageSize);
			json.put(AuthzConstants.PERMI_NAME, permissionName);
			RpcResponse<Pagination<Map<String, Object>>> res = permiBaseQueryService.getRolePermiByPage(json);
			if (res.isSuccess()) {
				logger.info(String.format("[queryRoleAuthz()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[queryRoleAuthz()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[queryRoleAuthz()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 根据角色id和菜单id查询共有的功能类型
	 *
	 * @param roleId
	 * @param menuId
	 * @return
	 */

	public Result<List<String>> getItemListByRoleIdAndMenuId(String roleId, String menuId) {
		logger.info(
				String.format("[getItemListByRoleIdAndMenuId()->request params:roleId:%s,menuId:%s]", roleId, menuId));
		try {
			if (StringUtils.isBlank(roleId)) {
				logger.error("[getItemListByRoleIdAndMenuId()->invalid：roleId不能为空！]");
				return ResultBuilder.invalidResult();
			}
			if (StringUtils.isBlank(menuId)) {
				logger.error("[getItemListByRoleIdAndMenuId()->invalid：menuId不能为空！]");
				return ResultBuilder.invalidResult();
			}
			// 校验角色是否正常（删除或者停用）
			Boolean check = permiBaseQueryService.checkRoleNormal(roleId);
			if (!check) {
				logger.error(String.format("[getItemListByRoleIdAndMenuId()->fail:%s]", AuthzConstants.GET_FAIL));
				return ResultBuilder.failResult(AuthzConstants.GET_ROLE_STATE_FAIL);
			}

			RpcResponse<List<String>> res = permiBaseQueryService.getPermiIdIdByRoleId(roleId);

			List<String> listItemId = new ArrayList<>();
			if (res.isSuccess()) {
				RpcResponse<List<String>> listValue = functionItemBaseQueryService
						.getItemIdByPermiIds((ArrayList<String>) res.getSuccessValue());
				if (listValue.isSuccess()) {
					listItemId = listValue.getSuccessValue();
				}
			}

			RpcResponse<List<Map>> itemInfo = functionItemBaseQueryService.getItemBymenuId(menuId);

			List<String> itemTypeList = new ArrayList<>();
			if (res.isSuccess() && listItemId.size() > 0 && itemInfo.isSuccess()
					&& itemInfo.getSuccessValue().size() > 0) {
				List<Map> list = itemInfo.getSuccessValue();
				for (int i = 0; i < listItemId.size(); i++) {
					for (int j = 0; j < list.size(); j++) {
						if (listItemId.get(i).equals(list.get(j).get(AuthzConstants.ID_))
								&& list.get(j).containsKey("itemType")) {
							itemTypeList.add(list.get(j).get("itemType").toString());
						}
					}
				}
			}
			// 去除空值，去除重复字符串
			itemTypeList.remove("");
			List<String> newList = new ArrayList<String>(new HashSet<String>(itemTypeList));

			if (res.isSuccess() && itemInfo.isSuccess()) {
				logger.info(String.format("[getItemListByRoleIdAndMenuId()->success:%s]", AuthzConstants.GET_SUCC));
				return ResultBuilder.successResult(newList, AuthzConstants.GET_SUCC);
			} else {
				logger.error(String.format("[getItemListByRoleIdAndMenuId()->fail:%s]", AuthzConstants.GET_FAIL));
				return ResultBuilder.failResult(AuthzConstants.GET_FAIL);
			}
		} catch (Exception e) {
			logger.error("[getItemListByRoleIdAndMenuId()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description
	 *
	 * @param accessSecret
	 * @param userId
	 * @return
	 */

	public Result<List<Map>> queryRoleInfoByUserIdAndAS(String accessSecret, String userId) {
		logger.info(String.format("[queryRoleInfoByUserIdAndAS()->request param:%s]",
				"accessSecret:" + accessSecret + "userId:" + userId));
		try {
			if (StringUtils.isBlank(accessSecret)) {
				logger.error("[queryRoleInfoByUserIdAndAS()->invalid：accessSecret不能为空！]");
				return ResultBuilder.invalidResult();
			}
			if (StringUtils.isBlank(userId)) {
				logger.error("[queryRoleInfoByUserIdAndAS()->invalid：userId不能为空！]");
				return ResultBuilder.invalidResult();
			}

			// 根据人员id以及接入方密钥 查询roleUserAs表
			RpcResponse<List<Map>> res = userRoleBaseQueryService.getRoleMessBySecret(userId, accessSecret);

			if (res.isSuccess()) {
				logger.info(String.format("[queryRoleInfoByUserIdAndAS()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[queryRoleInfoByUserIdAndAS()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[queryRoleInfoByUserIdAndAS()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 根据角色id查询角色所绑定的菜单信息
	 *
	 * @param roleId
	 * @param applicationType
	 * @return
	 */

	public Result<List<Map>> getMenuInfoByRoleIdAndAT(String roleId, String applicationType) {
		logger.info(String.format("[getMenuInfoByRoleIdAndAT()->request params:roleId:%s,applicationType:%s]", roleId,
				applicationType));
		try {
			if (StringUtils.isBlank(roleId)) {
				logger.error("[queryRoleInfoByUserIdAndAS()->invalid：roleId不能为空！]");
				return ResultBuilder.invalidResult();
			}
			if (StringUtils.isBlank(applicationType)) {
				logger.error("[queryRoleInfoByUserIdAndAS()->invalid：applicationType不能为空！]");
				return ResultBuilder.invalidResult();
			}
			RpcResponse<List<Map>> res = userRoleBaseQueryService.getRoleMenuById(roleId, applicationType);
			if (res.isSuccess()) {
				logger.info(String.format("[getRoleMenuById()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[getRoleMenuById()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}
		} catch (Exception e) {
			logger.error("[getRoleMenuById()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 根据组织id查询组织组织下的岗位信息
	 *
	 * @param organizeId
	 * @return
	 */

	public Result<List<Map>> getRoleListByorganizeId(String organizeId) {
		logger.info(String.format("[getRoleListByorganizeId()->request params:organizeId:%s]", organizeId));
		if (StringUtils.isBlank(organizeId)) {
			logger.error("[getRoleListByorganizeId()->invalid：organizeId不能为空！]");
			return ResultBuilder.invalidResult();
		}
		try {
			RpcResponse<List<Map>> res = userRoleBaseQueryService.getRoleListByOrgId(organizeId);
			if (res.isSuccess()) {
				logger.info(String.format("[getRoleListByorganizeId()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[getRoleListByorganizeId()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error("[getRoleListByOrgId()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * @Description 校验岗位名称是否重复
	 *
	 * @param organizeId
	 * @param roleName
	 * @return
	 */

	public Result<Boolean> checkOrgRoleName(String organizeId, String roleName) {
		logger.info(
				String.format("[checkOrgRoleName()->request params:organizeId:%s,roleName:%s]", organizeId, roleName));
		try {
			if (StringUtils.isBlank(organizeId)) {
				logger.error("[checkOrgRoleName()->invalid：organizeId不能为空！]");
				return ResultBuilder.invalidResult();
			}
			if (StringUtils.isBlank(roleName)) {
				logger.error("[checkOrgRoleName()->invalid：roleName不能为空！]");
				return ResultBuilder.invalidResult();
			}
			RpcResponse<Boolean> res = userRoleBaseQueryService.checkOrgRoleName(organizeId, roleName);
			if (res.isSuccess()) {
				logger.info(String.format("[checkOrgRoleName()->success:%s]", res.getMessage()));
				return ResultBuilder.successResult(res.getSuccessValue(), res.getMessage());
			} else {
				logger.error(String.format("[checkOrgRoleName()->fail:%s]", res.getMessage()));
				return ResultBuilder.failResult(res.getMessage());
			}

		} catch (Exception e) {
			logger.error("[checkOrgRoleName()->exception]", e);
			return ResultBuilder.exceptionResult(e);
		}
	}

}
