/**
 * Copyright (c) 2018 Klaus Tachtler. All Rights Reserved.
 * Klaus Tachtler. <klaus@tachtler.net>
 * http://www.tachtler.net
 */
package net.tachtler.jmilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.nightcode.milter.AbstractMilterHandler;
import org.nightcode.milter.MessageModificationService;
import org.nightcode.milter.MilterContext;
import org.nightcode.milter.MilterException;
import org.nightcode.milter.command.CommandProcessor;
import org.nightcode.milter.net.MilterPacket;
import org.nightcode.milter.util.Actions;
import org.nightcode.milter.util.ProtocolSteps;

/*******************************************************************************
 * JMilter Handler for handling connections from an MTA.
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
 *         Copyright (c) 2018 by Klaus Tachtler.
 ******************************************************************************/
public class InfoMilterHandler extends AbstractMilterHandler {

	private static Logger log = LogManager.getLogger();

	private static int timeout = 3;
	private static int ttl = 64;

	/**
	 * @param milterActions
	 * @param milterProtocolSteps
	 */
	public InfoMilterHandler(Actions milterActions, ProtocolSteps milterProtocolSteps) {
		super(milterActions, milterProtocolSteps);
	}

	/**
	 * @param milterActions
	 * @param milterProtocolSteps
	 * @param messageModificationService
	 */
	public InfoMilterHandler(Actions milterActions, ProtocolSteps milterProtocolSteps,
			MessageModificationService messageModificationService) {
		super(milterActions, milterProtocolSteps, messageModificationService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#connect(org.nightcode.milter.
	 * MilterContext, java.lang.String, java.net.InetAddress)
	 */
	@Override
	public void connect(MilterContext context, String hostname, @Nullable InetAddress address) throws MilterException {

		log.info("----------------------------------------: ");
		log.info(
				"JMilter - ENTRY: connect                : MilterContext context, String hostname, @Nullable InetAddress address");
		log.info("----------------------------------------: ");

		log.info("*hostname                               : " + hostname);
		log.info("*address.getCanonicalHostName()         : " + address.getCanonicalHostName());
		log.info("*address.getHostAddress()               : " + address.getHostAddress());
		log.info("*address.getHostName()                  : " + address.getHostName());

		byte[] addr = address.getAddress();
		short[] octet = new short[4];

		for (int i = 0; i <= addr.length - 1; i++) {
			if (addr[i] <= 127 && addr[i] >= -127 && addr[i] < 0) {
				octet[i] = (short) (addr[i] + 256);
			} else if (addr[i] <= 127 && addr[i] >= -127 && addr[i] > 0) {
				octet[i] = addr[i];
			} else {
				octet[i] = 0;
			}
		}

		log.info("*address.getAddress()                   : " + "Octet: " + Arrays.toString(octet) + " / Byte: "
				+ Arrays.toString(addr));
		log.info("*address.isAnyLocalAddress()            : " + address.isAnyLocalAddress());
		log.info("*address.isLinkLocalAddress()           : " + address.isLinkLocalAddress());
		log.info("*address.isLoopbackAddress()            : " + address.isLoopbackAddress());
		log.info("*address.isMCGlobal()                   : " + address.isMCGlobal());
		log.info("*address.isMCLinkLocal()                : " + address.isMCLinkLocal());
		log.info("*address.isMCNodeLocal()                : " + address.isMCNodeLocal());
		log.info("*address.isMCOrgLocal()                 : " + address.isMCOrgLocal());
		log.info("*address.isMCSiteLocal()                : " + address.isMCSiteLocal());
		log.info("*address.isMulticastAddress()           : " + address.isMulticastAddress());

		try {
			log.info("*address.isReachable(timeout)           : " + address.isReachable(timeout));
		} catch (IOException eIsReachableTimeout) {
			log.error("IOException                             : " + eIsReachableTimeout);
			eIsReachableTimeout.printStackTrace();
		}

		NetworkInterface netif = null;
		try {
			netif = NetworkInterface.getByInetAddress(address);

			log.info("*netif.getDisplayName()                 : " + netif.getDisplayName());
			log.info("*netif.getIndex()                       : " + netif.getIndex());
			log.info("*netif.getMTU()                         : " + netif.getMTU());
			log.info("*netif.getName()                        : " + netif.getName());

			byte[] hwAddr = netif.getHardwareAddress();
			String hwAddrString = null;
			StringBuilder stringBuilder = new StringBuilder();

			if (hwAddr != null && hwAddr.length > 0) {
				for (byte b : hwAddr) {
					stringBuilder.append(String.format("%02x:", b));
				}
				hwAddrString = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
			}

			log.info("*netif.getHardwareAddress()             : " + hwAddrString);

			Enumeration<InetAddress> inetAddresses = netif.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				log.info("*netif.getInetAddresses()               : " + inetAddress);
			}

			log.info("*netif.getInterfaceAddresses()          : " + netif.getInterfaceAddresses());
			log.info("*netif.getParent()                      : " + netif.getParent());

			Enumeration<NetworkInterface> networkInterfaces = netif.getSubInterfaces();
			for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
				log.info("*netif.getSubInterfaces()               : " + networkInterface);
			}

			log.info("*netif.isLoopback()                     : " + netif.isLoopback());
			log.info("*netif.isPointToPoint()                 : " + netif.isPointToPoint());
			log.info("*netif.isUp()                           : " + netif.isUp());
			log.info("*netif.isVirtual()                      : " + netif.isVirtual());
			log.info("*netif.supportsMulticast()              : " + netif.supportsMulticast());

		} catch (SocketException eNetif) {
			log.error("SocketException                         : " + eNetif);
			eNetif.printStackTrace();
		}

