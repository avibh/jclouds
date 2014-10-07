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

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.jclouds.openstack.nova.v2_0.domain.OsService;
import org.jclouds.openstack.nova.v2_0.internal.BaseNovaApiLiveTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Tests behavior of AggregateApi
 *
 * @author Adam Lowe
 */
@Test(groups = "live", testName = "OsServicesApiLiveTest", singleThreaded = true)
public class OsServicesApiLiveTest extends BaseNovaApiLiveTest {

   public static final String INTERNAL = "internal";

   @Test
   public void testList() {
      for (String regionId : api.getConfiguredZones()) {
         Optional<? extends OsServicesApi> apiOption = api.getOsServicesApi(regionId);
         assertTrue(apiOption.isPresent());
         OsServicesApi api = apiOption.get();
         Set<? extends OsService> osServices = api.list().toSet();

         assertNotNull(osServices);
         for (OsService osService : osServices) {
            assertNotNull(osService.getBinary());
            assertNotNull(osService.getHost());
            assertNotNull(osService.getState());
            assertNotNull(osService.getZone());
            assertNotNull(osService.getStatus());
            assertNotNull(osService.getUpdated());
         }
      }
   }

    @Test
    public void testEnableDisable() {
       for (String regionId : api.getConfiguredZones()) {
          Optional<? extends OsServicesApi> apiOption = api.getOsServicesApi(regionId);
          assertTrue(apiOption.isPresent());
          OsServicesApi api = apiOption.get();
          List<? extends OsService> osServices = api.list().toList();
          assertNotNull(osServices);
          OsService osService = Iterables.getFirst(Iterables.filter(osServices, new Predicate<Object>() {
             @Override
             public boolean apply(Object input) {
                OsService osService = (OsService) input;
                return OsService.Status.ENABLED.equals(osService.getStatus()) && !INTERNAL.equals(osService.getZone());
             }
          }), null);

          try {
             OsService osServiceDisabled = api.disable(osService.getHost(), osService.getBinary());
             assertEquals(OsService.Status.DISABLED, osServiceDisabled.getStatus());
          }
          finally {
             OsService osServiceEnabled = api.enable(osService.getHost(), osService.getBinary());
             assertEquals(OsService.Status.ENABLED, osServiceEnabled.getStatus());
          }
       }
    }
}
