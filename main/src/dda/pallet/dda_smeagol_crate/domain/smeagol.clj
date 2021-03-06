; Licensed to the Apache Software Foundation (ASF) under one
; or more contributor license agreements. See the NOTICE file
; distributed with this work for additional information
; regarding copyright ownership. The ASF licenses this file
; to you under the Apache License, Version 2.0 (the
; "License"); you may not use this file except in compliance
; with the License. You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

(ns dda.pallet.dda-smeagol-crate.domain.smeagol
  (:require
    [schema.core :as s]
    [dda.pallet.dda-smeagol-crate.domain.schema :as schema]))

(defn- resource-location-helper
  [smeagol-location]
  [{:name "war-location" :source (str smeagol-location "target/smeagol.war") :destination "/var/lib/tomcat8/webapps/smeagol.war"}
   {:name "passwd" :source (str smeagol-location "resources/passwd") :destination "/usr/local/etc/passwd"}
   {:name "config-edn" :source (str smeagol-location "resources/config.edn") :destination "/usr/local/etc/config.edn"}
   {:name "config-edn" :source (str smeagol-location "resources/public/content") :destination "/usr/local/etc/content"}])


(def environment-variables
  [{:name "SMEAGOL_CONFIG" :value "/usr/local/etc/config.edn"}
   {:name "SMEAGOL_CONTENT_DIR" :value "/usr/local/etc/content"}
   {:name "SMEAGOL_PASSWD" :value "/usr/local/etc/passwd"}
   {:name "TIMBRE_DEFAULT_STACKTRACE_FONTS" :value "{}"}
   {:name "TIMBRE_LEVEL" :value ":info"}
   {:name "PORT" :value "80"}])


(s/defn tomcat-domain-configuration
  [domain-config :- schema/SmeagolDomain]
  (let [{:keys [tomcat-xmx-megabyte] :or {tomcat-xmx-megabyte 2560}} domain-config]
    {:app-server
     {:xmx-megabyte tomcat-xmx-megabyte}}))            ; e.g. 6072 or 2560


(s/defn smeagol-infra-configuration
  [domain-config :- schema/SmeagolDomain
   facility :- s/Keyword]
  (let [smeagol-parent-dir "/var/lib/"
        smeagol-dir "smeagol-master/"]
    {facility
     {:smeagol-parent-dir smeagol-parent-dir
      :smeagol-dir smeagol-dir
      :repo-download-source "https://github.com/DomainDrivenArchitecture/smeagol/archive/master.zip"
      :resource-locations (resource-location-helper (str smeagol-parent-dir smeagol-dir))
      :environment-variables environment-variables}}))