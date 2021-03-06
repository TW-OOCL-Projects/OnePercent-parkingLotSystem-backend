package com.onepercent.ParkingLotApplication.controller;

import com.onepercent.ParkingLotApplication.domain.Indent;
import com.onepercent.ParkingLotApplication.domain.IndentStatus;
import com.onepercent.ParkingLotApplication.dto.UpdateIndentParams;
import com.onepercent.ParkingLotApplication.exception.IllegalCommandException;
import com.onepercent.ParkingLotApplication.repository.IndentRepository;
import com.onepercent.ParkingLotApplication.service.IndentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dylan Wei
 * @date 2018-08-02 15:05
 */
@RestController
@RequestMapping("/orders")
@Transactional
public class IndentController {
    @Autowired
    private IndentService indentService;


    @Autowired
    private IndentRepository indentRepository;

    @GetMapping
    public List<Indent> getAllOrders() {
        return this.indentRepository.findAll();
    }

    @PostMapping
    public String generateOrder(String carNo) {
        return this.indentService.generateIndent(carNo);
    }

    @GetMapping("/{status}")
    public List<Indent> getOrders(
            @PathVariable String status) {
        return this.indentService.getIndentsWithStatus(status);
    }

    @PutMapping("/{receiptNo}")
    public Indent unpark(@PathVariable String receiptNo){
        return this.indentService.changeIndentStatusByReceiptNo(
                receiptNo, IndentStatus.WAITING_TO_RETRIEVE);
    }

    @PatchMapping("/{indentId}")
    public Object updateIndent(
            @PathVariable Long indentId, UpdateIndentParams params) {
        switch (params.getOperation()) {
            case "robOrder":
                return this.robIndent(indentId, params.getCoordinatorId());
            case "setParkingLotId":
                return this.indentService.setParkingLotToIndent(indentId, params.getParkingLotId());
            case "updateStatus":
                return this.indentService.changeIndentStatusById(indentId, params.getStatus());
            default:
                throw new IllegalCommandException();
        }
    }

    private Indent robIndent(@PathVariable Long indentId, Integer coordinatorId) {
        return this.indentService.robIndent(indentId, coordinatorId);
    }



}
