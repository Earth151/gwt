package com.server;

import com.client.GreetingService;
import com.shared.Util;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


//TODO RENAME

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    public String greetServer(String input) throws IllegalArgumentException {
        // Verify that the input is valid.
        if (!Util.isAmountValid(Integer.parseInt(input))) {
            // If the input is not valid, throw an IllegalArgumentException back to the client.
            throw new IllegalArgumentException("Amount invalid, must be between 2 and 50");
        }

        String serverInfo = getServletContext().getServerInfo();
        System.out.println("ServerINFO: " + serverInfo);
        String userAgent = getThreadLocalRequest().getHeader("User-Agent");

        // Escape data from the client to avoid cross-site script vulnerabilities.
        input = escapeHtml(input);
        userAgent = escapeHtml(userAgent);

        return "Hello, " + input + "!<br><br>I am running " + serverInfo
                + ".<br><br>It looks like you are using:<br>" + userAgent;
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     *
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
                ">", "&gt;");
    }
}
