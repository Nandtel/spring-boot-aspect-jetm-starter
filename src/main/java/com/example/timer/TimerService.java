package com.example.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import etm.core.aggregation.ExecutionAggregate;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import etm.core.renderer.MeasurementRenderer;

/**
 * Service for measuring timing of code execution
 *
 * @Created 4/3/2016
 */
@Component
public class TimerService implements MeasurementRenderer, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TimerService.class);

    EtmMonitor monitor;
    private Executor executor = Executors.newCachedThreadPool();

    public TimerService() {
        super();
        BasicEtmConfigurator.configure();
        monitor = EtmManager.getEtmMonitor();
        monitor.start();
    }

    public EtmPoint createPoint(String name) {
        return monitor.createPoint(name);
    }

    public EtmPoint submit(EtmPoint point) {
        point.collect();
        executor.execute(this);
        return point;
    }

    @Override
    public void render(Map points) {
        points.keySet()
                .stream()
                .forEach(object -> {

                    ExecutionAggregate aggregate = (ExecutionAggregate) points.get(object);
                    String averageKey = object + ".timer.average.ms";
                    int value = (int) aggregate.getAverage();
                    logger.info(averageKey + " is " + value);

                });

        monitor.reset();
    }

    @Override
    public void run() {
        monitor.render(this);
    }
}
