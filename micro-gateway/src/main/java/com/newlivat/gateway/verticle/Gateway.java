package com.newlivat.gateway.verticle;

import org.jboss.resteasy.plugins.server.vertx.VertxRegistry;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.springframework.stereotype.Component;

import com.newlivat.common.verticle.BaseVerticle;
import com.newlivat.gateway.rest.ShareRest;

import io.vertx.ext.web.Router;
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
		router.route("/rest/*").handler(routerHandler -> {
			new VertxRequestHandler(vertx, vertxResteasyDeployment).handle(routerHandler.request());
		});
		vertx.createHttpServer().requestHandler(router::accept).listen(6601);
		log.info(this.getClass().getName() + "is deployed successfully.");
	}

}