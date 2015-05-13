package com.cometproject.manager.controllers;

import com.cometproject.manager.repositories.CustomerRepository;
import com.cometproject.manager.repositories.customers.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {
    private static final byte ERROR_INVALID_CREDENTIALS = 0;
    private static final byte ERROR_SUSPENDED = 1;

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getSession() != null && request.getSession().getAttribute("customer") != null) {
            response.sendRedirect("/home");
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("index");

        if (request.getSession() != null && request.getSession().getAttribute("loginError") != null) {
            final byte loginError = ((byte) request.getSession().getAttribute("loginError"));

            modelAndView.addObject("loginError", loginError);
            request.getSession().removeAttribute("loginError");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response, @RequestParam("emailAddress") String email, @RequestParam("password") String password) throws IOException {
        final Customer customer = customerRepository.findCustomerByEmailAndPassword(email, password);

        if (customer == null) {
            request.getSession().setAttribute("loginError", ERROR_INVALID_CREDENTIALS);
        } else if (customer.isSuspended()) {
            request.getSession().setAttribute("loginError", ERROR_SUSPENDED);
        } else {
            request.getSession().setAttribute("customer", customer.getId());
            response.sendRedirect("/home");
            return;
        }

        response.sendRedirect("/");
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getSession() != null)
            request.getSession().invalidate();

        response.sendRedirect("/");
    }
}
