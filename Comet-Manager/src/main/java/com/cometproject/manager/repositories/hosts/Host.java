package com.cometproject.manager.repositories.hosts;

import com.cometproject.manager.repositories.instances.Instance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class Host {

    private static final Map<String, String> cometConfigMappings = new HashMap<String, String>() {{
        put("serverHost", "comet.network.host");
        put("gamePort", "comet.network.port");

        put("apiPort", "comet.api.port");
        put("apiToken", "comet.api.token");

        put("dbHost", "comet.db.host");
        put("dbUsername", "comet.db.username");
        put("dbPassword", "comet.db.password");
        put("dbName", "comet.db.name");
        put("dbPoolMin", "comet.db.pool.min");
        put("dbPoolMax", "comet.db.pool.max");

        put("cacheEnabled", "comet.cache.enabled");
        put("cacheAddress", "comet.cache.address");
        put("cachePrefix", "comet.cache.prefix");
    }};

    private String id;
    private String hostName;
    private String endpoint;
    private String address;
    private String authenticationKey;

    public Host(String id, String hostName, String endpoint, String authenticationKey, String address) {
        this.id = id;
        this.hostName = hostName;
        this.endpoint = endpoint;
        this.address = address;
        this.authenticationKey = authenticationKey;
    }

    public InstanceStatus getInstanceStatus(RestTemplate restTemplate, String instanceId) {
        return restTemplate.getForObject(this.endpoint + "process/status/" + instanceId, InstanceStatus.class);
    }

    public InstanceStatus stopInstance(RestTemplate restTemplate, String instanceId) {
        return restTemplate.getForObject(this.endpoint + "process/stop/" + instanceId, InstanceStatus.class);
    }

    public void startInstance(RestTemplate restTemplate, Instance instance) {
        final StringBuilder applicationArguments = new StringBuilder();

        applicationArguments.append("comet.api.enabled=true ");

        for (Map.Entry<String, String> configEntry : instance.getConfig().entrySet()) {
            if (!cometConfigMappings.containsKey(configEntry.getKey())) {
                applicationArguments.append(configEntry.getKey()).append("=").append(configEntry.getValue()).append(" ");
            } else {
                applicationArguments.append(cometConfigMappings.get(configEntry.getKey())).append("=").append(configEntry.getValue()).append(" ");
            }
        }

        System.out.println(applicationArguments.toString());

        final HttpHeaders headers = new HttpHeaders();

        final LinkedMultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        postParameters.add("name", instance.getId());
        postParameters.add("applicationArguments", applicationArguments.toString());
        postParameters.add("serverVersion", instance.getVersion());
        postParameters.add("apiUrl", "http://" + this.address + ":" + instance.getConfig().get("apiPort"));
        postParameters.add("apiToken", instance.getConfig().get("apiToken"));

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<LinkedMultiValueMap<String, String>>(postParameters, headers);

        HttpEntity<InstanceStatus> instanceStatus = restTemplate.postForEntity(this.endpoint + "/process/start/CometServer", request, InstanceStatus.class);
    }

    public String getId() {
        return id;
    }

    public String getHostName() {
        return hostName;
    }

    public Host setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Host setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public Host setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
