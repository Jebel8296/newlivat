package com.newlivat.common.rest;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

public abstract class BaseRest {

	protected Logger log = Logger.getLogger(getClass());
	protected DeliveryOptions options = new DeliveryOptions().setSendTimeout(5000L);

	protected void send(Vertx vertx, String address, JsonObject message, DeliveryOptions options,
			AsyncResponse asyncResponse) {
		vertx.eventBus().send(address, message, options, msg -> {
			if (msg.succeeded()) {
				JsonObject result = (JsonObject) msg.result().body();
				ResponseBuilder builder = Response.ok(result.encode());
				asyncResponse.resume(builder.build());
			} else {
				asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
			}
		});
	}

}
