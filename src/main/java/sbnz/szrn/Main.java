package sbnz.szrn;

import sbnz.szrn.demo.DemoDataFactory;
import sbnz.szrn.dto.AllocationInput;
import sbnz.szrn.dto.AllocationOutput;
import sbnz.szrn.report.ConsoleReportPrinter;
import sbnz.szrn.service.AllocationService;

public class Main {

    public static void main(String[] args) {
        AllocationInput input = DemoDataFactory.createDemoInput();

        AllocationService allocationService = new AllocationService();
        AllocationOutput output = allocationService.allocate(input);

        ConsoleReportPrinter.print(input, output);
    }
}