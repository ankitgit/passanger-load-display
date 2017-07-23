package com.devavidity.passenger.load.display.controller;

import com.devavidity.passenger.load.display.model.Compartment;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class PassengerLoadController {

    static Map<String, Compartment> compartmentMap = new HashMap<>();

    @GetMapping("/currentStatus")
    public ModelAndView currentStatus() {
        List<Compartment> listOfCompartments = compartmentMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(compartment -> compartment.getVehicleId() + "" + compartment.getCompartmentId()))
                .collect(Collectors.toList());
        return new ModelAndView("vehicleLoad", "compartments", listOfCompartments);
    }

    public void addValue(String key, Compartment compartment) {
        compartmentMap.put(key, compartment);
    }

}
