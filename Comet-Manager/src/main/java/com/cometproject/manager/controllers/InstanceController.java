package com.cometproject.manager.controllers;

import com.cometproject.manager.repositories.CustomerRepository;
import com.cometproject.manager.repositories.InstanceRepository;
import com.cometproject.manager.repositories.customers.Customer;
import com.cometproject.manager.repositories.instances.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class InstanceController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @RequestMapping(value = "/instance/{id}", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String instanceId) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if (!customer.getInstanceIds().contains(instanceId)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("instance");
        modelAndView.addObject("customer", customer);

        if (request.getSession().getAttribute("saved") != null) {
            modelAndView.addObject("saved", true);

            request.getSession().setAttribute("saved", null);
        }

        modelAndView.addObject("instance", instanceRepository.findOne(instanceId));

        return modelAndView;
    }

    @RequestMapping(value = "/instance/save/{id}", method = RequestMethod.POST)
    public void saveInstance(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("id") String instanceId, @RequestParam("db-host") String mysqlHost,
                             @RequestParam("db-username") String mysqlUsername, @RequestParam("db-password") String mysqlPassword,
                             @RequestParam("db-database") String mysqlDatabase, @RequestParam("db-pool") int dbPool) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return;
        }


        final Customer customer = this.customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if (!customer.getInstanceIds().contains(instanceId)) {
            response.sendRedirect("/");
            return;
        }

        final Instance instance = this.instanceRepository.findOne(instanceId);

        instance.getConfig().put("dbHost", mysqlHost);
        instance.getConfig().put("dbUsername", mysqlUsername);
        instance.getConfig().put("dbPassword", mysqlPassword);
        instance.getConfig().put("dbName", mysqlDatabase);
        instance.getConfig().put("dbPoolMax", "" + dbPool);

        this.instanceRepository.save(instance);

        request.getSession().setAttribute("saved", true);

        ModelAndView modelAndView = new ModelAndView("instance");
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("instance", instance);

        response.sendRedirect("/instance/" + instanceId);
    }

}
