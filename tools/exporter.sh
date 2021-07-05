#!/bin/bash
# Invoke an export tool, that will encrypt/decrypt a file using the same
# algorithms as SyncYourSecret
#
#
#    Copyright 2009 Jan Petranek
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Prerequisites:
# The following jar files must be in the same folder:
# - syncyoursecrets-xmlbase.jar
# - bcprov-jdk15-140.jar
# - log4j-1.2.15.jar
# - jasypt-cli-bundle.jar
# And of course, you need Java > 1.5
# Optionally, you may want the international JCE policy.

java -cp syncyoursecrets-xmlbase.jar:bcprov-jdk15-140.jar:log4j-1.2.15.jar:jasypt-cli-bundle.jar  de.petranek.syncyoursecrets.exporter.Exporter $@

