package com.cometproject.manager.controllers.api;

import com.cometproject.manager.repositories.InstanceRepository;
import com.cometproject.manager.repositories.instances.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InstanceApiController {

    @Autowired
    private InstanceRepository instanceRepository;

    @RequestMapping(value = "/api/instance/data/{id}/{authKey}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Instance> getInstanceData(@PathVariable String id, @PathVariable String authKey) {
        Instance instance = this.instanceRepository.findOneByIdAndAuthKey(id, authKey);

        if(instance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(instance, HttpStatus.OK);
    }
}
