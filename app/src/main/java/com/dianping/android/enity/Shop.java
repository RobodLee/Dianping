package com.dianping.android.enity;

import java.io.Serializable;

public class Shop implements Serializable{
	
	private String id;
	private String name;
	private String address;
	private String area;
	private String opentime;
	private String tel;
	private String lat;
	private String lon;
	private String trafficinfo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getOpentime() {
		return opentime;
	}
	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getTrafficinfo() {
		return trafficinfo;
	}
	public void setTrafficinfo(String trafficinfo) {
		this.trafficinfo = trafficinfo;
	}

}
