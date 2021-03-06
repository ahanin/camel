/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.atmosphere.websocket;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.servlet.ServletEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 *
 */
@UriEndpoint(scheme = "atmosphere-websocket", syntax = "atmosphere-websocket:servicePath", consumerClass = WebsocketConsumer.class, label = "http,websocket")
public class WebsocketEndpoint extends ServletEndpoint {

    private WebSocketStore store;

    @UriPath
    private String servicePath;
    @UriParam(defaultValue = "false")
    private boolean sendToAll;
    @UriParam(defaultValue = "false")
    private boolean useStreaming;
    
    public WebsocketEndpoint(String endPointURI, WebsocketComponent component, URI httpUri, HttpClientParams params, HttpConnectionManager httpConnectionManager,
                             HttpClientConfigurer clientConfigurer) throws URISyntaxException {
        super(endPointURI, component, httpUri, params, httpConnectionManager, clientConfigurer);

        //TODO find a better way of assigning the store
        int idx = endPointURI.indexOf('?');
        String name = idx > -1 ? endPointURI.substring(0, idx) : endPointURI;

        this.servicePath = name;
        this.store = component.getWebSocketStore(servicePath);
    }
    
    @Override
    public Producer createProducer() throws Exception {
        return new WebsocketProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new WebsocketConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * @return the sendToAll
     */
    public boolean isSendToAll() {
        return sendToAll;
    }

    /**
     * @param sendToAll the sendToAll to set
     */
    public void setSendToAll(boolean sendToAll) {
        this.sendToAll = sendToAll;
    }
    
    /**
     * @return the useStreaming
     */
    public boolean isUseStreaming() {
        return useStreaming;
    }

    /**
     * @param useStreaming the useStreaming to set
     */
    public void setUseStreaming(boolean useStreaming) {
        this.useStreaming = useStreaming;
    }

    WebSocketStore getWebSocketStore() {
        return store;
    }
}
