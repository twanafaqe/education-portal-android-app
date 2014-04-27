package com.eduportal.videoportal.classes;

public class Lecture {
	private String isim;
	private String detay;
	private String resim;
	private String video;
	private String sure;

	public Lecture(String p) {
		this.isim = "";
		this.detay = "";
		this.resim = "";
		this.video = "";
		this.sure = "";
	}

	public Lecture() {
		// TODO Auto-generated constructor stub
	}

	public String getIsim() {
		return isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public String getDetay() {
		return detay;
	}

	public void setDetay(String detay) {
		this.detay = detay;
	}

	public String getResim() {
		return resim;
	}

	public void setResim(String resim) {
		this.resim = resim;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getSure() {
		return sure;
	}

	public void setSure(String sure) {
		this.sure = sure;
	}
	
	
}
