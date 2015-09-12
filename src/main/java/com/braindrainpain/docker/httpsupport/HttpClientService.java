/*
The MIT License (MIT)

Copyright (c) 2014 Jan De Cooman

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.braindrainpain.docker.httpsupport;

import com.thoughtworks.go.plugin.api.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import java.io.IOException;

/**
 * @author Manuel Kasiske
 */
public class HttpClientService {

    private final Logger LOG = Logger.getLoggerFor(HttpClientService.class);
    private final HttpClient httpClient;
    private final GetMethod getMethod;

    public HttpClientService() {
        httpClient = new HttpClient();
        getMethod = new GetMethod();
    }

    protected HttpClientService(HttpClient httpClient, GetMethod getMethod) {
        this.httpClient = httpClient;
        this.getMethod = getMethod;
    }

    public String doGet(String url) throws IOException {
        URI uri = new URI(url, false);
        this.getMethod.setURI(uri);
        final int status = httpClient.executeMethod(getMethod);
        if (status != HttpStatus.SC_OK) {
            LOG.error("cannot connect to url " + url);
            throw new IOException("cannot connect to url " + url);
        }
        return getMethod.getResponseBodyAsString();
    }

    public void checkConnection(String url) {
        LOG.debug("Checking: '" + url + "'");
        try {
            URI uri = new URI(url, false);
            this.getMethod.setURI(uri);
            getMethod.setFollowRedirects(false);
            int status = httpClient.executeMethod(getMethod);
            if (status != HttpStatus.SC_OK) {
                LOG.error("Not ok from: '" + url + "'");
                LOG.error("status: "+status);
                throw new RuntimeException("Not ok from: '" + url + "'");
            }
        } catch (IOException e) {
            LOG.error("Error connecting to: '" + url + "'");
            throw new RuntimeException("Error connecting to: '" + url + "'");
        }
    }

}
