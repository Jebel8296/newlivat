package com.newlivat.gateway.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.newlivat.common.constant.ConsumerAddress;
import com.newlivat.common.rest.BaseRest;

import io.vertx.core.Vertx;

/**
 * 分享Rest
 * 
 * @author xuxg
 *
 */
@Path("/rest/share")
public class ShareRest extends BaseRest {

	@GET
	@Path("/health")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public void health(@Suspended final AsyncResponse asyncResponse, @Context Vertx vertx) {
		send(vertx, ConsumerAddress.HEALTH_SHARE, null, options, asyncResponse);
	}

}
