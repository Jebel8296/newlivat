package com.newlivat.circuit;

import java.util.Properties;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.newlivat.circuit.verticle.ShareCircuitBreaker;
import com.newlivat.common.util.ConfigUtil;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;

public class CircuitApplication {
	private static Properties properties = ConfigUtil.getDefaultConfig();
	private static ClassPathXmlApplicationContext applicationContext = null;

	public static void main(String[] args) {
		// applicationContext = new
		// ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		// Gateway gateway = applicationContext.getBean(Gateway.class);

		ClusterManager mgr = new ZookeeperClusterManager();
		VertxOptions options = new VertxOptions().setClusterManager(mgr);
		options.setClusterHost(properties.getProperty("cluster.host", "localhost"));
		options.setClusterPublicHost(properties.getProperty("cluster.public.host", "localhost"));
		DeploymentOptions option = new DeploymentOptions();
		option.setWorker(true);
		Vertx.clusteredVertx(options, resultHandler -> {
			if (resultHandler.succeeded()) {
				Vertx vertx = resultHandler.result();
				vertx.deployVerticle(ShareCircuitBreaker.class.getName(), option);
			}
		});
	}

}
