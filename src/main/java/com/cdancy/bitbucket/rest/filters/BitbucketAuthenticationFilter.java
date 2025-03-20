/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cdancy.bitbucket.rest.filters;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.cdancy.bitbucket.rest.BitbucketAuthentication;
import com.cdancy.bitbucket.rest.auth.AuthenticationType;

import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import com.google.common.net.HttpHeaders;

@Singleton
public class BitbucketAuthenticationFilter implements HttpRequestFilter {
    private final BitbucketAuthentication creds;

    @Inject
    BitbucketAuthenticationFilter(final BitbucketAuthentication creds) {
        this.creds = creds;
    }

    @Override
    public HttpRequest filter(final HttpRequest request) throws HttpException {
        if (creds.authType() == AuthenticationType.Anonymous) {
            return request;
        } else {
            final String authHeader = creds.authType() + " " + creds.authValue();
            return request.toBuilder().addHeader(HttpHeaders.AUTHORIZATION, authHeader).build();
        }
    }
}
