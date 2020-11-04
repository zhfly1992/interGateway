/*
* File name: DeviceCudRestController.java								
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
* 1.0			guofeilong		2018年01月16日
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

import com.run.big.data.center.cud.service.DeviceCudRestService;
import com.run.big.data.center.entity.UrlConstant;
import com.run.entity.common.Result;
import com.run.gathering.center.api.entity.Device;
import com.run.gathering.center.api.entity.DeviceDTO;

/**
 * @Description: 设备增删改rest接口
 * @author: 郭飞龙
 * @version: 1.0, 2018年01月16日
 */
@RestController
@RequestMapping(value = UrlConstant.IOT_VERSIONS)
public class DeviceCudRestController {

	@Autowired
	private DeviceCudRestService deviceCudRestService;
	
	
    /**
     * @param devices 新增设备的信息
     * @return Result<Device>
     * @Description: 新增设备
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = UrlConstant.DEVICE_ADD, method = RequestMethod.POST)
    public Result<Device> addDevice(@RequestBody String devices) {
        return deviceCudRestService.addDevice(devices);
    }
    
    
    
	/**
	 * 
	 * @Description:设备删除(假删除)
	 * @param id 设备id
	 *             
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.DEVICE_DELETE, method = RequestMethod.DELETE)
	public Result<String> deviceDelete(@PathVariable("id") String id) {
		return deviceCudRestService.deviceDelete(id);
	}
	
	
	
	/**
	 * 
	 * @Description:更新设备
	 * @param device
	 *            设备
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.DEVICE_UPDATE, method = RequestMethod.PUT)
	public Result<DeviceDTO> updateDevice(@PathVariable("id") String id,@RequestBody Device device) {

		return deviceCudRestService.updateDevice(id,device);
	}
	
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = UrlConstant.REMOTE_CONTROL, method = RequestMethod.PUT)
	public Result<Boolean> remoteControl(@PathVariable("id") String id, @RequestBody String desiredInfo) {
		return deviceCudRestService.remoteControl(id, desiredInfo);
	}
}
