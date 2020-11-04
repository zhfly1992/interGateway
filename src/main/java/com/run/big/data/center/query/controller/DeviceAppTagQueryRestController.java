/*
 * File name: DeviceAppTagQueryRestController.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 王胜 2018年1月18日 ... ... ...
 *
 ***************************************************/
package com.run.big.data.center.query.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.run.big.data.center.entity.UrlConstant;
import com.run.big.data.center.query.service.DeviceAppTagQueryRestService;
import com.run.entity.common.Result;

/**
 * 
 * @Description:appTag查询Contro
 * @author: 王胜
 * @version: 1.0, 2018年1月18日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
public class DeviceAppTagQueryRestController {

	@Autowired
	private DeviceAppTagQueryRestService deviceAppTagQueryRestService;



	/**
	 * 
	 * @Description:根据用户id查询appTag
	 * @param userId
	 *            用户id
	 * @param pageNum
	 *            第几页
	 * @param pageSize
	 *            分页大小
	 * @return Result<PageInfo<Map<String, Object>>>
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.QUERY_APPTAGS_BY_USERID, method = RequestMethod.GET)
	public Result<PageInfo<Map<String, Object>>> queryAppTagsByUserId(
			@PathVariable String userId,
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		return deviceAppTagQueryRestService.queryAppTagsByUserId(userId, pageNum, pageSize);
	}
}
