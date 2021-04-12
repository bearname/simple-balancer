package ru.mikushov.balancer;

import java.util.*;
import java.util.stream.Collectors;

public class WeightedRoundRobinLoadBalancer extends RoundRobinLoadBalancer {

    public WeightedRoundRobinLoadBalancer(Map<String, Integer> ipMap) {
        super(ipMap.keySet()
                .stream()
                .map(ip -> {
                    List<String> tempList = new LinkedList<>();
                    for (int i = 0; i < ipMap.get(ip); i++) {
                        tempList.add(ip);
                    }
                    return tempList;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
        );
    }
}
