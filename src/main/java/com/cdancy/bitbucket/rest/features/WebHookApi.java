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

package com.cdancy.bitbucket.rest.features;

import com.cdancy.bitbucket.rest.annotations.Documentation;
import com.cdancy.bitbucket.rest.domain.common.RequestStatus;
import com.cdancy.bitbucket.rest.domain.repository.WebHook;
import com.cdancy.bitbucket.rest.domain.repository.WebHookPage;
import com.cdancy.bitbucket.rest.fallbacks.BitbucketFallbacks;
import com.cdancy.bitbucket.rest.filters.BitbucketAuthenticationFilter;
import com.cdancy.bitbucket.rest.options.CreateWebHook;
import com.cdancy.bitbucket.rest.parsers.RequestStatusParser;
import com.google.inject.name.Named;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.binders.BindToJsonPayload;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@RequestFilters(BitbucketAuthenticationFilter.class)
@Path("/rest/api/{jclouds.api-version}/projects")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public interface WebHookApi {

    @Named("webhook:list-webhook")
    @Documentation({"https://docs.atlassian.com/bitbucket-server/rest/5.14.1/bitbucket-rest.html#idm45367160971744"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{project}/repos/{repo}/webhooks")
    @Fallback(BitbucketFallbacks.WebHookPageOnError.class)
    @GET
    WebHookPage list(@PathParam("project") String project,
                     @PathParam("repo") String repo,
                     @Nullable @QueryParam("start") Integer start,
                     @Nullable @QueryParam("limit") Integer limit);

    @Named("webhook:get-webhook")
    @Documentation({"https://docs.atlassian.com/bitbucket-server/rest/5.14.1/bitbucket-rest.html#idm45367160920544"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{project}/repos/{repo}/webhooks/{webHookKey}")
    @Fallback(BitbucketFallbacks.WebHookOnError.class)
    @GET
    WebHook get(@PathParam("project") String project,
                @PathParam("repo") String repo,
                @PathParam("webHookKey") String webHookKey);

    @Named("webhook:create-webhook")
    @Documentation({"https://docs.atlassian.com/bitbucket-server/rest/5.14.1/bitbucket-rest.html#idm45367160974656"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{project}/repos/{repo}/webhooks")
    @Fallback(BitbucketFallbacks.WebHookOnError.class)
    @POST
    WebHook create(@PathParam("project") String project,
                   @PathParam("repo") String repo,
                   @BinderParam(BindToJsonPayload.class) CreateWebHook webHook);

    @Named("webhook:update-webhook")
    @Documentation({"https://docs.atlassian.com/bitbucket-server/rest/5.14.1/bitbucket-rest.html#idm45367160908160"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{project}/repos/{repo}/webhooks/{webHookKey}")
    @Fallback(BitbucketFallbacks.WebHookOnError.class)
    @PUT
    WebHook update(@PathParam("project") String project,
                   @PathParam("repo") String repo,
                   @PathParam("webHookKey") String webHookKey,
                   @BinderParam(BindToJsonPayload.class) CreateWebHook webHook);

    @Named("webhook:delete-webhook")
    @Documentation({"https://docs.atlassian.com/bitbucket-server/rest/5.14.1/bitbucket-rest.html#idm45367160891616"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{project}/repos/{repo}/webhooks/{webHookKey}")
    @Fallback(BitbucketFallbacks.RequestStatusOnError.class)
    @ResponseParser(RequestStatusParser.class)
    @DELETE
    RequestStatus delete(@PathParam("project") String project,
                   @PathParam("repo") String repo,
                   @PathParam("webHookKey") String webHookKey);
}
