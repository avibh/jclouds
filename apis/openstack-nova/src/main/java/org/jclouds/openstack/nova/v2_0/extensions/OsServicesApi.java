/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.openstack.nova.v2_0.extensions;

import com.google.common.annotations.Beta;
import com.google.common.collect.FluentIterable;
import org.jclouds.Fallbacks;
import org.jclouds.openstack.keystone.v2_0.filters.AuthenticateRequest;
import org.jclouds.openstack.nova.v2_0.domain.OsService;
import org.jclouds.openstack.v2_0.ServiceType;
import org.jclouds.openstack.v2_0.services.Extension;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SelectJson;
import org.jclouds.rest.binders.BindToJsonPayload;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Provides access to OpenStack Compute (Nova) OS-Services extension API.
 */
@Beta
@Extension(of = ServiceType.COMPUTE, namespace = ExtensionNamespaces.OS_SERVICES)
@RequestFilters(AuthenticateRequest.class)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/os-services")
public interface OsServicesApi {


   /**
    * List all os-services (binary, host, zone...)
    *
    * @return all os-services (binary, host, zone...)
    */
   @Named("os-services:list")
   @GET
   @SelectJson("services")
   @Fallback(Fallbacks.EmptyFluentIterableOnNotFoundOr404.class)
   FluentIterable<? extends OsService> list();

   /**
    * enable an os-service
    * @param host - host to enable
    * @param binary - binary to enable
    * @return os-service that was enabled
    */
   @Named("os-services:list")
   @PUT
   @Path("/enable")
   @SelectJson("service")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   @Fallback(Fallbacks.NullOnNotFoundOr404.class)
   @MapBinder(BindToJsonPayload.class)
   OsService enable(@PayloadParam("host") String host, @PayloadParam("binary") String binary);

   /**
    * disable an os-service
    * @param host - host to disable
    * @param binary - binary to disable
    * @return os-service that was disabled
    */
   @PUT
   @Path("/disable")
   @SelectJson("service")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   @Fallback(Fallbacks.NullOnNotFoundOr404.class)
   @MapBinder(BindToJsonPayload.class)
   OsService disable(@PayloadParam("host") String host, @PayloadParam("binary") String binary);
}
