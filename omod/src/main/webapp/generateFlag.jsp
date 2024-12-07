<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ taglib prefix="springform" uri="resources/spring-form.tld"%>

<openmrs:require privilege="Manage Flags" otherwise="/login.htm"
                 redirect="/module/patientflags/generateFlag.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<script type="text/javascript"
        src="${pageContext.request.contextPath}/scripts/jquery/jquery-1.3.2.min.js"></script>

<script type="text/javascript"><!--

	// function that sets the value of the custom evaluator radiobutton to the value of the custom evaluator text box
	function updateCustomEvaluatorValue(){
		var className = $('#customEvaluatorTextbox').attr('value');
		$('#customEvaluator').attr('value', className);
	}

	$(document).ready(function(){
		// 'check' the custom evaluator radio button if the custom evaluator text box has text upon loading
		if(!($('#customEvaluatorTextbox').attr('value') == "")){
			updateCustomEvaluatorValue();
			$('#customEvaluator').attr('checked', true);
		}else{
			$('#customEvaluatorBlock').hide();
		}

		// event handlers to hide and show custom evaluator text box
		$('.evaluator').click(function disableCustomEvaluator(){
			$('#customEvaluatorBlock').hide();
		});
		$('#customEvaluator').click(function (){
			$('#customEvaluatorBlock').show();
		});

		// event handler to update custom evaluator value on change to value of custom evaluator text box
		$('#customEvaluatorTextbox').change(updateCustomEvaluatorValue);
	});
-->

 function updateFlagCriteriaOptions() {
        // Get the selected value of the flagType dropdown
        const flagType = document.getElementById('flagType').value;
        const flagCriteriaDropdown = document.getElementById('flagCritiria');

        // Clear existing options
        flagCriteriaDropdown.innerHTML = '';

        // Define options based on flagType
        let options = [];
        if (flagType === 'Allergy') {
            options = ['Drug', 'Food', 'Other'];
        } else if (flagType === 'Visits') {
            options = ['Blood Pressure', 'Pulse', 'Temperature', 'Weight', 'Height'];
        }

        // Add new options to the flagCritiria dropdown
        if (options.length > 0) {
            options.forEach(option => {
                const opt = document.createElement('option');
                opt.value = option;
                opt.text = option;
                flagCriteriaDropdown.add(opt);
            });
        } else {
            // Add a default "Select Flag Criteria" option if no flagType is selected
            const defaultOption = document.createElement('option');
            defaultOption.value = '';
            defaultOption.text = 'Select Flag Criteria';
            flagCriteriaDropdown.add(defaultOption);
        }
    }

<!-- JavaScript to Show/Hide Flag Logic -->
     function updateFlagLogicVisibility() {
        // Get the selected value of the flagType dropdown
        const flagType = document.getElementById('flagType').value;
        const flagLogicRow = document.getElementById('flagLogicRow');

        // Show the flagLogic dropdown only if flagType is 'Visits'
        if (flagType === 'Visits') {
            flagLogicRow.style.display = '';
        } else {
            flagLogicRow.style.display = 'none';
        }
    }

</script>

<h2><spring:message code="patientflags.admin.title" /></h2>

<div><b class="boxHeader"><spring:message
        code="patientflags.generateFlag" /></b>
    <div class="box"><springform:form modelAttribute="flag">
        <springform:errors path="evaluator" cssClass="error" />
        <br />
        <table>
            <tr>
                <td align="right"><spring:message code="patientflags.name" />:</td>
                <td><springform:input path="name" size="50" /><springform:errors
                        path="name" cssClass="error" /></td>
            </tr>

            <tr>
                <td align="right"><spring:message code="patientflags.flagType" />:</td>
                <td>
                    <springform:select path="flagType" id="flagType" onchange="updateFlagCriteriaOptions()">
                        <springform:option value="" label="Select Flag Type" />
                        <springform:option value="Allergy" label="Allergy" />
                        <springform:option value="Visits" label="Visits" />
                    </springform:select>
                    <springform:errors path="flagType" cssClass="error" />
                </td>
            </tr>

            <tr>
                <td align="right"><spring:message code="patientflags.flagCritiria" />:</td>
                <td>
                    <springform:select path="flagCritiria" id="flagCritiria">
                        <!-- Options will be populated dynamically -->
                    </springform:select>
                    <springform:errors path="flagCritiria" cssClass="error" />
                </td>
            </tr>

            <tr id="flagLogicRow" style="display: none;" >
                <td align="right"><spring:message code="patientflags.flagLogic" />:</td>
                <td>
                    <springform:select path="flagLogic" id="flagLogic" onchange="updateFlagLogicVisibility()">
                        <springform:option value="=" label="equals" />
                        <springform:option value=">" label="greater than" />
                        <springform:option value="<" label="lower than" />
                        <springform:option value="!=" label="not equals" />
                    </springform:select>
                    <springform:errors path="flagLogic" cssClass="error" />
                </td>
            </tr>

            <tr>
                <td align="right"><spring:message code="patientflags.flagValue" />:</td>
                <td><springform:input path="flagHighValue" size="50" /><springform:errors
                        path="flagHighValue" cssClass="error" /></td>
            </tr>

    <tr>
        <td align="right"><spring:message code="patientflags.message" />:</td>
        <td><springform:input path="message" size="50" /><springform:errors
                path="message" cssClass="error" /></td>
    </tr>
    <tr>
        <td align="right" valign="top"><spring:message
                code="patientflags.priority" />:</td>
        <td><springform:select path="priority">
            <option value="">&nbsp;</option>
            <springform:options items="${priorities}" itemValue="priorityId" itemLabel="name" />
        </springform:select></td>
    </tr>
    <tr>
        <td align="right" valign="top"><spring:message
                code="patientflags.editFlag.associatedTags" />:</td>
        <td><springform:select path="tags" multiple="true">
            <springform:options items="${tags}" itemValue="tagId" itemLabel="name" />
        </springform:select></td>
    </tr>
    <tr>
        <td align="right"><spring:message
                code="patientflags.editFlag.enabled" />:</td>
        <td><springform:checkbox path="enabled" /></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><input type="submit"
                   value="<spring:message code='patientflags.editFlag.saveFlag'/>" />
            </springform:form>
            <a href="${pageContext.request.contextPath}/module/patientflags/manageFlags.form">
                <input type="button" value="<spring:message code='patientflags.cancel'/>" /></a></td>
    </tr>
    </table></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>