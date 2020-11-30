package me.java.library.rpc.thrift.client.loadbalancer;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.java.library.rpc.thrift.client.common.ThriftServerNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RoundRobinRule extends AbstractLoadBalancerRule {

    private AtomicInteger nextServerCyclicCounter;

    public RoundRobinRule() {
        nextServerCyclicCounter = new AtomicInteger();
    }

    public RoundRobinRule(ILoadBalancer lb) {
        this();
        setLoadBalancer(lb);
    }

    @Override
    public ThriftServerNode choose(String key) {
        return choose(getLoadBalancer(), key);
    }

    @SuppressWarnings("unchecked")
    private ThriftServerNode choose(ILoadBalancer lb, String key) {
        if (lb == null) {
            log.warn("No specified load balancer");
            return null;
        }

        List<ThriftServerNode> serverNodes;
        ThriftServerNode serverNode = null;
        int count = 0;

        while (serverNode == null && count++ < 10) {
            serverNodes = lb.getServerNodes(key);
            if (CollectionUtils.isEmpty(serverNodes)) {
                Map<String, LinkedHashSet<ThriftServerNode>> serverNodesMap = lb.getAllServerNodes();

                if (MapUtils.isEmpty(serverNodesMap) || !serverNodesMap.containsKey(key)) {
                    log.warn("No up servers of key {}, available from load balancer: " + lb, key);
                    return null;
                }

                LinkedHashSet<ThriftServerNode> thriftServerNodes = serverNodesMap.get(key);
                if (CollectionUtils.isEmpty(thriftServerNodes)) {
                    log.warn("No up servers of key {}, available from load balancer: " + lb, key);
                    return null;
                }

                serverNodes = Lists.newArrayList(thriftServerNodes);
            }

            int nextServerIndex = incrementAndGetModulo(serverNodes.size());
            serverNode = serverNodes.get(nextServerIndex);

            if (serverNode == null) {
                Thread.yield();
            }
        }

        if (count >= 10) {
            log.warn("No available alive server nodes after 10 tries from load balancer: "
                    + lb);
        }

        return serverNode;
    }

    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next)) {
                return next;
            }
        }
    }

}
