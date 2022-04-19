/**
 * Copyright (c) 2022 Klaus Tachtler. All Rights Reserved.
 * Klaus Tachtler. <klaus@tachtler.net>
 * http://www.tachtler.net
 */
package net.tachtler.jmilter;

import org.apache.commons.cli.ParseException;
import org.nightcode.common.service.ServiceManager;
import org.nightcode.milter.MilterHandler;
import org.nightcode.milter.config.GatewayConfig;
import org.nightcode.milter.net.MilterChannelHandler;
import org.nightcode.milter.net.MilterGatewayManager;
import org.nightcode.milter.util.Actions;
import org.nightcode.milter.util.ProtocolSteps;

/*******************************************************************************
 * JMilter Server for connections from an MTA.
 * 
 * JMilter is an Open Source implementation of the Sendmail milter protocol, for
 * implementing milters in Java that can interface with the Sendmail or Postfix
 * MTA.
 * 
 * Java implementation of the Sendmail Milter protocol based on the project of
 * org.nightcode.jmilter from dmitry@nightcode.org.
 * 
 * @author Klaus Tachtler. <klaus@tachtler.net>
 * 
 *         Homepage : http://www.tachtler.net
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License..
 * 
 *         Copyright (c) 2022 by Klaus Tachtler.
 ******************************************************************************/
public class InfoMilter {

	/**
	 * Constructor.
	 */
	public InfoMilter() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InfoMilterCLIArgParserException {

		/*
		 * Read the arguments from command line into the dwuaFileBean.
		 */
		InfoMilterCLIArgsParserBean argsBean = new InfoMilterCLIArgsParserBean(null, 0, null, null);

		try {
			argsBean = InfoMilterCLIArgsParser.readArgs(argsBean, args);
		} catch (ParseException eArgsBean) {
			throw new InfoMilterCLIArgParserException(
					"***** Program stop, because InfoMilter could not be initialized! ***** (For more details, see error messages and caused by below).",
					eArgsBean);
		}

		/*
		 * Start JMilter only, if all required args are set.
		 */
		if (argsBean.getInetAddress() != null && argsBean.getPort() != 0 && argsBean.getTcpLoggingEnabled() != null
				&& argsBean.getTcpLogLevel() != null) {
			GatewayConfig gatewayConfig = new GatewayConfig();
			gatewayConfig.setAddress(argsBean.getInetAddress().getHostAddress());
			gatewayConfig.setPort(argsBean.getPort());
			gatewayConfig.setTcpLoggingEnabled(argsBean.getTcpLoggingEnabled());
			gatewayConfig.setTcpLogLevel(argsBean.getTcpLogLevel());

			// indicates what changes you intend to do with messages
			Actions milterActions = Actions.builder().addHeader().build();

			// indicates which steps you want to skip
			ProtocolSteps milterProtocolSteps = ProtocolSteps.builder().build();

			// a simple milter handler
			MilterHandler milterHandler = new InfoMilterHandler(milterActions, milterProtocolSteps);

			MilterGatewayManager gatewayManager = new MilterGatewayManager(gatewayConfig,
					() -> new MilterChannelHandler(milterHandler), ServiceManager.instance());

			gatewayManager.start();
		}

	}

}
