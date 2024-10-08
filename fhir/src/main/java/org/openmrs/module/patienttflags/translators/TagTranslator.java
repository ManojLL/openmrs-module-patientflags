/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patienttflags.translators;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.openmrs.module.fhir2.api.translators.ToFhirTranslator;
import org.openmrs.module.patientflags.Tag;

import javax.annotation.Nonnull;

public interface TagTranslator extends ToFhirTranslator<Tag, CodeableConcept> {

    /**
     * Maps an OpenMRS data element to a FHIR resource
     *
     * @param tag the OpenMRS data element to translate
     * @return the corresponding FHIR resource
     */
    @Override
    CodeableConcept toFhirResource(@Nonnull Tag tag);

}
