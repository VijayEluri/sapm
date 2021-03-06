/**
 * The Subversion Authentication Parse Module (SAPM for short).
 *
 * Copyright (c) 2010, 2011 by SoftwareEntwicklung Beratung Schulung (SoEBeS)
 * Copyright (c) 2010, 2011 by Karl Heinz Marbaise
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soebes.subversion.sapm;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This test will create an AccessRule as follows:
 *
 * <pre>
 * [/test/trunk]
 * harry = rw
 * brian = r
 * michael = rw
 * </pre>
 *
 * The tests will check if the AccessLevel will be extracted correctly and
 * furthermore if the access to different directories inside this repository
 * will work well.
 *
 * @author Karl Heinz Marbaise
 *
 */
public class AccessRuleUserAnyRepositoryTest {

    private AccessRule accessRule;

    @BeforeMethod
    public void beforeMethod() {
        User userHarry = UserFactory.createInstance("harry");
        User userBrian = UserFactory.createInstance("brian");
        User userMicheal = UserFactory.createInstance("michael");

        accessRule = new AccessRule("/test/trunk");

        accessRule.add(userHarry, AccessLevel.READ_WRITE);
        accessRule.add(userBrian, AccessLevel.READ);
        accessRule.add(userMicheal, AccessLevel.READ_WRITE);

    }

    @Test
    public void accessTest() {
        AccessLevel al_harry = accessRule.getAccessForPrincipal("harry");
        Assert.assertEquals(AccessLevel.READ_WRITE, al_harry);

        AccessLevel al_brian = accessRule.getAccessForPrincipal("brian");
        Assert.assertEquals(AccessLevel.READ, al_brian);

        AccessLevel al_hugo = accessRule.getAccessForPrincipal("hugo");
        Assert.assertEquals(AccessLevel.NOTHING, al_hugo);

        AccessLevel al_micheal = accessRule.getAccessForPrincipal("michael");
        Assert.assertEquals(AccessLevel.READ_WRITE, al_micheal);
    }

    @DataProvider(name = "createAccessSet")
    public Object[][] createAccessSet() {
        return new Object[][] {
            { "harry", "repository", "/",                           AccessLevel.NOTHING },
            { "harry", "repository", "/test/trunk/",                AccessLevel.READ_WRITE },
            { "harry", "repository", "/test/trunk/src/",            AccessLevel.READ_WRITE },
            { "harry", "repository", "/test/trunk/src/xyz.java",    AccessLevel.READ_WRITE },
            { "harry", "repository", "/test/trunk/src/CHANGELOG",   AccessLevel.READ_WRITE },
            { "brian", "repository", "/test/trunk/src/xyz.java",    AccessLevel.READ },
            { "brian", "repository", "/test/trunk/src/CHANGELOG",   AccessLevel.READ },
            { "sally", "repository", "/test/trunk/src/xyz.java",    AccessLevel.NOTHING },
            { "brian", "repository", "/test/trunk/",                AccessLevel.READ },
            { "sally", "repository", "/test/trunk/",                AccessLevel.NOTHING },
            { "harry", "different", "/test/trunk/",                 AccessLevel.READ_WRITE },
        };
    }

    @Test(dataProvider = "createAccessSet")
    public void accessRuleCheck(String user, String repository,
            String accessPath, AccessLevel expectedLevel) {
        // Check the permission of the current user in the repository within the
        // given accessPath
        AccessLevel al_user = accessRule
        .getAccess(user, repository, accessPath);
        Assert.assertEquals(expectedLevel, al_user);
    }

}
