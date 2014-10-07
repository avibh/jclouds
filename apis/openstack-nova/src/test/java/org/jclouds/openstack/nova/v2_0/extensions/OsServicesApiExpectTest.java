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

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.jclouds.date.DateService;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.OsService;
import org.jclouds.openstack.nova.v2_0.internal.BaseNovaApiExpectTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collection;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Tests OsServicesApi guice wiring and parsing
 *
 * @author Adam Lowe
 */
@Test(groups = "unit", testName = "OsServicesApiExpectTest")
public class OsServicesApiExpectTest extends BaseNovaApiExpectTest {
   private DateService dateService = new SimpleDateFormatDateService();

   public void testWhenNamespaceInExtensionsListFloatingIpPresent() throws Exception {

      NovaApi apiWhenExtensionNotInList = requestsSendResponses(keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse);

      assertEquals(apiWhenExtensionNotInList.getConfiguredRegions(), ImmutableSet.of("az-1.region-a.geo-1", "az-2.region-a.geo-1", "az-3.region-a.geo-1"));

      assertTrue(apiWhenExtensionNotInList.getOsServicesApi("az-1.region-a.geo-1").isPresent());

   }

   public void testList() {
      URI endpoint = URI.create("https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v2/3456/os-services");
      OsServicesApi api = requestsSendResponses(keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess, extensionsOfNovaRequest, extensionsOfNovaResponse,
            authenticatedGET().endpoint(endpoint).build(),
            HttpResponse.builder().statusCode(200).payload(payloadFromResource("/os_services_list.json")).build())
            .getOsServicesApi("az-1.region-a.geo-1").get();

      OsService result = getOsService("host1", api.list().toList());
      assertEquals(result, exampleOsServices());
   }

   public OsService exampleOsServices() {
      return OsService.builder()
              .binary("nova-scheduler")
              .host("host1").state(OsService.State.UP)
              .status(OsService.Status.DISABLED)
              .updated(dateService.iso8601SecondsDateParse("2012-10-29T13:42:02"))
              .zone("internal")
              .build();
   }

   /**
    * return os-service by host
    */
   private OsService getOsService(final String host, Collection<? extends OsService> osServices) {
      OsService osService = Iterables.getFirst(Iterables.filter(osServices, new Predicate<Object>() {
         @Override
         public boolean apply(Object input) {
            OsService osService = (OsService) input;
            return host.equals(osService.getHost());
         }
      }), null);
   return osService;
   }
}
