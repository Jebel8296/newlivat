package com.newlivat.share;

import java.util.Properties;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.newlivat.common.util.ConfigUtil;
import com.newlivat.share.verticle.ShareVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;

public class Application {
	private static Properties properties = ConfigUtil.getDefaultConfig();
	private static ClassPathXmlApplicationContext applicationContext = null;

	public static void main(String[] args) {
		// applicationContext = new
		// ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		// ShareVerticle share =
		// applicationContext.getBean(ShareVerticle.class);

		ClusterManager mgr = new ZookeeperClusterManager();
		VertxOptions options = new VertxOptions().setClusterManager(mgr);
		options.setClusterHost(properties.getProperty("cluster.host", "localhost"));
		options.setClusterPublicHost(properties.getProperty("cluster.public.host", "localhost"));
		DeploymentOptions option = new DeploymentOptions();
		option.setWorker(true);
		Vertx.clusteredVertx(options, resultHandler -> {
			if (resultHandler.succeeded()) {
				Vertx vertx = resultHandler.result();
				vertx.deployVerticle(ShareVerticle.class.getName(), option);
			}
		});
	}

}
