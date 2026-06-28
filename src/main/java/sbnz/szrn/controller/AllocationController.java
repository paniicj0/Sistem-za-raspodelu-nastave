package sbnz.szrn.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sbnz.szrn.demo.DemoDataFactory;
import sbnz.szrn.dto.AllocationInput;
import sbnz.szrn.dto.AllocationOutput;
import sbnz.szrn.service.AllocationService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @GetMapping("/demo-input")
    public AllocationInput demoInput() {
        return DemoDataFactory.createDemoInput();
    }

    @GetMapping("/demo-allocation")
    public AllocationOutput demoAllocation() {
        return allocationService.allocate(DemoDataFactory.createDemoInput());
    }

    @PostMapping("/allocate")
    public AllocationOutput allocate(@RequestBody AllocationInput input) {
        return allocationService.allocate(input);
    }
}
