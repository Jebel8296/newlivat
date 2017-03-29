package com.newlivat.circuit.verticle;

import javax.ws.rs.core.Response;

import com.newlivat.common.constant.ConsumerAddress;
import com.newlivat.common.response.ReplyData;
import com.newlivat.common.verticle.BaseVerticle;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.json.JsonObject;

/**
 * 分享服务--断路器
 * 
 * @author xuxg
 *
 */
public class ShareCircuitBreaker extends BaseVerticle {

	private CircuitBreaker breaker = null;

	@Override
	public void start() throws Exception {
		super.start();
		CircuitBreakerOptions options = new CircuitBreakerOptions();
		options.setMaxFailures(3);
		options.setTimeout(2000);
		options.setFallbackOnFailure(true);
		options.setResetTimeout(10000);
		breaker = CircuitBreaker.create("share_breaker", vertx, options);
		vertx.eventBus().consumer(ConsumerAddress.CIRCUIT_BREAKER_SHARE, message -> {
			JsonObject m = (JsonObject) message.body();
			log.info("accept:" + m);
			ReplyData reply = new ReplyData(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), "Service Unavailable");
			breaker.executeWithFallback(future -> {
				vertx.eventBus().send(m.getString("address"), m.getJsonObject("param"), res -> {
					if (res.succeeded()) {
						JsonObject result = (JsonObject) res.result().body();
						future.complete(result.encodePrettily());
					} else {
						future.fail(reply.encodePrettily());
					}
				});
			}, error -> {
				log.error("********请检查分享服务是否正常启动********");
				return reply.encodePrettily();
			}).setHandler(ar -> {
				log.info("reply:" + ar.result());
				message.reply(ar.result());
			});
		});

		log.info(this.getClass().getName() + "is deployed successfully.");
	}

}
