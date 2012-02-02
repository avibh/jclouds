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
package org.jclouds.location.config;

import static org.jclouds.Constants.PROPERTY_SESSION_INTERVAL;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.collect.Memoized;
import org.jclouds.domain.Location;
import org.jclouds.location.Iso3166;
import org.jclouds.location.Provider;
import org.jclouds.location.Region;
import org.jclouds.location.Zone;
import org.jclouds.location.suppliers.ImplicitLocationSupplier;
import org.jclouds.location.suppliers.ImplicitRegionIdSupplier;
import org.jclouds.location.suppliers.LocationIdToIso3166CodesSupplier;
import org.jclouds.location.suppliers.LocationsSupplier;
import org.jclouds.location.suppliers.ProviderURISupplier;
import org.jclouds.location.suppliers.RegionIdToURISupplier;
import org.jclouds.location.suppliers.RegionIdToZoneIdsSupplier;
import org.jclouds.location.suppliers.RegionIdsSupplier;
import org.jclouds.location.suppliers.ZoneIdToURISupplier;
import org.jclouds.location.suppliers.ZoneIdsSupplier;
import org.jclouds.rest.AuthorizationException;
import org.jclouds.rest.suppliers.MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier;

import com.google.common.base.Supplier;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * All of these are memoized as locations do not change often at runtime. Note that we take care to
 * propagate authorization exceptions. this is so that we do not lock out the account.
 * 
 * @author Adrian Cole
 */
public class LocationModule extends AbstractModule {

   @Override
   protected void configure() {
   }

   @Provides
   @Singleton
   @Iso3166
   protected Supplier<Map<String, Supplier<Set<String>>>> isoCodesSupplier(
            AtomicReference<AuthorizationException> authException, @Named(PROPERTY_SESSION_INTERVAL) long seconds,
            LocationIdToIso3166CodesSupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   @Provider
   protected Supplier<URI> provideProvider(AtomicReference<AuthorizationException> authException,
            @Named(PROPERTY_SESSION_INTERVAL) long seconds, ProviderURISupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   protected Supplier<Location> implicitLocationSupplier(AtomicReference<AuthorizationException> authException,
            @Named(PROPERTY_SESSION_INTERVAL) long seconds, ImplicitLocationSupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   // TODO: we should eventually get rid of memoized as an annotation, as it is confusing
   @Memoized
   protected Supplier<Set<? extends Location>> memoizedLocationsSupplier(
            AtomicReference<AuthorizationException> authException, @Named(PROPERTY_SESSION_INTERVAL) long seconds,
            LocationsSupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   @Region
   protected Supplier<Set<String>> regionIdsSupplier(AtomicReference<AuthorizationException> authException,
            @Named(PROPERTY_SESSION_INTERVAL) long seconds, RegionIdsSupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   @Region
   protected Supplier<Map<String, Supplier<URI>>> regionIdToURISupplier(
            AtomicReference<AuthorizationException> authException, @Named(PROPERTY_SESSION_INTERVAL) long seconds,
            RegionIdToURISupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   @Region
   protected Supplier<String> implicitRegionIdSupplier(AtomicReference<AuthorizationException> authException,
            @Named(PROPERTY_SESSION_INTERVAL) long seconds, ImplicitRegionIdSupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   @Zone
   protected Supplier<Set<String>> regionIdsSupplier(
            AtomicReference<AuthorizationException> authException, @Named(PROPERTY_SESSION_INTERVAL) long seconds,
            ZoneIdsSupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }
   
   @Provides
   @Singleton
   @Zone
   protected Supplier<Map<String, Supplier<Set<String>>>> regionIdToZoneIdsSupplier(
            AtomicReference<AuthorizationException> authException, @Named(PROPERTY_SESSION_INTERVAL) long seconds,
            RegionIdToZoneIdsSupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }

   @Provides
   @Singleton
   @Zone
   protected Supplier<Map<String, Supplier<URI>>> zoneIdToURISupplier(
            AtomicReference<AuthorizationException> authException, @Named(PROPERTY_SESSION_INTERVAL) long seconds,
            ZoneIdToURISupplier uncached) {
      return MemoizedRetryOnTimeOutButNotOnAuthorizationExceptionSupplier.create(authException, seconds, uncached);
   }
}