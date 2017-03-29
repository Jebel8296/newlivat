package com.newlivat.gateway.verticle;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.server.vertx.VertxRegistry;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.springframework.stereotype.Component;

import com.newlivat.common.response.ReplyData;
import com.newlivat.common.verticle.BaseVerticle;
import com.newlivat.gateway.rest.ShareRest;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;

/**
 * 
 * 荟分享-网关
 * 
 * @author xxug
 *
 */
@Component
public class Gateway extends BaseVerticle {

	@Override
	public void start() throws Exception {
		super.start();
		VertxResteasyDeployment vertxResteasyDeployment = new VertxResteasyDeployment();
		vertxResteasyDeployment.start();
		VertxRegistry vertxRegistry = vertxResteasyDeployment.getRegistry();
		vertxRegistry.addPerInstanceResource(ShareRest.class);
		Router router = Router.router(vertx);
		router.route().handler(CookieHandler.create());
		enableCorsSupport(router);
		router.route().handler(this::checkRequest);
		router.route().handler(this::checkLogin);
		router.route().handler(this::checkAuth);
		router.route("/rest/*").handler(routerHandler -> {
			new VertxRequestHandler(vertx, vertxResteasyDeployment).handle(routerHandler.request());
		});
		vertx.createHttpServer().requestHandler(router::accept).listen(6601);
		log.info(this.getClass().getName() + "is deployed successfully.");
	}

	private void checkRequest(RoutingContext context) {
		HttpServerRequest request = context.request();
		log.info("request:" + request.toString());
		context.next();
	}

	private void checkLogin(RoutingContext context) {
		boolean login = Boolean.FALSE;
		if (!login) {
			context.next();
		} else {
			ReplyData reply = new ReplyData(418, "登陆失效,请重新登陆");
			context.response().end(reply.encodePrettily());
		}
	}

	private void checkAuth(RoutingContext context) {
		boolean auth = Boolean.FALSE;
		if (!auth) {
			context.next();
		} else {
			ReplyData reply = new ReplyData(Response.Status.UNAUTHORIZED.getStatusCode(), "用户无权进行此操作");
			context.response().end(reply.encodePrettily());
		}
	}

}