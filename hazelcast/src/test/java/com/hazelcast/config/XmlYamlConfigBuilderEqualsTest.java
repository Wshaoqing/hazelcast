/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.config;

import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelTest.class})
public class XmlYamlConfigBuilderEqualsTest {
    @Test
    public void testFullConfig() {
        Config xmlConfig = new ClasspathXmlConfig("hazelcast-fullconfig.xml");
        Config yamlConfig = new ClasspathYamlConfig("hazelcast-fullconfig.yaml");

        sortClientPermissionConfigs(xmlConfig);
        sortClientPermissionConfigs(yamlConfig);

        String xmlConfigFromXml = new ConfigXmlGenerator(true).generate(xmlConfig);
        String xmlConfigFromYaml = new ConfigXmlGenerator(true).generate(yamlConfig);

        assertEquals(xmlConfigFromXml, xmlConfigFromYaml);

    }

    @Test
    public void testDefaultConfig() {
        Config xmlConfig = new ClasspathXmlConfig("hazelcast-default.xml");
        Config yamlConfig = new ClasspathYamlConfig("hazelcast-default.yaml");

        sortClientPermissionConfigs(xmlConfig);
        sortClientPermissionConfigs(yamlConfig);

        String xmlConfigFromXml = new ConfigXmlGenerator(true).generate(xmlConfig);
        String xmlConfigFromYaml = new ConfigXmlGenerator(true).generate(yamlConfig);

        assertEquals(xmlConfigFromXml, xmlConfigFromYaml);

    }

    private void sortClientPermissionConfigs(Config config) {
        SecurityConfig securityConfig = config.getSecurityConfig();
        Set<PermissionConfig> unsorted = securityConfig.getClientPermissionConfigs();
        Set<PermissionConfig> sorted = new TreeSet<PermissionConfig>(new PermissionConfigComparator());
        sorted.addAll(unsorted);
        securityConfig.setClientPermissionConfigs(sorted);

    }

    private static class PermissionConfigComparator implements Comparator<PermissionConfig> {
        @Override
        public int compare(PermissionConfig o1, PermissionConfig o2) {
            if (o1 == o2) {
                return 0;
            }

            if (o1 == null) {
                return -1;
            }

            if (o2 == null) {
                return 1;
            }

            return o1.getType().name().compareTo(o2.getType().name());
        }
    }

}
