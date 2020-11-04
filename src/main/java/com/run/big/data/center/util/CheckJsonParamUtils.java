/*
* File name: CheckJsonUtils.java								
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

package com.run.big.data.center.util;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.run.big.data.center.entity.LogConstants;
import com.run.entity.common.RpcResponse;
import com.run.entity.tool.RpcResponseBuilder;
import com.run.usc.api.base.util.ParamChecker;
import com.run.usc.api.constants.UscConstants;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2018年3月20日
*/

public class CheckJsonParamUtils {
	@SuppressWarnings("rawtypes")
	public static RpcResponse checkRequestParamHasKey(String requestJson, String checkKey) {
		try {
			// 判断是否为空
			if (ParamChecker.isBlank(requestJson)) {
				return RpcResponseBuilder.buildErrorRpcResp("传入参数为空！");
			}
			// 参数非法是否是json格式
			if (ParamChecker.isNotMatchJson(requestJson)) {
				return RpcResponseBuilder.buildErrorRpcResp("传入参数json参数非法！");
			}
			JSONObject json = JSON.parseObject(requestJson);
			if (!json.containsKey(checkKey)) {
				return RpcResponseBuilder.buildErrorRpcResp("没有业务数据！");
			}
		} catch (Exception e) {
			return RpcResponseBuilder.buildExceptionRpcResp(e);
		}

		return null;
	}



	@SuppressWarnings("rawtypes")
	public static RpcResponse checkRequestParam(String requestJson) {
		try {
			// 判断是否为空
			if (ParamChecker.isBlank(requestJson)) {
				return RpcResponseBuilder.buildErrorRpcResp("传入参数为空！");
			}
			// 参数非法是否是json格式
			if (ParamChecker.isNotMatchJson(requestJson)) {
				return RpcResponseBuilder.buildErrorRpcResp("传入参数json参数非法！");
			}
		} catch (Exception e) {
			return RpcResponseBuilder.buildExceptionRpcResp(e);
		}
		return null;
	}



	/**
	 * 
	 * @Description:判断必填参数Key是否存在,值是否为null或者""
	 * @param json
	 *            json字符串
	 * @param key
	 *            包含的key
	 * @return
	 */
	public static <T> RpcResponse<T> checkRequestKey(Logger logger, String methodName, JSONObject json,
			String... keys) {
		for (String key : keys) {
			if (isEmpty(json.getString(key))) {
				logger.error(String.format("[%s()->error:%s:%s]", methodName, UscConstants.NO_BUSINESS, key));
				return RpcResponseBuilder.buildErrorRpcResp(UscConstants.NO_BUSINESS);
			}
		}
		return null;
	}



	/**
	 * 
	 * @Description:判断必填参数Key是否存在
	 * @param json
	 *            json字符串
	 * @param key
	 *            包含的key
	 * @return
	 */
	public static <T> RpcResponse<T> checkRequestKey(Logger logger, String methodName, String... keys) {
		for (String key : keys) {
			if (isEmpty(key)) {
				logger.error(String.format("[%s()->error:%s:%s]", methodName, LogConstants.NO_PARAMETER_EXISTS, key));
				return RpcResponseBuilder.buildErrorRpcResp("参数校验:非法参数");
			}
		}
		return null;
	}



	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}
}