		try {
			log.info("*address.isReachable(netif, ttl, time...: " + address.isReachable(netif, ttl, timeout));
		} catch (IOException eIsReachableNetifTtlTimeout) {
			log.error("IOException                             : " + eIsReachableNetifTtlTimeout);
			eIsReachableNetifTtlTimeout.printStackTrace();
		}
		log.info("*address.isSiteLocalAddress()           : " + address.isSiteLocalAddress());

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);

		log.info("----------------------------------------: ");
		log.info(
				"JMilter - LEAVE: connect                : MilterContext context, String hostname, @Nullable InetAddress address");
		log.info("----------------------------------------: ");

		/*
		 * Change the SMFIS action possible values are:
		 * 
		 * SMFIS_CONTINUE, SMFIS_REJECT, SMFIS_DISCARD, SMFIS_ACCEPT, SMFIS_TEMPFAIL,
		 * SMFIS_SKIP
		 * 
		 * context.sendPacket(MilterPacketUtil.SMFIS_CONTINUE);
		 */

		super.connect(context, hostname, address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#helo(org.nightcode.milter.
	 * MilterContext, java.lang.String)
	 */
	@Override
	public void helo(MilterContext context, String helohost) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: helo                   : MilterContext context, String helohost");
		log.info("----------------------------------------: ");

		log.info("*helohost                               : " + helohost);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: helo                   : MilterContext context, String helohost");
		log.info("----------------------------------------: ");

		super.helo(context, helohost);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#envfrom(org.nightcode.milter.
	 * MilterContext, java.util.List)
	 */
	@Override
	public void envfrom(MilterContext context, List<String> from) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: envfrom                : MilterContext context, List<String> from");
		log.info("----------------------------------------: ");

		for (int i = 0; i <= from.size() - 1; i++) {
			log.info("*from.get(i)                            : " + "[" + i + "] " + from.get(i));
		}

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: envfrom                : MilterContext context, List<String> from");
		log.info("----------------------------------------: ");

		super.envfrom(context, from);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#envrcpt(org.nightcode.milter.
	 * MilterContext, java.util.List)
	 */
	@Override
	public void envrcpt(MilterContext context, List<String> recipients) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: envrcpt                : MilterContext context, List<String> recipients");
		log.info("----------------------------------------: ");

		for (int i = 0; i <= recipients.size() - 1; i++) {
			log.info("*recipients.get(i)                      : " + "[" + i + "] " + recipients.get(i));
		}

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: envrcpt                : MilterContext context, List<String> recipients");
		log.info("----------------------------------------: ");

		super.envrcpt(context, recipients);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#data(org.nightcode.milter.
	 * MilterContext, byte[])
	 */
	@Override
	public void data(MilterContext context, byte[] payload) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: data                   : MilterContext context, byte[] payload");
		log.info("----------------------------------------: ");

		byte[] dataPayload = payload;
		String dataPayloadString = null;
		StringBuilder stringBuilder = new StringBuilder();

		if (dataPayload != null && dataPayload.length > 0) {
			for (byte b : dataPayload) {
				stringBuilder.append(String.format("%02x:", b));
			}
			dataPayloadString = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
		}

		log.info("*payload                                : " + dataPayloadString);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: data                   : MilterContext context, byte[] payload");
		log.info("----------------------------------------: ");

		super.data(context, payload);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#header(org.nightcode.milter.
	 * MilterContext, java.lang.String, java.lang.String)
	 */
	@Override
	public void header(MilterContext context, String headerName, String headerValue) throws MilterException {

		log.info("----------------------------------------: ");
		log.info(
				"JMilter - ENTRY: header                 : MilterContext context, String headerName, String headerValue");
		log.info("----------------------------------------: ");

		log.info("*headerName: headerValue                : " + headerName + ": " + headerValue);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);
		logContext(context, CommandProcessor.SMFIC_HEADER);

		log.info("----------------------------------------: ");
		log.info(
				"JMilter - LEAVE: header                 : MilterContext context, String headerName, String headerValue");
		log.info("----------------------------------------: ");

		super.header(context, headerName, headerValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#eoh(org.nightcode.milter.
	 * MilterContext)
	 */
	@Override
	public void eoh(MilterContext context) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: eoh                    : MilterContext context");
		log.info("----------------------------------------: ");

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);
		logContext(context, CommandProcessor.SMFIC_HEADER);
		logContext(context, CommandProcessor.SMFIC_EOH);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: eoh                    : MilterContext context");
		log.info("----------------------------------------: ");

		super.eoh(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#body(org.nightcode.milter.
	 * MilterContext, java.lang.String)
	 */
	@Override
	public void body(MilterContext context, String bodyChunk) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: body                   : MilterContext context, String bodyChunk");
		log.info("----------------------------------------: ");

		log.info("*bodyChunk <-- (Start at next line) --> : " + System.lineSeparator() + bodyChunk);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);
		logContext(context, CommandProcessor.SMFIC_HEADER);
		logContext(context, CommandProcessor.SMFIC_EOH);
		logContext(context, CommandProcessor.SMFIC_BODY);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: body                   : MilterContext context, String bodyChunk");
		log.info("----------------------------------------: ");

		super.body(context, bodyChunk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#eom(org.nightcode.milter.
	 * MilterContext, java.lang.String)
	 */
	@Override
	public void eom(MilterContext context, @Nullable String bodyChunk) throws MilterException {

		/*
		 * Here is the best place to modify anything.
		 */
		messageModificationService.addHeader(context, "X-Logged", "JMilter");

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: eom                    : MilterContext context, @Nullable String bodyChunk");
		log.info("----------------------------------------: ");

		log.info("*bodyChunk <-- (Start at next line) --> : " + System.lineSeparator() + bodyChunk);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);
		logContext(context, CommandProcessor.SMFIC_HEADER);
		logContext(context, CommandProcessor.SMFIC_EOH);
		logContext(context, CommandProcessor.SMFIC_BODY);
		logContext(context, CommandProcessor.SMFIC_BODYEOB);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: eom                    : MilterContext context, @Nullable String bodyChunk");
		log.info("----------------------------------------: ");

		super.eom(context, bodyChunk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#abort(org.nightcode.milter.
	 * MilterContext, org.nightcode.milter.net.MilterPacket)
	 */
	@Override
	public void abort(MilterContext context, MilterPacket packet) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: abort                  : MilterContext context, MilterPacket packet");
		log.info("----------------------------------------: ");

		log.info("*packet                                 : " + packet);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);
		logContext(context, CommandProcessor.SMFIC_HEADER);
		logContext(context, CommandProcessor.SMFIC_EOH);
		logContext(context, CommandProcessor.SMFIC_BODY);
		logContext(context, CommandProcessor.SMFIC_BODYEOB);
		logContext(context, CommandProcessor.SMFIC_ABORT);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: abort                  : MilterContext context, MilterPacket packet");
		log.info("----------------------------------------: ");

		super.abort(context, packet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nightcode.milter.AbstractMilterHandler#negotiate(org.nightcode.milter.
	 * MilterContext, int, org.nightcode.milter.util.Actions,
	 * org.nightcode.milter.util.ProtocolSteps)
	 */
	@Override
	public void negotiate(MilterContext context, int mtaProtocolVersion, Actions mtaActions,
			ProtocolSteps mtaProtocolSteps) throws MilterException {

		log.info("----------------------------------------: ");
		log.info(
				"JMilter - ENTRY: negotiate              : MilterContext context, int mtaProtocolVersion, Actions mtaActions, ProtocolSteps mtaProtocolSteps");
		log.info("----------------------------------------: ");

		log.info("*mtaProtocolVersion                     : " + mtaProtocolVersion);
		log.info("*mtaActions                             : " + mtaActions);
		log.info("*mtaProtocolSteps                       : " + mtaProtocolSteps);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);
		logContext(context, CommandProcessor.SMFIC_HEADER);
		logContext(context, CommandProcessor.SMFIC_EOH);
		logContext(context, CommandProcessor.SMFIC_BODY);
		logContext(context, CommandProcessor.SMFIC_BODYEOB);
		logContext(context, CommandProcessor.SMFIC_ABORT);
		logContext(context, CommandProcessor.SMFIC_OPTNEG);

		log.info("----------------------------------------: ");
		log.info(
				"JMilter - LEAVE: negotiate              : MilterContext context, int mtaProtocolVersion, Actions mtaActions, ProtocolSteps mtaProtocolSteps");
		log.info("----------------------------------------: ");

		super.negotiate(context, mtaProtocolVersion, mtaActions, mtaProtocolSteps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nightcode.milter.AbstractMilterHandler#unknown(org.nightcode.milter.
	 * MilterContext, byte[])
	 */
	@Override
	public void unknown(MilterContext context, byte[] payload) throws MilterException {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: unknown                : MilterContext context, byte[] payload");
		log.info("----------------------------------------: ");

		byte[] dataPayload = payload;
		String dataPayloadString = null;
		StringBuilder stringBuilder = new StringBuilder();

		if (dataPayload != null && dataPayload.length > 0) {
			for (byte b : dataPayload) {
				stringBuilder.append(String.format("%02x:", b));
			}
			dataPayloadString = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
		}

		log.info("*payload                                : " + dataPayloadString);

		logContext(context);
		logContext(context, CommandProcessor.SMFIC_CONNECT);
		logContext(context, CommandProcessor.SMFIC_HELO);
		logContext(context, CommandProcessor.SMFIC_MAIL);
		logContext(context, CommandProcessor.SMFIC_RCPT);
		logContext(context, CommandProcessor.SMFIC_DATA);
		logContext(context, CommandProcessor.SMFIC_HEADER);
		logContext(context, CommandProcessor.SMFIC_EOH);
		logContext(context, CommandProcessor.SMFIC_BODY);
		logContext(context, CommandProcessor.SMFIC_BODYEOB);
		logContext(context, CommandProcessor.SMFIC_ABORT);
		logContext(context, CommandProcessor.SMFIC_OPTNEG);
		logContext(context, CommandProcessor.SMFIC_UNKNOWN);

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: unknown                : MilterContext context, byte[] payload");
		log.info("----------------------------------------: ");

		super.unknown(context, payload);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nightcode.milter.MilterHandler#close(org.nightcode.milter.MilterContext)
	 */
	@Override
	public void close(MilterContext arg0) {

		log.info("----------------------------------------: ");
		log.info("JMilter - ENTRY: close                  : MilterContext arg0");
		log.info("----------------------------------------: ");

		log.info("----------------------------------------: ");
		log.info("JMilter - LEAVE: close                  : MilterContext arg0");
		log.info("----------------------------------------: ");
	}

	/**
	 * Log MilterContext (context) default (0) part.
	 * 
	 * @param context
	 */
	private void logContext(MilterContext context) {
		logContext(context, 0);
	}

	/**
	 * Log MilterContext (context) SMFIC or default (0) part.
	 * 
	 * @param context
	 * @param smfic
	 */
	private void logContext(MilterContext context, int smfic) {

		switch (smfic) {
		case CommandProcessor.SMFIC_CONNECT:
			if (context.getMacros(CommandProcessor.SMFIC_CONNECT) != null) {
				log.info("*context.getMacros(SMIFC_CONNECT)       : "
						+ context.getMacros(CommandProcessor.SMFIC_CONNECT));

				if (context.getMacros(CommandProcessor.SMFIC_CONNECT).containsKey("v")) {
					log.info("*context.getMacros(SMIFC_CONNECT)|(\"v\") : "
							+ context.getMacros(CommandProcessor.SMFIC_CONNECT).get("v").toString());
				}
				if (context.getMacros(CommandProcessor.SMFIC_CONNECT).containsKey("{daemon_name}")) {
					log.info("*context.getMacros(SMIFC_CONNECT)|(\"{...: "
							+ context.getMacros(CommandProcessor.SMFIC_CONNECT).get("{daemon_name}").toString());
				}
				if (context.getMacros(CommandProcessor.SMFIC_CONNECT).containsKey("j")) {
					log.info("*context.getMacros(SMIFC_CONNECT)|(\"j\") : "
							+ context.getMacros(CommandProcessor.SMFIC_CONNECT).get("j").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_HELO:
			if (context.getMacros(CommandProcessor.SMFIC_HELO) != null) {
				log.info("*context.getMacros(SMIFC_HELO)          : " + context.getMacros(CommandProcessor.SMFIC_HELO));
			}

			break;
		case CommandProcessor.SMFIC_MAIL:
			if (context.getMacros(CommandProcessor.SMFIC_MAIL) != null) {
				log.info("*context.getMacros(SMIFC_MAIL)          : " + context.getMacros(CommandProcessor.SMFIC_MAIL));

				if (context.getMacros(CommandProcessor.SMFIC_MAIL).containsKey("{mail_host}")) {
					log.info("*context.getMacros(SMIFC_MAIL)|(\"{mai...: "
							+ context.getMacros(CommandProcessor.SMFIC_MAIL).get("{mail_host}").toString());
				}
				if (context.getMacros(CommandProcessor.SMFIC_MAIL).containsKey("{mail_mailer}")) {
					log.info("*context.getMacros(SMIFC_MAIL)|(\"{mai...: "
							+ context.getMacros(CommandProcessor.SMFIC_MAIL).get("{mail_mailer}").toString());
				}
				if (context.getMacros(CommandProcessor.SMFIC_MAIL).containsKey("{mail_addr}")) {
					log.info("*context.getMacros(SMIFC_MAIL)|(\"{mai...: "
							+ context.getMacros(CommandProcessor.SMFIC_MAIL).get("{mail_addr}").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_RCPT:
			if (context.getMacros(CommandProcessor.SMFIC_RCPT) != null) {
				log.info("*context.getMacros(SMIFC_RCPT)          : " + context.getMacros(CommandProcessor.SMFIC_RCPT));

				if (context.getMacros(CommandProcessor.SMFIC_RCPT).containsKey("{rcpt_mailer}")) {
					log.info("*context.getMacros(SMIFC_RCPT)|(\"{rcp...: "
							+ context.getMacros(CommandProcessor.SMFIC_RCPT).get("{rcpt_mailer}").toString());
				}
				if (context.getMacros(CommandProcessor.SMFIC_RCPT).containsKey("{rcpt_addr}")) {
					log.info("*context.getMacros(SMIFC_RCPT)|(\"{rcp...: "
							+ context.getMacros(CommandProcessor.SMFIC_RCPT).get("{rcpt_addr}").toString());
				}
				if (context.getMacros(CommandProcessor.SMFIC_RCPT).containsKey("{rcpt_host}")) {
					log.info("*context.getMacros(SMIFC_RCPT)|(\"{rcp...: "
							+ context.getMacros(CommandProcessor.SMFIC_RCPT).get("{rcpt_host}").toString());
				}
				if (context.getMacros(CommandProcessor.SMFIC_RCPT).containsKey("i")) {
					log.info("*context.getMacros(SMIFC_RCPT)|(\"i\")    : "
							+ context.getMacros(CommandProcessor.SMFIC_RCPT).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_DATA:
			if (context.getMacros(CommandProcessor.SMFIC_DATA) != null) {
				log.info("*context.getMacros(SMIFC_DATA)          : " + context.getMacros(CommandProcessor.SMFIC_DATA));

				if (context.getMacros(CommandProcessor.SMFIC_DATA).containsKey("i")) {
					log.info("*context.getMacros(SMIFC_DATA)|(\"i\")    : "
							+ context.getMacros(CommandProcessor.SMFIC_DATA).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_HEADER:
			if (context.getMacros(CommandProcessor.SMFIC_HEADER) != null) {
				log.info("*context.getMacros(SMFIC_HEADER)        : "
						+ context.getMacros(CommandProcessor.SMFIC_HEADER));

				if (context.getMacros(CommandProcessor.SMFIC_HEADER).containsKey("i")) {
					log.info("*context.getMacros(SMIFC_HEADER)|(\"i\")  : "
							+ context.getMacros(CommandProcessor.SMFIC_HEADER).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_EOH:
			if (context.getMacros(CommandProcessor.SMFIC_EOH) != null) {
				log.info("*context.getMacros(SMFIC_EOH)           : " + context.getMacros(CommandProcessor.SMFIC_EOH));

				if (context.getMacros(CommandProcessor.SMFIC_EOH).containsKey("i")) {
					log.info("*context.getMacros(SMIFC_EOH)|(\"i\")     : "
							+ context.getMacros(CommandProcessor.SMFIC_EOH).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_BODY:
			if (context.getMacros(CommandProcessor.SMFIC_BODY) != null) {
				log.info("*context.getMacros(SMIFC_BODY)          : " + context.getMacros(CommandProcessor.SMFIC_BODY));

				if (context.getMacros(CommandProcessor.SMFIC_BODY).containsKey("i")) {
					log.info("*context.getMacros(SMIFC_BODY)|(\"i\")    : "
							+ context.getMacros(CommandProcessor.SMFIC_BODY).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_BODYEOB:
			if (context.getMacros(CommandProcessor.SMFIC_BODYEOB) != null) {
				log.info("*context.getMacros(SMIFC_BODYEOB)       : "
						+ context.getMacros(CommandProcessor.SMFIC_BODYEOB));

				if (context.getMacros(CommandProcessor.SMFIC_BODYEOB).containsKey("i")) {
					log.info("*context.getMacros(SMIFC_BODYEOB)|(\"i\") : "
							+ context.getMacros(CommandProcessor.SMFIC_BODYEOB).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_ABORT:
			if (context.getMacros(CommandProcessor.SMFIC_ABORT) != null) {
				log.info(
						"*context.getMacros(SMIFC_ABORT)         : " + context.getMacros(CommandProcessor.SMFIC_ABORT));

				if (context.getMacros(CommandProcessor.SMFIC_ABORT).containsKey("i")) {
					log.info("*context.getMacros(SMIFC_ABORT)|(\"i\")   : "
							+ context.getMacros(CommandProcessor.SMFIC_ABORT).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_OPTNEG:
			if (context.getMacros(CommandProcessor.SMFIC_OPTNEG) != null) {
				log.info("*context.getMacros(SMFIC_OPTNEG)        : "
						+ context.getMacros(CommandProcessor.SMFIC_OPTNEG));

				if (context.getMacros(CommandProcessor.SMFIC_OPTNEG).containsKey("i")) {
					log.info("*context.getMacros(SMFIC_OPTNEG)|(\"i\")  : "
							+ context.getMacros(CommandProcessor.SMFIC_OPTNEG).get("i").toString());
				}
			}

			break;
		case CommandProcessor.SMFIC_UNKNOWN:
			if (context.getMacros(CommandProcessor.SMFIC_UNKNOWN) != null) {
				log.info("*context.getMacros(SMFIC_UNKNOWN)       : "
						+ context.getMacros(CommandProcessor.SMFIC_UNKNOWN));
			}

			break;
		default:
			if (context != null) {
				log.info("*context.getMtaProtocolVersion()        : " + context.getMtaProtocolVersion());
				log.info("*context.getSessionProtocolVersion()    : " + context.getSessionProtocolVersion());
				log.info("*ontextt.milterProtocolVersion()        : " + context.milterProtocolVersion());
				log.info("*context.PROTOCOL_VERSION               : " + MilterContext.PROTOCOL_VERSION);
				log.info("*context.getMacros(ttl)                 : " + context.getMacros(ttl));
				log.info("*context.getMacros(timeout)             : " + context.getMacros(timeout));
				log.info("*context.getMtaActions()                : " + context.getMtaActions());
				log.info("*context.getMtaProtocolSteps()          : " + context.getMtaProtocolSteps());
				log.info("*context.getSessionProtocolSteps()      : " + context.getSessionProtocolSteps());
				log.info("*context.getSessionState()              : " + context.getSessionState());
				log.info("*context.id()                           : " + context.id());
				log.info("*context.milterActions()                : " + context.milterActions());
				log.info("*context.milterProtocolSteps()          : " + context.milterProtocolSteps());
			}

			break;
		}

	}

}
