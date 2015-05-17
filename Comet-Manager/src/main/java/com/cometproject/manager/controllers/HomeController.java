package com.cometproject.manager.controllers;

import com.cometproject.manager.repositories.CustomerRepository;
import com.cometproject.manager.repositories.InstanceRepository;
import com.cometproject.manager.repositories.customers.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HomeController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession() == null || request.getSession().getAttribute("customer") == null) {
            response.sendRedirect("/");
            return null;
        }

        final Customer customer = customerRepository.findOne((String) request.getSession().getAttribute("customer"));

        ModelAndView modelAndView = new ModelAndView("home");

        modelAndView.addObject("customer", customer);
        modelAndView.addObject("instances", customer.getInstances(this.instanceRepository));

        return modelAndView;
    }
}
