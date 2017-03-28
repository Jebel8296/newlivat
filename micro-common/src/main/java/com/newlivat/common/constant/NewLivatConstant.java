package com.newlivat.common.constant;

import com.newlivat.common.response.ReplyData;

import io.vertx.core.json.JsonObject;

public abstract class NewLivatConstant {

	public static final JsonObject UP = new ReplyData().toJson();
}
