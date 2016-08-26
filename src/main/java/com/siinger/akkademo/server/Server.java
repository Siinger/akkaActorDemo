package com.siinger.akkademo.server;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.siinger.akkademo.utils.ActorCommand;
import com.siinger.akkademo.utils.PropertiesUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


/**
 * ClassName: Master <br/>
 * Function: Master，连接client，并定时发送任务给client. <br/>
 * date: 2016年8月5日 下午5:06:58 <br/>
 *
 * @author siinger
 * @version 
 * @since JDK 1.7
 */
public class Server {
	public static void main(String[] args) {
		PropertiesUtils.load("core.properties");
		ExecutorService executor = Executors.newFixedThreadPool(3);
		// 启动Remote
		executor.execute(new Runnable() {

			public void run() {
				Config config = ConfigFactory.load("master-application.conf");
				final ActorSystem actorSystem = ActorSystem.create("ServerSystem", config.getConfig("ServerSys"));
				actorSystem.actorOf(Props.create(ServerActor.class), "serverActor");
				// DeployAgentAndExec.actorSystem = actorSystem;

				// 创建client任务
				String str = "akka.tcp://ServerSystem@" + PropertiesUtils.get("client.ip") + ":"
						+ PropertiesUtils.get("client.port") + "/user/serverActor";
				final ActorSelection remoteActor = actorSystem.actorSelection(str);
				actorSystem.scheduler().schedule(Duration.create(10, SECONDS), Duration.create(10, SECONDS),
						new Runnable() {
							public void run() {
								final ActorRef actor = actorSystem.actorOf(Props.create(ServerActor.class));
								remoteActor.tell(ActorCommand.DO_SOMETHING, actor);
							}
						}, actorSystem.dispatcher());
			}
		});
	}
}
