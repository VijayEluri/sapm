/**
 * The Subversion Authentication Parse Module (SAPM for short).
 *
 * Copyright (c) 2010, 2011 by SoftwareEntwicklung Beratung Schulung (SoEBeS)
 * Copyright (c) 2010, 2011 by Karl Heinz Marbaise
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
 *
 * The License can viewed online under http://www.gnu.org/licenses/gpl.html
 * If you have any questions about the Software or about the license
 * just write an email to license@soebes.de
 */
package com.soebes.subversion.sapm;

import java.util.ArrayList;

/**
 * This class can handle an access rule which comprises of multiple {@link AccessRules}.
 *
 * <pre>
 * [/]
 * * = r
 *
 * [repository:/test/trunk]
 * harry = rw
 * michael = rw
 * </pre>
 *
 * @author Karl Heinz Marbaise
 *
 */
public class AccessRules {

    private ArrayList<AccessRule> accessRules;

    public AccessRules() {
        setAccessRules(new ArrayList<AccessRule>());
    }

    public void add(AccessRule accessRule) {
        getAccessRules().add(accessRule);
    }

    public ArrayList<AccessRule> getAccessRules() {
        return accessRules;
    }
    public void setAccessRules(ArrayList<AccessRule> accessRules) {
        this.accessRules = accessRules;
    }

    public AccessLevel getAccess(String user, String repository, String accessPath) {
        AccessLevel result = AccessLevel.NOTHING;
        for (AccessRule item : getAccessRules()) {
            AccessLevel itemResult = item.getAccess(user, repository, accessPath);
            if (!itemResult.equals(AccessLevel.NOTHING)) {
                result = itemResult;
            }
        }
        return result;
    }
}