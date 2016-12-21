package com.joytouch.superlive.javabean;

public class SoftwareUpdate extends BaseBean {

	// "ver": "1.1",
	// "updateDescription": "1.可查看比分直播\n2.bugfix",
	// "downloadUrl": "http://itunes.apple.com/"
	// "codeVer":"20140105"
	// "force":0

	public String ver="";
	public int force; //如果force》1，要升级，手动升级有更新时要升级
	public String downloadUrl="";
	public String updateDescription="";
	public String codeVer="";
}
