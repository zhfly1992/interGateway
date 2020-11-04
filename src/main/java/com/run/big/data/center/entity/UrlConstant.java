/*
 * File name: LogConstants.java
 *
 * Purpose:
 *
 * Functions used and called: Name Purpose ... ...
 *
 * Additional Information:
 *
 * Development History: Revision No. Author Date 1.0 田明 2018年01月16日 ... ... ...
 *
 ***************************************************/
package com.run.big.data.center.entity;

/**
 * @Description: url常量类
 * @author: 田明
 * @version: 1.0, 2018年01月16日
 */
public class UrlConstant {
	/** 消息网关版本号 */

	public static final String	IOT_VERSIONS					= "/v3";
	/** 查询设备信息 */
	public static final String	QUERY_DEVICEINFO				= "/device/{id}";
	/** 添加设备 */
	public static final String	DEVICE_ADD						= "/device";
	/** 删除设备 */
	public static final String	DEVICE_DELETE					= "/device/{id}";
	/** 修改设备 */
	public static final String	DEVICE_UPDATE					= "/device/{id}";
	/** 查询设备当前状态 */
	public static final String	FIND_DEVICE_STATE				= "/device/state/{id}";
	public static final String	QUERY_DEVICELIST				= "/user/devices/{userId}";
	public static final String	DEVICE_DELETE_OR_RESTORE		= "/delOrRestore";
	public static final String	REMOTE_CONTROL					= "/device/state/{id}";
	/** 根据appTag获取设备集合 */
	public static final String	QUERY_DEVICELIST_BY_APPTAGS		= "/appTag/devices";
	/** 根据用户id查询apptag集合 */
	public static final String	QUERY_APPTAGS_BY_USERID			= "/appTags/{userId}";

	/** 用户中心 */
	/** 查询组织架构树 */
	public static final String	ORGANIZATION_TYPE				= "/organizations/{accessSecret}";
	/** 分页查询组织人员 */
	public static final String	USER_TYPE_PAGE					= "/organization/users/{accessSecret}";
	/** 根据组织id查詢组织信息 */
	public static final String	ORGANIZATION_ID					= "/organization/{organizationId}";
	/** 检测组织下面是否存在子类组织 */
	public static final String	CHECK_CHILD_BY_ORGANID			= "/organization/sonExist/{organizationId}";
	/** 检测组织是否重名 */
	public static final String	CHECK_ORGANIZATION_NAME			= "/organization/organNameExist";
	/** 通过token查询用户信息 */
	public static final String	USER_TOKEN						= "/user/{token}";
	/** 人员管理:查询当前用户所在的接入方所有人员信息 */
	public static final String	USER_QUERY_BY_AS				= "/users/{accessSecret}";
	/** 新增组织 */
	public static final String	ORGANIZATION_ADD_BY_AS			= "/organization";
	/** 根据接入方秘钥修改组织信息 */
	public static final String	ORGANIZATION_UPDATE_BY_AS		= "/organization/{organizationid}/{accessSecret}";
	/** 管理组织状态 */
	public static final String	ORGANIZATION_MANAGER_STATE		= "/organization/state/{organizationId}";
	/** 添加人员(关联组织，关联角色)RS */
	public static final String	USER_SAVE						= "/user";
	/** 修改人员(关联组织，关联角色)RS */
	public static final String	USER_UPDATE_CONNECTION			= "/user/{id}";
	/** 启用或者禁用人员 */
	public static final String	USER_MANAGER					= "/user/state/{id}";
	/** 根据用户id查询用户所属接入方信息 */
	public static final String	GET_ASINFO_BY_USERID			= "/accessInfo/{userId}";
	/** 根据用户信息查询接入方信息 */
	public static final String	GET_ASINFO_BY_USERINFO			= "/accessInfo/userInfo";
	/** 根据接入方密钥查询接入方信息 */
	public static final String	GET_ASINFO_BY_AS				= "/accessInformation/{accessSecret}";
	/** 根据接入方密钥查询当前接入方下用户数量 */
	public static final String	GET_USERIDS_BY_AS				= "/userNumber/{accessSecret}";
	/** 根据组织id查询组织及其子组织信息 */
	public static final String	GET_OWN_AND_CHILD_BY_ORGANID	= "/organization/ownAndSonInfo/{organizationId}";
	/** 根据组织id查询其父组织id */
	public static final String	GET_PARENT_BY_ORGANID			= "/organization/parentIds/{organizationId}";
	/** 批量查询组织名 */
	public static final String	GET_ORGANIZATIONS_BY_ORGANID	= "/organizationNames/organizationIds";
	/** 根据接入方密钥查询最近一个月的用户使用率 */
	public static final String	GET_USERUSAGE_BY_AS				= "/user/usage/{accessSecret}";
	/** 根据人员id查询其厂家id */
	public static final String	GET_FACTORY_ID					= "/user/factoryId";
	/** 根据人员id查询人员信息 */
	public static final String	GET_USER_INFO					= "/user/info/{userId}";
	/** 根据人员id批量查询其人员信息 */
	public static final String	GET_PERSON_INFO					= "/user/info";

