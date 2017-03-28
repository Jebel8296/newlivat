package com.newlivat.common.response;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * 返回数据
 * 
 * @author xuxg
 *
 */
public class ReplyData {
	private int code;
	private String msg;
	private Object result;
	private Long ts = System.currentTimeMillis();

	public String encodePrettily() {
		return Json.encodePrettily(this);
	}

	public JsonObject toJson() {
		return new JsonObject(encodePrettily());
	}

	public ReplyData() {
		super();
		this.code = 200;
		this.msg = "UP";
	}

	public ReplyData(int code) {
		super();
		this.code = code;
	}

	public ReplyData(int code, String msg, Object result) {
		super();
		this.code = code;
		this.msg = msg;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

}
