package com.cometproject.manager.controllers;

import com.cometproject.manager.repositories.CustomerRepository;
import com.cometproject.manager.repositories.HostRepository;
import com.cometproject.manager.repositories.InstanceRepository;
import com.cometproject.manager.repositories.VersionRepository;
import com.cometproject.manager.repositories.customers.Customer;
import com.cometproject.manager.repositories.customers.roles.CustomerRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private VersionRepository versionRepository;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if(!customer.hasRole(CustomerRole.ADMINISTRATOR)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("admin/dashboard");

        modelAndView.addObject("customer", customer);


        modelAndView.addObject("pageName", "admin-dash");

        return modelAndView;
    }

    @RequestMapping(value = "/admin/hosts", method = RequestMethod.GET)
    public ModelAndView hosts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if(!customer.hasRole(CustomerRole.ADMINISTRATOR)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("admin/hosts");

        modelAndView.addObject("customer", customer);
        modelAndView.addObject("instances", customer.getInstances(this.instanceRepository));

        modelAndView.addObject("hosts", this.hostRepository.findAll());

        modelAndView.addObject("pageName", "admin-hosts");

        return modelAndView;
    }


    @RequestMapping(value = "/admin/instances", method = RequestMethod.GET)
    public ModelAndView instances(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if(!customer.hasRole(CustomerRole.ADMINISTRATOR)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("admin/instances");

        modelAndView.addObject("customer", customer);

        modelAndView.addObject("instances", instanceRepository.findAll());

        modelAndView.addObject("pageName", "admin-instances");

        return modelAndView;
    }

    @RequestMapping(value = "/admin/versions", method = RequestMethod.GET)
    public ModelAndView versions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if(!customer.hasRole(CustomerRole.ADMINISTRATOR)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("admin/versions");

        modelAndView.addObject("customer", customer);

        modelAndView.addObject("instances", instanceRepository.findAll());
        modelAndView.addObject("versions", versionRepository.findAll());

        modelAndView.addObject("pageName", "admin-versions");

        return modelAndView;
    }
}