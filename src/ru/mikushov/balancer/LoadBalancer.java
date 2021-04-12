package ru.mikushov.balancer;

import java.util.Collections;
import java.util.List;

public abstract class LoadBalancer {
    final List<String> ipList;

    protected LoadBalancer(List<String> ipList) {
        this.ipList = Collections.unmodifiableList(ipList);
    }

    abstract String getIp();

    public List<String> getIpList() {
        return ipList;
    }
}
