/*
 * File name: DeviceQueryRestService.java
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

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.run.entity.common.Result;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.ResultBuilder;
import com.run.gathering.center.api.query.service.AppTagQueryService;

/**
 * @Description:
 * @author: 王胜
 * @version: 1.0, 2018年01月16日
 */
@Service
public class DeviceAppTagQueryRestService {

	private static final Logger	logger	= Logger.getLogger(DeviceAppTagQueryRestService.class);

	@Autowired
	private AppTagQueryService	appTagQueryService;



	public Result<PageInfo<Map<String, Object>>> queryAppTagsByUserId(String userId, Integer pageNum,
			Integer pageSize) {
		logger.info(String.format("[queryAppTagsByUserId()->request params:%s]", userId));
		try {
			// 参数验证
			if (StringUtils.isBlank(userId)) {
				logger.error("queryAppTagsByUserId()--用户id不能为空!");
				return ResultBuilder.failResult("用户id不能为空!");
			}

			// 根据用户id查询设备信息
			RpcResponse<PageInfo<Map<String, Object>>> appTagList = appTagQueryService.getAppTagList(userId, null,
					pageNum, pageSize);

			if (!appTagList.isSuccess()) {
				logger.error("queryAppTagsByUserId()--" + appTagList.getMessage());
				return ResultBuilder.failResult(appTagList.getMessage());
			}
			return ResultBuilder.successResult(appTagList.getSuccessValue(), appTagList.getMessage());

		} catch (Exception e) {
			logger.error("queryAppTagsByUserId()->exception", e);
			return ResultBuilder.exceptionResult(e);
		}
	}
}
