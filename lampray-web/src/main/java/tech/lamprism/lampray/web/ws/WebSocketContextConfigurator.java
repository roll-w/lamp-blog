/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.lamprism.lampray.web.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import space.lingu.NonNull;
import tech.lamprism.lampray.web.common.ApiContext;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author RollW
 */
public class WebSocketContextConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketContextConfigurator.class);

    private static ContextThreadAware<ApiContext> apiContextThreadAware;

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
        ContextThread<ApiContext> apiContextThread =
                apiContextThreadAware.getContextThread();
        sec.getUserProperties().put(ApiContext.class.getName(),
                apiContextThread.getContext());
    }

    private static volatile WebApplicationContext webApplicationContext;


    private static final String NO_VALUE = ObjectUtils.identityToString(new Object());

    private static final Map<String, Map<Class<?>, String>> cache =
            new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        WebApplicationContext wac = webApplicationContext;
        // WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        if (wac == null) {
            String message = "Failed to find the root WebApplicationContext. Was ContextLoaderListener not used?";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        String beanName = ClassUtils.getShortNameAsProperty(endpointClass);
        if (wac.containsBean(beanName)) {
            T endpoint = wac.getBean(beanName, endpointClass);
            if (logger.isTraceEnabled()) {
                logger.trace("Using @ServerEndpoint singleton {} by bean name {}.", endpoint, beanName);
            }
            return endpoint;
        }

        Component ann = AnnotationUtils.findAnnotation(endpointClass, Component.class);
        if (ann != null && wac.containsBean(ann.value())) {
            T endpoint = wac.getBean(ann.value(), endpointClass);
            if (logger.isTraceEnabled()) {
                logger.trace("Using @ServerEndpoint singleton {} by bean name @Component({}).", endpoint, ann.value());
            }
            return endpoint;
        }

        beanName = getBeanNameByType(wac, endpointClass);
        if (beanName != null) {
            return (T) wac.getBean(beanName);
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Creating new @ServerEndpoint instance of type " + endpointClass);
        }
        return wac.getAutowireCapableBeanFactory().createBean(endpointClass);
    }

    @Nullable
    private String getBeanNameByType(WebApplicationContext wac, Class<?> endpointClass) {
        String wacId = wac.getId();

        Map<Class<?>, String> beanNamesByType = cache.get(wacId);
        if (beanNamesByType == null) {
            beanNamesByType = new ConcurrentHashMap<>();
            cache.put(wacId, beanNamesByType);
        }

        if (!beanNamesByType.containsKey(endpointClass)) {
            String[] names = wac.getBeanNamesForType(endpointClass);
            if (names.length == 1) {
                beanNamesByType.put(endpointClass, names[0]);
            } else {
                beanNamesByType.put(endpointClass, NO_VALUE);
                if (names.length > 1) {
                    throw new IllegalStateException("Found multiple @ServerEndpoint's of type [" +
                            endpointClass.getName() + "]: bean names " + Arrays.toString(names));
                }
            }
        }

        String beanName = beanNamesByType.get(endpointClass);
        return (NO_VALUE.equals(beanName) ? null : beanName);
    }

    @Autowired
    public void setApiContextThreadAware(ContextThreadAware<ApiContext> apiContextThreadAware) {
        WebSocketContextConfigurator.apiContextThreadAware = apiContextThreadAware;
    }

    @Autowired
    public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
        WebSocketContextConfigurator.webApplicationContext = webApplicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
    }
}
