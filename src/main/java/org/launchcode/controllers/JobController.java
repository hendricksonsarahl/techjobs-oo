package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.Job;
import org.launchcode.models.data.JobData;
import org.launchcode.models.forms.JobForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode and Sarah Hendrickson
 */

@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // Completed #1 - get the Job with the given ID and pass it into the view

        Job desiredJob = jobData.findById(id);

        if (desiredJob == null) {
            return "redirect:";
        } else {
            model.addAttribute("job", desiredJob);
            return "job-detail";
        }
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // Completed #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()) {
            model.addAttribute("name", errors);
            model.addAttribute(jobForm.getName());
            return "new-job";
        } else {
            String newJobName = jobForm.getName();

            int employerId = jobForm.getEmployerId();
            int locationId = jobForm.getLocationId();
            int positionTypeId = jobForm.getPositionTypeId();
            int coreCompetencyId = jobForm.getCoreCompetencyId();

            Employer newEmployer = jobData.getEmployers().findById(employerId);
            Location newLocation = jobData.getLocations().findById(locationId);
            PositionType newPositionType = jobData.getPositionTypes().findById(positionTypeId);
            CoreCompetency newCoreCompetency = jobData.getCoreCompetencies().findById(coreCompetencyId);

            Job newJob = new Job(newJobName, newEmployer, newLocation, newPositionType, newCoreCompetency);
            jobData.add(newJob);
            model.addAttribute(newJob);
            return "redirect:/job?id=" + newJob.getId();

        }

    }
}
