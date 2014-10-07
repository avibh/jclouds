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
package org.jclouds.openstack.nova.v2_0.domain;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.jclouds.javax.annotation.Nullable;

import javax.inject.Named;
import java.beans.ConstructorProperties;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Os-Service can be manipulated using the os-services Extension to Nova (alias "OS-SERVICES")
 *
 * @see org.jclouds.openstack.nova.v2_0.extensions.OsServicesApi
 */
public class OsService {


   public static enum Status {
      ENABLED, DISABLED, UNRECOGNIZED;

      public String value() {
         return name().toLowerCase();
      }

      @Override
      public String toString() {
         return value();
      }

      public static Status fromValue(String state) {
         try {
            return valueOf(checkNotNull(state, "status").toUpperCase());
         } catch (IllegalArgumentException e) {
            return UNRECOGNIZED;
         }
      }
   }

   public static enum State {
      UP, DOWN, UNRECOGNIZED;

      public String value() {
         return name().toLowerCase();
      }

      @Override
      public String toString() {
         return value();
      }

      public static State fromValue(String state) {
         try {
            return valueOf(checkNotNull(state, "state").toUpperCase());
         } catch (IllegalArgumentException e) {
            return UNRECOGNIZED;
         }
      }
   }

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   public Builder<?> toBuilder() {
      return new ConcreteBuilder().fromOsService(this);
   }

   public static abstract class Builder<T extends Builder<T>> {

      protected abstract T self();

      protected String binary;
      protected String host;
      protected State state;
      protected Status status;
      protected Date updated;
      protected String zone;


      public T binary(String binary) {
         this.binary = binary;
         return self();
      }

      public T host(String host) {
         this.host = host;
         return self();
      }

      public T state(State state) {
         this.state = state;
         return self();
      }

      public T status(Status status) {
         this.status = status;
         return self();
      }

      public T updated(Date updated) {
         this.updated = updated;
         return self();
      }

      public T zone(String zone) {
         this.zone = zone;
         return self();
      }

      public OsService build() {
         return new OsService(binary, host, state, status, updated, zone);
      }

      public T fromOsService(OsService in) {
         return this
               .binary(in.getBinary())
               .host(in.getHost())
               .state(in.getState())
               .status(in.getStatus())
               .updated(in.getUpdated().get())
               .zone(in.getZone());
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
      @Override
      protected ConcreteBuilder self() {
         return this;
      }
   }


   protected String binary;
   protected String host;
   protected State state;
   protected Status status;
   @Named("updated_at")
   private final Optional<Date> updated;
   protected String zone;

   @ConstructorProperties({
         "binary", "host", "state", "status", "updated_at", "zone"
   })
   public OsService(String binary, String host, State state, Status status, @Nullable Date updated, String zone) {
      this.binary = Preconditions.checkNotNull(binary);
      this.host = Preconditions.checkNotNull(host);
      this.state = state;
      this.status = Preconditions.checkNotNull(status);
      this.updated = Optional.fromNullable(updated);
      this.zone = zone;
   }

   public String getBinary() {
      return binary;
   }

   public String getHost() {
      return host;
   }

   public State getState() {
      return state;
   }

   public Status getStatus() {
      return status;
   }

   public Optional<Date> getUpdated() {
      return updated;
   }

   public String getZone() {
      return zone;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      OsService osService = (OsService) o;

      if (binary != null ? !binary.equals(osService.binary) : osService.binary != null) return false;
      if (host != null ? !host.equals(osService.host) : osService.host != null) return false;
      if (state != null ? !state.equals(osService.state) : osService.state != null) return false;
      if (status != null ? !status.equals(osService.status) : osService.status != null) return false;
      if (updated != null ? !updated.equals(osService.updated) : osService.updated != null) return false;
      if (zone != null ? !zone.equals(osService.zone) : osService.zone != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = binary != null ? binary.hashCode() : 0;
      result = 31 * result + (host != null ? host.hashCode() : 0);
      result = 31 * result + (state != null ? state.hashCode() : 0);
      result = 31 * result + (status != null ? status.hashCode() : 0);
      result = 31 * result + (updated != null ? updated.hashCode() : 0);
      result = 31 * result + (zone != null ? zone.hashCode() : 0);
      return result;
   }

   protected Objects.ToStringHelper string() {
      return Objects.toStringHelper(this)
            .add("binary", binary).add("state", state).add("status", status).add("updated", updated).add("zone", zone);
   }

   @Override
   public String toString() {
      return string().toString();
   }

}