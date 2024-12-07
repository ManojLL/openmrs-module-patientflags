package org.openmrs.module.patientflags.web;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientflags.Flag;
import org.openmrs.module.patientflags.PatientFlagsConstants;
import org.openmrs.module.patientflags.Priority;
import org.openmrs.module.patientflags.Tag;
import org.openmrs.module.patientflags.api.FlagService;
import org.openmrs.module.patientflags.web.validators.FlagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/module/patientflags/generateFlag.form")
@SessionAttributes("flag")
public class GenerateFlagController {

    /**
     * Controller that handles adding and editing Patient Flags via the webapp Called by http request
     * ".../module/patientflags/editFlag.form"
     */

        //private Log log = LogFactory.getLog(this.getClass());

        /** Validator for this controller */
        private FlagValidator validator;

        /**
         * Generic Constructor
         */
        public GenerateFlagController() {
        }

        /**
         * Constructor that registers validator
         *
         * @param validator
         */
        @Autowired
        public GenerateFlagController(FlagValidator validator) {
            this.validator = validator;
        }

        @InitBinder
        public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
            // map the tag id to the actual tag
            binder.registerCustomEditor(Set.class, "tags", new CustomCollectionEditor(
                    Set.class) {

                @Override
                public Object convertElement(Object element) {
                    return Context.getService(FlagService.class).getTag(Integer.valueOf((String) element));
                }
            });

            // map the priority id to the actual priority
            binder.registerCustomEditor(Priority.class, new PropertyEditorSupport() {

                public void setAsText(String priority) {
                    if (StringUtils.isBlank(priority)) {
                        setValue(null);
                    }
                    else {
                        setValue(Context.getService(FlagService.class).getPriority(Integer.valueOf((String) priority)));
                    }
                }

            });

            // map the evaluator strings to the full evaluator classes
            binder.registerCustomEditor(String.class, "evaluator", new PropertyEditorSupport(){
                public void setAsText(String evaluator) {
                    // check to see if the class is in the evaluator map, if so, set based on the map
                    if (PatientFlagsConstants.FLAG_EVALUATOR_MAP.containsKey(evaluator)){
                        setValue(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get(evaluator));
                    }
                    else{
                        // otherwise is must be a custom evaluator; set it directly
                        setValue(evaluator);
                    }
                }
            });
        }

        @ModelAttribute("tags")
        public List<Tag> populateTags() {
            return Context.getService(FlagService.class).getAllTags();
        }

        @ModelAttribute("priorities")
        public List<Priority> populatePriorities(){
            return Context.getService(FlagService.class).getAllPriorities();
        }

        /**
         * Displays the form to edit (or add) a Flag
         *
         * @param request
         * @param model
         * @return new ModelAndView
         * @throws ServletRequestBindingException
         */
        @RequestMapping(method = RequestMethod.GET)
        public ModelAndView showForm(HttpServletRequest request, ModelMap model) throws ServletRequestBindingException {
            Flag flag;
            Integer flagId = ServletRequestUtils.getIntParameter(request, "flagId");

            if (flagId != null) {
                flag = Context.getService(FlagService.class).getFlag(flagId);

                // we need to manually set the customEvaluatorTextbox if this is a custom evaluator
                if(!PatientFlagsConstants.FLAG_EVALUATOR_MAP.containsValue(flag.getEvaluator())){
                    model.addAttribute("customEvaluator", flag.getEvaluator());
                }
            }else {
                flag = new Flag();
            }

            model.addAttribute("flag", flag);
            return new ModelAndView("/module/patientflags/generateFlag", model);
        }

        /**
         * Processes the form to edit (or add) a Flag
         *
         * @param flag the flag to add/update
         * @param result
         * @param status
         * @return new ModelAndView
         */
        @RequestMapping(method = RequestMethod.POST)
        public ModelAndView processSubmit(@ModelAttribute("flag") Flag flag, BindingResult result, SessionStatus status) {
            flag.setEvaluator("org.openmrs.module.patientflags.evaluator.SQLFlagEvaluator");
            // validate form entries
//

            if(flag.getFlagType().equals("Allergy")) {
                if(flag.getFlagCritiria().equals("drug")){
                    flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"DRUG\" AND a.voided = 0;");
                }else if(flag.getFlagCritiria().equals("food")){
                    flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"FOOD\" AND a.voided = 0;");
                }else{
                    flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"OTHER\" AND a.voided = 0;");
                }
            } else if(flag.getFlagType().equals("Visits")){
                switch (flag.getFlagCritiria()){
                    case "Blood Pressure":
                        flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"DRUG\" AND a.voided = 0;");
                    case "Pulse":
                        flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"DRUG\" AND a.voided = 0;");
                    case "Temperature":
                        flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"DRUG\" AND a.voided = 0;");
                    case "Weight":
                        flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"DRUG\" AND a.voided = 0;");
                    case "Height":
                        flag.setCriteria("SELECT a.patient_id FROM allergy a where a.allergen_type = \"DRUG\" AND a.voided = 0;");
                }
            }

            validator.validate(flag, result);
            if (result.hasErrors()) {
                return new ModelAndView("/module/patientflags/generateFlag");
            }

            // add/update the flag
            Context.getService(FlagService.class).saveFlag(flag);

            // clears the command object from the session
            status.setComplete();

            // just display the edit page again
            return new ModelAndView("redirect:/module/patientflags/manageFlags.form");
        }


}
