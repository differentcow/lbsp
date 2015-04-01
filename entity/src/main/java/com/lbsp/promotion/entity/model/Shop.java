package com.lbsp.promotion.entity.model;


/**
 *
 * Created on 2015-04-01 09:20:42
 *
 * @author 简易自动代码创建工具
 */
public class Shop extends BaseModel {
	private String shop_name;
	private String contact_user;
	private String contact_phone;
	private String verify_pic_path;
	private String description;
	private Integer status;
	private String pic_path;
	private Double latitude;
	private Double longitude;
	private String area_code;
	private String sell_no;
	private String address;
	private Integer customer_id;


	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_name() {
		return this.shop_name;
	}

	public void setContact_user(String contact_user) {
		this.contact_user = contact_user;
	}

	public String getContact_user() {
		return this.contact_user;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getContact_phone() {
		return this.contact_phone;
	}

	public void setVerify_pic_path(String verify_pic_path) {
		this.verify_pic_path = verify_pic_path;
	}

	public String getVerify_pic_path() {
		return this.verify_pic_path;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public String getPic_path() {
		return this.pic_path;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getArea_code() {
		return this.area_code;
	}

	public void setSell_no(String sell_no) {
		this.sell_no = sell_no;
	}

	public String getSell_no() {
		return this.sell_no;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public Integer getCustomer_id() {
		return this.customer_id;
	}


}