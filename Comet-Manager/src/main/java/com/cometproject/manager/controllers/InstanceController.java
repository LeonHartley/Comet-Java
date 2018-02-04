package com.cometproject.manager.controllers;

import com.cometproject.manager.repositories.CustomerRepository;
import com.cometproject.manager.repositories.HostRepository;
import com.cometproject.manager.repositories.InstanceRepository;
import com.cometproject.manager.repositories.VersionRepository;
import com.cometproject.manager.repositories.customers.Customer;
import com.cometproject.manager.repositories.hosts.Host;
import com.cometproject.manager.repositories.hosts.InstanceStatus;
import com.cometproject.manager.repositories.instances.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
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

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private HostRepository hostRepository;

    private RestTemplate restTemplate = new RestTemplate();

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

        final Instance instance = instanceRepository.findOne(instanceId);
        final Host host = hostRepository.findOneByHostName(instance.getServer());
        final InstanceStatus instanceStatus = host.getInstanceStatus(this.restTemplate, instanceId);

        ModelAndView modelAndView = new ModelAndView("instance-dash");
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("instance", instance);
        modelAndView.addObject("instanceStatus", instanceStatus);
        modelAndView.addObject("version", versionRepository.findOneByVersion(instance.getVersion()));
        modelAndView.addObject("versions", versionRepository.findAll());

        modelAndView.addObject("pageName", "instance-dash");

        return modelAndView;
    }

    @RequestMapping(value = "/instance/{id}/com.cometproject.networking.api.config", method = RequestMethod.GET)
    public ModelAndView config(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String instanceId) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if (!customer.getInstanceIds().contains(instanceId)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("instance-com.cometproject.networking.api.config");
        modelAndView.addObject("customer", customer);

        if (request.getSession().getAttribute("saved") != null) {
            modelAndView.addObject("saved", true);

            request.getSession().setAttribute("saved", null);
        }

        final Instance instance = instanceRepository.findOne(instanceId);
        final Host host = hostRepository.findOneByHostName(instance.getServer());
        final InstanceStatus instanceStatus = host.getInstanceStatus(this.restTemplate, instanceId);

        modelAndView.addObject("pageName", "instance-com.cometproject.networking.api.config");
        modelAndView.addObject("instance", instance);
        modelAndView.addObject("instanceStatus", instanceStatus);

        return modelAndView;
    }

    @RequestMapping(value = "/instance/{id}/console", method = RequestMethod.GET)
    public ModelAndView console(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String instanceId) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        if (!customer.getInstanceIds().contains(instanceId)) {
            response.sendRedirect("/");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("instance-console");
        modelAndView.addObject("customer", customer);

        final Instance instance = instanceRepository.findOne(instanceId);
        final Host host = hostRepository.findOneByHostName(instance.getServer());
        final InstanceStatus instanceStatus = host.getInstanceStatus(this.restTemplate, instanceId);

        modelAndView.addObject("pageName", "instance-console");
        modelAndView.addObject("instance", instance);
        modelAndView.addObject("host", host);
        modelAndView.addObject("instanceStatus", instanceStatus);

        return modelAndView;
    }

    @RequestMapping(value = "/instance/save/{id}", method = RequestMethod.POST)
    public void saveInstance(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("id") String instanceId,
                             @RequestParam("api-token") String apiToken, @RequestParam("db-host") String mysqlHost,
                             @RequestParam("db-username") String mysqlUsername, @RequestParam("db-password") String mysqlPassword,
                             @RequestParam("db-database") String mysqlDatabase) throws IOException {
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

        // We don't want to set these here anymore, we can use the admin panel to do this. It'll stop issues arising such as users stealing other hotels' ports etc.

        //instance.getConfig().put("serverHost", gameHost);
        //instance.getConfig().put("gamePort", gamePort + "");
        //instance.getConfig().put("apiPort", apiPort+ "");
        instance.getConfig().put("apiToken", apiToken);

        instance.getConfig().put("dbHost", mysqlHost);
        instance.getConfig().put("dbUsername", mysqlUsername);
        instance.getConfig().put("dbPassword", mysqlPassword);
        instance.getConfig().put("dbName", mysqlDatabase);
//        instance.getConfig().put("dbPoolMax", "" + dbPool);

        if(instance.getConfig().containsKey("comet.cache.enabled")) {
            instance.getConfig().put("cacheEnabled", instance.getConfig().get("comet.cache.enabled"));
            instance.getConfig().put("cachePrefix", instance.getConfig().get("comet.cache.prefix"));

            instance.getConfig().remove("comet.cache.enabled");
            instance.getConfig().remove("comet.cache.prefix");
        }

        this.instanceRepository.save(instance);

        request.getSession().setAttribute("saved", true);
        response.sendRedirect("/instance/" + instanceId + "/com.cometproject.networking.api.config");
    }

    @RequestMapping(value = "/instance/start/{id}", method = RequestMethod.GET)
    public void startInstance(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("id") String instanceId) throws IOException {
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
        final Host host = this.hostRepository.findOneByHostName(instance.getServer());

        host.startInstance(restTemplate, instance);

        response.sendRedirect("/instance/" + instanceId);
    }

    @RequestMapping(value = "/instance/stop/{id}", method = RequestMethod.GET)
    public void stopInstance(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable("id") String instanceId) throws IOException {
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
        final Host host = this.hostRepository.findOneByHostName(instance.getServer());

        host.stopInstance(restTemplate, instance.getId());

        response.sendRedirect("/instance/" + instanceId);
    }
}
