/*
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 *
 *  https://mozilla.org/MPL/2.0/.
 */
package org.mifos.connector.common.mojaloop.dto;

public class PersonalInfo {

    private ComplexName complexName;
    private String dateOfBirth;

    public ComplexName getComplexName() {
        return complexName;
    }

    public void setComplexName(ComplexName complexName) {
        this.complexName = complexName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
