package com.newlivat.share.verticle;

import com.newlivat.common.constant.ConsumerAddress;
import com.newlivat.common.constant.NewLivatConstant;
import com.newlivat.common.verticle.BaseVerticle;

public class ShareVerticle extends BaseVerticle {

	@Override
	public void start() throws Exception {
		super.start();
		vertx.eventBus().consumer(ConsumerAddress.HEALTH_SHARE, message -> {
			LogInfoForServiceParam(ConsumerAddress.HEALTH_SHARE, message.body());
			message.reply(NewLivatConstant.UP);
		});

		log.info(this.getClass().getName() + "is deployed successfully.");
	}

}
