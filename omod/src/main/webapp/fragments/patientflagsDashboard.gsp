<openmrs:require privilege="View Patients" otherwise="/login.htm" redirect="/index.htm"/>

<div class="info-section">
    <div class="info-header">
        <i class="icon-flag"></i>

        <h3>${ui.message("patientflags.patientOverview.title")}</h3>
    </div>

    <div class="info-body">
        <script>
            jq.get('${ ui.actionLink("processPatientFlags") }', {
                patientId: ${patientId}
            }, function (response) {
                if (!response) {
                    ${ui.message("coreapps.none")};
                } else {
                    try {
                         if (typeof response === "string") {
                            jq("#flags").html(response.replace("{patientflags=", "").replace("}", ""));
                        } else if (typeof response === "object") {
                            jq("#flags").html(response.patientflags);
                        } else {
                            jq("#flags").html("No patient flags found in response");
                        }
                    } catch (error) {
                        console.error("Error processing flags response: ", error);
                        jq("#flags").html("Error processing response");
                    }
                }
            });
        </script>
        <ul id="flags">
            <i class="icon-spinner"></i>
        </ul>
    </div>
</div>