package com.lbsp.promotion.entity.model;


/**
 *
 * Created on 2015-03-31 09:41:11
 *
 * @author 简易自动代码创建工具
 */
public class Preferential extends BaseModel {
	private String title;
	private String type;
	private String was_price;
	private String now_price;
	private String off;
	private String description;
	private Integer status;
	private Integer shop_id;
	private String pic_path;
    private String mark;
	private Long start_time;
	private Long end_time;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setWas_price(String was_price) {
		this.was_price = was_price;
	}

	public String getWas_price() {
		return this.was_price;
	}

	public void setNow_price(String now_price) {
		this.now_price = now_price;
	}

	public String getNow_price() {
		return this.now_price;
	}

	public void setOff(String off) {
		this.off = off;
	}

	public String getOff() {
		return this.off;
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

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public Integer getShop_id() {
		return this.shop_id;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public String getPic_path() {
		return this.pic_path;
	}

	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}

	public Long getStart_time() {
		return this.start_time;
	}

	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}

	public Long getEnd_time() {
		return this.end_time;
	}


}