/*
* File name: ElasticSearchController.java								
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
* 1.0			 		2018年01月16日
* ...			...			...
*
***************************************************/
package com.run.big.data.center.es.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery.ZeroTermsQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.run.big.data.center.util.ESFactory;
import com.run.common.util.ParamChecker;
import com.run.entity.common.Result;
import com.run.entity.tool.ResultBuilder;

@RestController
@RequestMapping("/es")
public class ElasticSearchController {
	private static final Logger logger = Logger.getLogger(ElasticSearchController.class);
	private Client					client	= ESFactory.getTransportClient();



	/**
	 * 
	 * 根据设备编号查询设备详情
	 *
	 * @param deviceId
	 *            设备编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deviceDetail", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public Result<Map<String, Object>> queryDeviceDetail(@RequestBody(required = true) String param) throws Exception {
		try {
			if (StringUtils.isBlank(param)) {
				return ResultBuilder.emptyResult();
			}
			String deviceId = null;
			if (StringUtils.isNotBlank(param)) {
				// 参数非法是否是json格式
				if (ParamChecker.isNotMatchJson(param)) {
					return ResultBuilder.invalidResult();
				}
				JSONObject paramsObject = JSON.parseObject(param);
				if (paramsObject.containsKey("deviceId")) {
					deviceId = paramsObject.getString("deviceId");
				}

			}
			if (StringUtils.isBlank(deviceId)) {
				return ResultBuilder.noBusinessResult();
			}
			MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("deviceId", deviceId).operator(Operator.AND)
					.zeroTermsQuery(ZeroTermsQuery.ALL);
			SearchResponse getResponse = this.client.prepareSearch().setIndices("device").setTypes("devicelist")
					.setQuery(queryBuilder).get();
			SearchHits hits = getResponse.getHits();
			if (hits == null || hits.totalHits <= 0) {
				return ResultBuilder.failResult("该设备类型不存在！");
			}
			Map<String, Object> sourceAsMap = hits.getAt(0).getSourceAsMap();
			if (sourceAsMap == null || sourceAsMap.isEmpty()) {
				return ResultBuilder.failResult("该设备类型不存在！");
			}
			
			return ResultBuilder.successResult(sourceAsMap, "查询成功");
		} catch (Exception e) {
			logger.error(String.format("[getUserListFromData()->exception:%s]", e));
			return ResultBuilder.exceptionResult(e);
		}
	}



//	/**
//	 *
//	 * 根据用户编号聚合设备列表
//	 *
//	 * @return
//	 */
//	@SuppressWarnings("rawtypes")
//	@RequestMapping("/userListFromData")
//	@CrossOrigin(origins = "*")
//	public Result<List<Map>> getUserListFromData() {
//		try {
//			SearchRequestBuilder requestBuilder = this.client.prepareSearch().setIndices("device")
//					.setTypes("devicelist");
//			TermsAggregationBuilder aggBuilder = AggregationBuilders.terms("userIdAgg").field("userId").size(100);
//			requestBuilder.addAggregation(aggBuilder);
//			SearchResponse searchResponse = requestBuilder.execute().actionGet();
//			Aggregations aggregations = searchResponse.getAggregations();
//
//			List<String> userIds = Lists.newArrayList();
//			if (aggregations != null) {
//				Map<String, Aggregation> asMap = aggregations.asMap();
//				StringTerms userIdTerms = (StringTerms) asMap.get("userIdAgg");
//				List<Bucket> buckets = userIdTerms.getBuckets();
//				for (Bucket bucket : buckets) {
//					userIds.add(bucket.getKeyAsString());
//				}
//			}
//			// 组装用户信息
//			if (!userIds.isEmpty()) {
//				RpcResponse<List<Map>> listUserByUserIds = accUserQuery.getListUserByUserIds(userIds);
//				if (listUserByUserIds.isSuccess()) {
//					return ResultBuilder.successResult(listUserByUserIds.getSuccessValue(),
//							"数据中心存在" + listUserByUserIds.getSuccessValue().size() + "个拥有设备的用户！");
//				}
//			}
//			return ResultBuilder.failResult("数据中心不存在拥有设备的用户！");
//		} catch (Exception e) {
//			logger.error(String.format("[getUserListFromData()->exception:%s]", e));
//			return ResultBuilder.exceptionResult(e);
//		}
//	}