	/** 登录相关 */
	/** 用户注册 */
	public static final String	USER_REGISTER					= "/user/register";
	/** 修改密码 */
	public static final String	CHANGE_PASSWORD					= "/user/password";
	/** 根据验证码重置密码 */
	public static final String	CHANGE_PASSWORD_BY_SENDNUM		= "/user/password/verificationCode";
	/** 修改手机号 */
	public static final String	CHANGE_PHONE_NUM				= "/user/phoneNum";
	/** 用户认证,登录 */
	public static final String	USER_LOGIN						= "/user/authentication";
	/** 用户认证，登录ByCode(用于铁塔免秘登录的方式) */
	public static final String	USER_LOGIN_APPCODE				= "/user/authenticationCode";
	/** 用户退出登录 */
	public static final String	USER_LOGOUT						= "/user/logout/{token}";
	/** 忘记密码, 发送手机或者邮箱验证 */
	public static final String	SENDEMIMOB						= "/user/validation";
	/** 检查用户手机号或者邮箱是否存在 (现不允许手机号登录) */
	public static final String	CHECK_USER_EXIT					= "/user/emailMobExist";										
	/** 手机验证码验证身份(通过手机修改密码时) */
	public static final String	CHECK_VERIFICATION_CODE_EXIT	= "/user/verificationCodeExist/{verificationCode}/{phoneNum}";

	/** 权限中心 */
	/** 根据接入方密钥分页查询岗位权限 */
	public static final String	QUERY_AS_ROLE					= "/roles/{accessSecret}";
	/** 查询角色已经拥有的或者未拥有的权限信息 */
	public static final String	QUERY_PERMISSIONS				= "/role/authzs/{accessSecret}/{applicationType}";
	/** 根据角色id和菜单id查询共有的功能类型 */
	public static final String	QUERY_BY_RID_MID				= "/role/{roleId}/{menuId}";
	/** 校验岗位名称是否重复 */
	public static final String	CHECK_ORG_ROLE_NAME				= "/role/nameExist/{organizeId}";
	/** 根据用户id和接入方秘钥查询角色信息 */
	public static final String	FIND_USERINFO_BY_UID_AS			= "/role/roleInfo/{accessSecret}/{userId}";
	/** 根据角色id查询角色所绑定的菜单信息 */
	public static final String	GET_MENUINFO_BY_RID				= "/role/menuInfo/{roleId}/{applicationType}";
	/** 根据组织id查询组织下面的岗位信息 */
	public static final String	GET_PERMISSIONS_BY_OID			= "/roleInfo/{organizeId}";
	/** 保存用户角色以及他的关联信息（组织以及菜单权限） */
	public static final String	SAVE_ROLE_INFO					= "/role";
	/** 修改用户角色以及他的关联信息（组织以及菜单权限） */
	public static final String	UPDATE_ROLE_INFO				= "/role/{roleId}";
	/** 转换用户角色状态 */
	public static final String	MANAGER_ROLE_STATE				= "/role/state/{roleId}";
	/** 刷新登录时间 */
	public static final String	REFRESH_LOGIN_TIME				= "/refreshLoginTime";
	/** 修改用户公司信息 */
	public static final String	USER_COMPANY					= "/user/company/{id}";
	/** 通过关键字模糊查询用户名返回id */
	public static final String	FIND_USER_KEYWORD				= "/user/findUserKeyword";
	/** 查询用户的名称->ids */
	public static final String	FIND_USER_NAME					= "/user/findUserName";
	/** 查询该组织id下所有的组织id */
	public static final String	FIND_ORG_ID						= "/tenmentResource/findOrgId";
	/** 查询组织信息名称 */
	public static final String	FIND_ORG_NAME					= "/tenmentResource/findOrgName";
	/** 登录时获取密码加密的公共秘钥*/
	public static final String  GET_PUBLIC_KEY                  = "/user/getPublicKey";
	/**发送手机或者短信验证*/
	public static final String  GET_SENDEMIMOB                  = "/user/sendEmiMob";
	

}