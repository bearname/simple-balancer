package ru.mikushov.balancer;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer extends LoadBalancer {

    private Random random = new Random();

    protected RandomLoadBalancer(List<String> ipList) {
        super(ipList);
    }

    @Override
    String getIp() {
        random = new Random();
        return ipList.get(random.nextInt(ipList.size()));
    }
}
