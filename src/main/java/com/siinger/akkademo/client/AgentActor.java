package com.siinger.akkademo.client;

import org.apache.log4j.Logger;

import akka.actor.UntypedActor;

import com.siinger.akkademo.utils.ActorCommand;

/**
 * ClassName: AgentActor <br/>
 * Function: Agent actor，完成具体的工作 <br/>
 * date: 2016年8月5日 下午5:06:34 <br/>
 *
 * @author siinger
 * @version 
 * @since JDK 1.7
 */
public class AgentActor extends UntypedActor {

	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onReceive(Object message) {
		if (message == ActorCommand.HeartBeat_OK) {
			logger.info(getSender().path().address().host().get() + " agent is start");
			getContext().stop(getSelf());
		} else if (message == ActorCommand.DO_SOMETHING) {
			logger.info("agentServer do something!");
			getSender().tell(ActorCommand.DO_SOMETHING_OK, getSelf());
		}
	}
}