	/**
	 * 
	 * 查询设备类型列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/deviceTypeListFromData", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public Result<List<Map<String, String>>> getDeviceTypeList(@RequestBody(required = false) String param) {
		try {
			String userId = null;
			String deviceTypeId = null;
			String deviceTypeName = null;
			int pageNo = 0;
			int pageSize = 100;
			if (StringUtils.isNotBlank(param)) {
				// 参数非法是否是json格式
				if (ParamChecker.isNotMatchJson(param)) {
					return ResultBuilder.invalidResult();
				}
				JSONObject paramsObject = JSON.parseObject(param);
				if (paramsObject.containsKey("userId")) {
					userId = paramsObject.getString("userId");
				}
				if (paramsObject.containsKey("deviceTypeId")) {
					deviceTypeId = paramsObject.getString("deviceTypeId");
				}
				if (paramsObject.containsKey("deviceTypeName")) {
					deviceTypeName = paramsObject.getString("deviceTypeName");
				}
				if (paramsObject.containsKey("pageNo")) {
					if (!StringUtils.isBlank(paramsObject.getString("pageNo"))
							&& StringUtils.isNumeric(paramsObject.getString("pageNo"))) {
						pageNo = paramsObject.getIntValue("pageNo");

					}
				}
				if (paramsObject.containsKey("pageSize")) {
					if (!StringUtils.isBlank(paramsObject.getString("pageSize"))
							&& StringUtils.isNumeric(paramsObject.getString("pageSize"))) {
						pageSize = paramsObject.getIntValue("pageSize");
					}
				}
			}

			SearchRequestBuilder requestBuilder = this.client.prepareSearch().setIndices("devicetype")
					.setTypes("devicetypelist");
			if (StringUtils.isNotBlank(userId) || StringUtils.isNotBlank(deviceTypeId)
					|| StringUtils.isNotBlank(deviceTypeName)) {
				requestBuilder = this.client.prepareSearch().setIndices("device").setTypes("devicelist");
				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
				if (StringUtils.isNotBlank(userId)) {
					queryBuilder.must(QueryBuilders.matchQuery("userId", userId).operator(Operator.AND)
							.zeroTermsQuery(ZeroTermsQuery.ALL));
				}
				if (StringUtils.isNotBlank(deviceTypeId)) {
					queryBuilder.must(QueryBuilders.matchQuery("deviceTypeId", deviceTypeId).operator(Operator.AND)
							.zeroTermsQuery(ZeroTermsQuery.ALL));
				}
				if (StringUtils.isNotBlank(deviceTypeName)) {
					queryBuilder.must(QueryBuilders.matchQuery("deviceTypeName", deviceTypeName).operator(Operator.AND)
							.zeroTermsQuery(ZeroTermsQuery.ALL));
				}
				requestBuilder.setQuery(queryBuilder);
			}
			// 设置分页查询
			if (pageNo == 0) {
				pageNo = 1;
			}
			requestBuilder.setFrom((pageNo - 1) * pageSize);
			requestBuilder.setSize(pageSize);
			SearchHits hits = requestBuilder.get().getHits();
			Set<Map<String, String>> docList = Sets.newHashSet();
			if (hits != null && hits.totalHits > 0) {
				for (SearchHit searchHit : hits) {
					Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
					Map<String, String> map = Maps.newHashMap();
					for (Map.Entry<String, Object> entry : sourceAsMap.entrySet()) {
						if (entry.getValue() != null) {
							if (StringUtils.equals("deviceTypeId", entry.getKey())
									|| StringUtils.equals("deviceTypeName", entry.getKey())) {
								map.put(entry.getKey(), entry.getValue().toString());
							}
						}
					}
					docList.add(map);
				}
			}
			if (docList.isEmpty()) {
				return ResultBuilder.failResult("数据中心不存在设备类型数据！");
			}
			return ResultBuilder.successResult(Lists.newArrayList(docList), "设备类型查询成功");
		} catch (Exception e) {
			logger.error(String.format("[getDeviceTypeList()->exception:%s]", e));
			return ResultBuilder.exceptionResult(e);
		}
	}



	/**
	 * 
	 * 查询设备类型列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/deviceListFromData", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public Result<Map<String, Object>> getDeviceList(@RequestBody(required = false) String param) {
		try {
			String userId = null;
			String deviceId = null;
			String deviceName = null;
			String deviceTypeId = null;
			String deviceTypeName = null;
			int pageNo = 0;
			int pageSize = 100;
			if (StringUtils.isNotBlank(param)) {
				// 参数非法是否是json格式
				if (ParamChecker.isNotMatchJson(param)) {
					return ResultBuilder.invalidResult();
				}
				JSONObject paramsObject = JSON.parseObject(param);
				if (paramsObject.containsKey("userId")) {
					userId = paramsObject.getString("userId");
				}
				if (paramsObject.containsKey("deviceId")) {
					deviceId = paramsObject.getString("deviceId");
				}
				if (paramsObject.containsKey("deviceName")) {
					deviceName = paramsObject.getString("deviceName");
				}
				if (paramsObject.containsKey("deviceTypeId")) {
					deviceTypeId = paramsObject.getString("deviceTypeId");
				}
				if (paramsObject.containsKey("deviceTypeName")) {
					deviceTypeName = paramsObject.getString("deviceTypeName");
				}
				if (paramsObject.containsKey("pageNo")) {
					if (!StringUtils.isBlank(paramsObject.getString("pageNo"))
							&& StringUtils.isNumeric(paramsObject.getString("pageNo"))) {
						pageNo = paramsObject.getIntValue("pageNo");

					}
				}
				if (paramsObject.containsKey("pageSize")) {
					if (!StringUtils.isBlank(paramsObject.getString("pageSize"))
							&& StringUtils.isNumeric(paramsObject.getString("pageSize"))) {
						pageSize = paramsObject.getIntValue("pageSize");

					}
				}
			}
			SearchRequestBuilder requestBuilder = this.client.prepareSearch().setIndices("device")
					.setTypes("devicelist");
			// 设置查询器
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			if (StringUtils.isNotBlank(userId)) {
				queryBuilder.must(QueryBuilders.matchQuery("userId", userId).operator(Operator.AND)
						.zeroTermsQuery(ZeroTermsQuery.ALL));
			}
			if (StringUtils.isNotBlank(deviceId)) {
				queryBuilder.must(QueryBuilders.matchQuery("deviceId", deviceId).operator(Operator.AND)
						.zeroTermsQuery(ZeroTermsQuery.ALL));
			}
			if (StringUtils.isNotBlank(deviceName)) {
				queryBuilder.must(QueryBuilders.matchQuery("deviceName", deviceName).operator(Operator.AND)
						.zeroTermsQuery(ZeroTermsQuery.ALL));
			}
			if (StringUtils.isNotBlank(deviceTypeId)) {
				queryBuilder.must(QueryBuilders.matchQuery("deviceTypeId", deviceTypeId).operator(Operator.AND)
						.zeroTermsQuery(ZeroTermsQuery.ALL));
			}
			if (StringUtils.isNotBlank(deviceTypeName)) {
				queryBuilder.must(QueryBuilders.matchQuery("deviceTypeName", deviceTypeName).operator(Operator.AND)
						.zeroTermsQuery(ZeroTermsQuery.ALL));
			}
			requestBuilder.setQuery(queryBuilder);
			// 设置分页查询
			if (pageNo == 0) {
				pageNo = 1;
			}
			requestBuilder.setFrom((pageNo - 1) * pageSize);
			requestBuilder.setSize(pageSize);

			// 执行查询
			SearchResponse searchResponse = requestBuilder.get();
			SearchHits hits = searchResponse.getHits();

			List<Map<String, Object>> docList = Lists.newArrayList();
			if (hits != null && hits.totalHits > 0) {
				for (SearchHit searchHit : hits) {
					docList.add(searchHit.getSourceAsMap());
				}
			}

			if (docList.isEmpty()) {
				return ResultBuilder.failResult("不存在该系列设备");
			}
			// 封装返回结果
			Map<String, Object> resultMap = Maps.newHashMap();
			resultMap.put("totalCount", hits.totalHits);
			resultMap.put("pageNo", pageNo);
			resultMap.put("pageSize", pageSize);
			if (hits.totalHits < pageSize) {
				resultMap.put("pageSize", hits.totalHits);
			}
			resultMap.put("deviceInfoList", docList);

			return ResultBuilder.successResult(resultMap, "设备集合查询成功！");
		} catch (Exception e) {
			logger.error(String.format("[getDeviceList()->exception:%s]", e));
			return ResultBuilder.exceptionResult(e);
		}
	}

}
