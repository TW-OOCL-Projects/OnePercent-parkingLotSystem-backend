package com.onepercent.ParkingLotApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onepercent.ParkingLotApplication.config.WebSecurityConfig;
import com.onepercent.ParkingLotApplication.domain.ParkingLot;
import com.onepercent.ParkingLotApplication.exception.OperationNotAllowedException;
import com.onepercent.ParkingLotApplication.service.ParkingLotService;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dylan Wei
 * @date 2018-08-01 16:14
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ParkingLotController.class)
@AutoConfigureMockMvc(secure=false)
public class ParkingLotControllerTest {
    @MockBean
    private ParkingLotService parkingLotService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private static final String prefix = "/parkinglots";

    @Test
    public void should_get_204_when_add_parking_lot_successfully() throws Exception {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("停车场1");
        parkingLot.setTotalSize(100);
        mockMvc.perform(
                post(prefix).content(mapper.writeValueAsString(parkingLot))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().is(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void should_get_403_when_add_OperationNotAllowedExcepton_thrown() throws Exception {
        OperationNotAllowedException exception =
                new OperationNotAllowedException();
        doThrow(exception).when(this.parkingLotService).addParkingLot(any(ParkingLot.class));

        mockMvc.perform(
                post(prefix).content(mapper.writeValueAsString(
                        new ParkingLot())).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(HttpStatus.SC_FORBIDDEN))
                .andExpect(content().string(exception.getMessage()));
    }




}