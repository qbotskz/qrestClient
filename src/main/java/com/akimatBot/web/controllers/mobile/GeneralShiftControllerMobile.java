package com.akimatBot.web.controllers.mobile;


import com.akimatBot.entity.custom.GeneralShift;
import com.akimatBot.repository.repos.GeneralShiftRepo;
import com.akimatBot.repository.repos.PropertiesRepo;
import com.akimatBot.services.EmployeeService;
import com.akimatBot.services.GeneralShiftService;
import com.akimatBot.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/mobile/shift/general")

public class GeneralShiftControllerMobile {

    @Autowired
    GeneralShiftRepo generalShiftRepo;

    @Autowired
    PropertiesRepo propertiesRepo;

    @Autowired
    GeneralShiftService generalShiftService;

    @Autowired
    OrderService orderService;

    @Autowired
    EmployeeService employeeService;


//    @PreAuthorize("@permissionEvaluator.hasRoleByCode(#code, 'GENERAL_SHIFT')")
//    @PostMapping("/open")
//    public ResponseEntity<Object> openShift(@RequestHeader("code") Long code){
//        GeneralShift generalShift = generalShiftService.openShift(code);
//        if (generalShift != null)
//            return new ResponseEntity<>(generalShift.getDTO(),HttpStatus.CREATED);
//
//        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
//    }
//
//
//    @PreAuthorize("@permissionEvaluator.hasRoleByCode(#code, 'GENERAL_SHIFT')")
//    @PostMapping("/close")
//    public Object closeShift(@RequestHeader("code") Long code) {
//
//        if (!orderService.existActiveOrders() && generalShiftService.closeShift(code)){
//            employeeService.closeAllShifts();
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        }
//
//        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
//
//    }

    @GetMapping("/isOpen")
    public ResponseEntity<Object> isOpenShift(){

        GeneralShift generalShift = generalShiftService.getOpenedShift();
        if (generalShift != null){
            return new ResponseEntity<>(generalShift.getDTO(), HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

}
