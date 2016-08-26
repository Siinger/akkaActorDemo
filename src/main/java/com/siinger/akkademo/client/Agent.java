package com.siinger.akkademo.client;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

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
 * ClassName: Agent <br/>
 * Function: Agent 测试类. <br/>
 * date: 2016年8月5日 下午5:05:15 <br/>
 *
 * @author siinger
 * @version
 * @since JDK 1.7
 */
public class Agent {

	Logger logger = Logger.getLogger(this.getClass());

	public void init() {
		PropertiesUtils.load("core.properties");
	}

	public static void main(String[] args) {
		Agent agent = new Agent();
		agent.init();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new Runnable() {
			public void run() {
				// 创建remote 接受命令
				Config config = ConfigFactory.load("agent-application.conf");
				// 修改IP设置，TODO
				Config ipConfig = ConfigFactory.parseString("ServerSys.akka.remote.netty.tcp.hostname="
						+ PropertiesUtils.get("masterServer.ip"));
				final ActorSystem actorSystem = ActorSystem.create("ServerSystem", ipConfig.withFallback(config)
						.getConfig("ServerSys"));
				actorSystem.actorOf(Props.create(AgentActor.class), "serverActor");
				// 创建client 10秒发送心跳
				String str = "akka.tcp://ServerSystem@" + PropertiesUtils.get("masterServer.ip") + ":"
						+ PropertiesUtils.get("masterServer.port") + "/user/serverActor";
				final ActorSelection remoteActor = actorSystem.actorSelection(str);
				actorSystem.scheduler().schedule(Duration.create(5, SECONDS), Duration.create(5, SECONDS),
						new Runnable() {
							public void run() {
								final ActorRef actor = actorSystem.actorOf(Props.create(AgentActor.class));
								remoteActor.tell(ActorCommand.HeartBeat, actor);
							}
						}, actorSystem.dispatcher());

			}
		});
	}

}
