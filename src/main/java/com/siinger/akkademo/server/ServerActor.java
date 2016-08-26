package com.siinger.akkademo.server;

import org.apache.log4j.Logger;

import akka.actor.UntypedActor;

import com.siinger.akkademo.utils.ActorCommand;

/**
 * ClassName: MasterActor <br/>
 * Function: 接受client消息 <br/>
 * date: 2016年8月5日 下午5:33:40 <br/>
 *
 * @author siinger
 * @version 
 * @since JDK 1.7
 */
public class ServerActor extends UntypedActor {

	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onReceive(Object message) throws Exception {
		if (message == ActorCommand.HeartBeat) {
			logger.info(getSender().path().address().host().get() + " agent is start");
			getSender().tell(ActorCommand.HeartBeat_OK, getSelf());
		} else if (message == ActorCommand.DO_SOMETHING_OK) {
			logger.info(getSender().path().address().host().get() + " do something ok!");
			getContext().stop(getSelf());
		}
	}
}
