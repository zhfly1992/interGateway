/*
* File name: OrganizationCudRestController.java								
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
import com.run.big.data.center.cud.service.OrganizationCudRestService;
import com.run.big.data.center.entity.UrlConstant;
import com.run.entity.common.Result;

/**
* @Description:	组织管理cud类Controller
* @author: guofeilong
* @version: 1.0, 2018年3月20日
*/
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
public class OrganizationCudRestController {
	
	@Autowired
	private OrganizationCudRestService organizationCudRestService;

	
	/**
     * @param organizationInfo 新增组织的信息
     * 		{"accessSecret":"密钥",
			"organizationName":"组织名",
			"organizationDecs":"备注"}


     * @return
     * @Description: 新增组织
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = UrlConstant.ORGANIZATION_ADD_BY_AS, method = RequestMethod.POST)
    public Result<JSONObject> addOrganizationByAS(@RequestBody String organizationInfo) {
        return organizationCudRestService.addOrganization(organizationInfo);
    }
	/**
	 * 
	 * @Description:更新组织信息
	 * @param organizationInfo 更新的组织信息
     * 		{"accessSecret":"密钥",
			"organizationName":"组织名",
			"organizationDecs":"备注"}
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.ORGANIZATION_UPDATE_BY_AS, method = RequestMethod.PUT)
	public Result<JSONObject> updateOrganizationByAS(@PathVariable("organizationid") String organizationid,
			@PathVariable("accessSecret") String accessSecret,@RequestBody String organizationInfo) {

		return organizationCudRestService.updateOrganizationByAS(accessSecret,organizationid,organizationInfo);
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
	@RequestMapping(value = UrlConstant.ORGANIZATION_MANAGER_STATE, method = RequestMethod.PUT)
	public Result<String> manageOrganizationState(@PathVariable("organizationId") String organizationId,@RequestBody String manageInfo) {
		return organizationCudRestService.manageOrganizationState(organizationId,manageInfo);
	}
}
