package com.cometproject.manager.controllers;

import com.cometproject.manager.repositories.*;
import com.cometproject.manager.repositories.customers.Customer;
import com.cometproject.manager.repositories.customers.roles.CustomerRole;
import com.cometproject.manager.repositories.instances.Instance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

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

    @Autowired
    private ConfigurationRepository configurationRepository;

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
        modelAndView.addObject("hosts", hostRepository.findAll());

        modelAndView.addObject("pageName", "admin-instances");

        if (request.getSession().getAttribute("saved") != null) {
            modelAndView.addObject("saved", true);

            request.getSession().setAttribute("saved", null);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/admin/instances/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editInstance(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws IOException {
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
        modelAndView.addObject("editingId", id);

        modelAndView.addObject("instances", instanceRepository.findAll());
        modelAndView.addObject("hosts", hostRepository.findAll());

        modelAndView.addObject("pageName", "admin-instances");

        return modelAndView;
    }

    @RequestMapping(value = "/admin/instances/save/{id}", method = RequestMethod.POST)
    public void saveInstance(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("id") String instanceId,
                             @RequestParam("instance-name") String instanceName, @RequestParam("instance-host") String instanceHost,
                             @RequestParam("instance-com.cometproject.networking.api.config") String instanceConfig) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return;
        }

        final Instance instance = instanceRepository.findOne(instanceId);
        final Type type = new TypeToken<Map<String, String>>(){}.getType();
        final Map<String, String> configuration = new Gson().fromJson(instanceConfig, type);

        instance.setName(instanceName);
        instance.setServer(instanceHost);
        instance.setConfig(configuration);

        instanceRepository.save(instance);

        request.getSession().setAttribute("saved", true);
        response.sendRedirect("/admin/instances");
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

    @RequestMapping(value = "/admin/configuration", method = RequestMethod.GET)
    public ModelAndView configuration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if(!customer.hasRole(CustomerRole.ADMINISTRATOR)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("admin/configuration");

        modelAndView.addObject("customer", customer);

        modelAndView.addObject("configuration", configurationRepository.findAll());

        modelAndView.addObject("pageName", "admin-configuration");

        return modelAndView;
    }
}