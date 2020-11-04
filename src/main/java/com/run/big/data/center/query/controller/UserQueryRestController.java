/*
 * File name: UserQueryRestController.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 guofeilong 2018年3月16日 ...
 * ... ...
 *
 ***************************************************/

package com.run.big.data.center.query.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.run.big.data.center.entity.UrlConstant;
import com.run.big.data.center.query.service.UserQueryRestService;
import com.run.entity.common.Pagination;
import com.run.entity.common.Result;

/**
 * @Description: 接入方(用户)信息查询
 * @author: guofeilong
 * @version: 1.0, 2018年3月16日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
@CrossOrigin(origins = "*")
public class UserQueryRestController {
	@Autowired
	private UserQueryRestService userQueryRestService;



	/**
	 * @Description: 1.查询组织架构树
	 * @param "
	 *            organizationName":"组织名称关键字"非必填 "accessSecret":"密钥"(固定必须传)
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.ORGANIZATION_TYPE, method = RequestMethod.GET)
	public Result<List<Map>> queryOrganizationInfoByAs(@PathVariable("accessSecret") String accessSecret,
			@RequestParam(value = "organizationName", required = false) String organizationName) {
		return userQueryRestService.getAccSourceInfoByType(accessSecret, organizationName);
	}



	/**
	 * @Description: 分页查询组织人员
	 * @param "accessSecret":"密钥",(必填)
	 *            "organizationId":"组织id(非必填)",
	 *            "organizationName":"组织名称关键字(非必填)"
	 * 
	 * 
	 * @param pageNum
	 *            页码
	 * @param pageSize
	 *            分页大小
	 * @return
	 */
	@RequestMapping(value = UrlConstant.USER_TYPE_PAGE, method = RequestMethod.GET)
	public Result<Pagination<Map<String, Object>>> queryOrganizationInfoListByUserId(
			@PathVariable("accessSecret") String accessSecret,
			@RequestParam(value = "organizationId", required = false) String organizationId,
			@RequestParam(value = "organizationName", required = false) String organizationName,
			@RequestParam(value = "pageNo", required = true) Integer pageNum,
			@RequestParam(value = "pageSize", required = true) Integer pageSize,
			@RequestParam(value = "state", required = false) String state) {
		return userQueryRestService.getOrganizationPersonPageByType(accessSecret, organizationId, organizationName,
				pageNum, pageSize, state);
	}



	/**
	 * @Description: 根据组织id查询组织信息
	 * @param organizationId
	 *            组织id
	 * @return 组织信息
	 */
	@RequestMapping(value = UrlConstant.ORGANIZATION_ID, method = RequestMethod.GET)
	public Result<Map<String, Object>> queryOrganizationById(@PathVariable String organizationId) {
		return userQueryRestService.queryOrganizationById(organizationId);
	}



	/**
	 * @Description: 通过token查询用户信息
	 * @param "token":token
	 * 
	 * @return 用户信息
	 */
	@RequestMapping(value = UrlConstant.USER_TOKEN, method = RequestMethod.GET)
	public Result<Object> queryUserByToken(@PathVariable String token) {
		return userQueryRestService.queryUserByToken(token);
	}



	/**
	 * @Description: 查询接入方下面的所有人员信息
	 * @param "accessSecret":"密钥",(必传)
	 *            "pageNo":页码, (必传) "pageSize":分页大小(必传) "organizationId":"组织id",
	 *            "nameKey":"查询关键字", "roleName":"角色名"
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.USER_QUERY_BY_AS, method = RequestMethod.GET)
	public Result<Object> queryOrganizationInfoListByUserId(@PathVariable("accessSecret") String accessSecret,
			@RequestParam(value = "organizationId", required = false) String organizationId,
			@RequestParam(value = "nameKey", required = false) String nameKey,
			@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "pageNo", required = true) Integer pageNum,
			@RequestParam(value = "pageSize", required = true) Integer pageSize,
			@RequestParam(value = "peopleType") String peopleType,
			@RequestParam(value = "receiveSms") String receiveSms,
			@RequestParam(value = "state", required = false) String state) {
		return userQueryRestService.getAllOrganizationPersonPage(accessSecret, pageNum, pageSize, organizationId,
				nameKey, roleName, peopleType, receiveSms, state);
	}



	/**
	 * @Description: 根据用户id查询用户所属接入方信息
	 * @param userId
	 *            用户id
	 * @return 接入方信息
	 */

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.GET_ASINFO_BY_USERID, method = RequestMethod.GET)
	public Result<List<Map>> queryAccessInfoByUserId(@PathVariable String userId) {
		return userQueryRestService.queryAccessInfoByUserId(userId);
	}



	/**
	 * @Description: 检测组织下面是否存在子类
	 * @param userId
	 *            用户id
	 * @return 接入方信息
	 */
	@RequestMapping(value = UrlConstant.CHECK_CHILD_BY_ORGANID, method = RequestMethod.GET)
	public Result<Boolean> checkChildByOrganId(@PathVariable String organizationId) {
		return userQueryRestService.checkChildByOrganId(organizationId);
	}



	/**
	 * @Description: 检测组织是否重名
	 * @param "accessSecret":"密钥",
	 *            "organizationName":"新增下级组织名称", "parentId":"父组织id"
	 * 
	 * @return true/false
	 */
	@RequestMapping(value = UrlConstant.CHECK_ORGANIZATION_NAME, method = RequestMethod.POST)
	public Result<Boolean> checkOrgName(@RequestBody String orgInfo) {
		return userQueryRestService.checkOrgName(orgInfo);
	}



	/**
	 * 
	 * @Description:根据用户名密码查询接入方信息
	 * @param userInfo
	 *            用户名loginAccount 密码password
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.GET_ASINFO_BY_USERINFO, method = RequestMethod.POST)
	public Result<List<Map>> queryAccessInfoByUserInfo(@RequestBody String userInfo) {
		return userQueryRestService.queryAccessInfoByUserInfo(userInfo);
	}



	/**
	 * @Description: 根据组织id查询组织及其子组织信息
	 * @param "organizationId":"组织id",
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.GET_OWN_AND_CHILD_BY_ORGANID, method = RequestMethod.GET)
	public Result<List<Map>> ownAndSonOrganizationInfo(@PathVariable String organizationId) {
		return userQueryRestService.ownAndSonOrganizationInfo(organizationId);
	}



	/**
	 * @Description: 根据组织id查询其父组织信息
	 * @param "organizationId":"组织id",
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.GET_PARENT_BY_ORGANID, method = RequestMethod.GET)
	public Result<String> getParentByOrganizationId(@PathVariable String organizationId) {
		return userQueryRestService.getParentByOrganizationId(organizationId);
	}



	/**
	 * @Description: 根据组织id批量查询组织名
	 * @param "organizationIds":"组织id",
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.GET_ORGANIZATIONS_BY_ORGANID, method = RequestMethod.POST)
	public Result<Map<String, String>> getOrganizationNamesByIds(@RequestBody String organizationIds) {
		return userQueryRestService.getOrganizationNamesByIds(organizationIds);
	}



	/**
	 * @Description: 根据接入方密钥查询接入方信息
	 * @param "accessSecret":"密钥",
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = UrlConstant.GET_ASINFO_BY_AS, method = RequestMethod.GET)
	public Result<Map> getAccessInfoByAS(@PathVariable String accessSecret) {
		return userQueryRestService.getAccessInfoByAS(accessSecret);
	}



	/**
	 * @Description: 根据接入方密钥查询当前接入方下用户数量
	 * @param "accessSecret":"密钥",
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.GET_USERIDS_BY_AS, method = RequestMethod.GET)
	public Result<String> getUserNumByAS(@PathVariable String accessSecret) {
		return userQueryRestService.getUserNumByAS(accessSecret);
	}



	/**
	 * @Description: 根据接入方密钥查询最近一个月的用户使用率
	 * @param "accessSecret":"密钥",
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.GET_USERUSAGE_BY_AS, method = RequestMethod.GET)
	public Result<String> getUserUsageByAS(@PathVariable String accessSecret) {
		return userQueryRestService.getUserUsageByAS(accessSecret);
	}



	/**
	 * @Description: 根据人员查询厂家Id
	 * @param "organizationIds":"组织id",
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.GET_FACTORY_ID, method = RequestMethod.POST)
	public Result<Map<String, Object>> getFactoryIds(@RequestBody String factoryIds) {
		return userQueryRestService.getFactoryIdsByPersonId(factoryIds);
	}



	/**
	 * 
	 * @Description:根据接入方id查询用户公司信息
	 * @param id
	 *            locman应用id
	 * @param companyInfo
	 *            公司信息
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.USER_COMPANY, method = RequestMethod.GET)
	public Result<Map<String, Object>> getUserCompany(@PathVariable String id) {
		return userQueryRestService.getUserCompany(id);
	}



	/**
	 * @Description: 根据人员id查询人员信息
	 * @param "userId":"人员id",
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.GET_USER_INFO, method = RequestMethod.GET)
	public Result<Map<String, Object>> getUserByUserId(@PathVariable("userId") String userId) {
		return userQueryRestService.getUserByUserId(userId);
	}



	/**
	 * @Description: 根据人员id批量查询人员信息
	 * @param "userIds":"组织id",
	 * 
	 * @return
	 */
	@RequestMapping(value = UrlConstant.GET_PERSON_INFO, method = RequestMethod.POST)
	public Result<List<Map<String, Object>>> getUserInfoByIds(@RequestBody String userIds) {
		return userQueryRestService.getUserInfoByIds(userIds);
	}



	/**
	 * 
	 * @Description:根据接入方密钥以及关键字模糊查询用户 返回用户id
	 * @param userKeyWord
	 * @return
	 */
	@PostMapping(value = UrlConstant.FIND_USER_KEYWORD)
	public Result<List<String>> findUserIdsByKey(@RequestBody JSONObject userKeyWord) {
		return userQueryRestService.findUserIdsByKey(userKeyWord);
	}



	/**
	 * 
	 * @Description:查询用户的名称->ids
	 * @param userIdsJson
	 * @return
	 */
	@PostMapping(value = UrlConstant.FIND_USER_NAME)
	public Result<Map<String, String>> findUserLoginAccout(@RequestBody JSONObject userIdsJson) {
		return userQueryRestService.findUserLoginAccout(userIdsJson);
	}



	/**
	 * 
	 * @Description:通过组织id获取所有的子类组织
	 * @param orgJson
	 * @return
	 */
	@PostMapping(value = UrlConstant.FIND_ORG_ID)
	public Result<List<String>> getModelByParentId(@RequestBody JSONObject orgJson) {
		return userQueryRestService.getModelByParentId(orgJson);
	}



	/**
	 * 
	 * @Description:根据资源id集合查询资源信息
	 * @param orgJsonIds
	 * @return
	 */
	@PostMapping(value = UrlConstant.FIND_ORG_NAME)
	public Result<Map<String, String>> findOrgName(@RequestBody JSONObject orgJsonIds) {
		return userQueryRestService.findOrgName(orgJsonIds);
	}
}
