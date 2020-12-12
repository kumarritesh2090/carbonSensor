package com.example.ritesh.carboncollector.controller;

import com.example.ritesh.carboncollector.mongo.model.Sensor;
import com.example.ritesh.carboncollector.postgres.enumeration.SensorStatus;
import com.example.ritesh.carboncollector.restweb.controller.SensorController;
import com.example.ritesh.carboncollector.restweb.path.ApiPath;
import com.example.ritesh.carboncollector.restweb.request.CarbonReadingVo;
import com.example.ritesh.carboncollector.restweb.response.SensorAlertsResponse;
import com.example.ritesh.carboncollector.restweb.response.SensorMetricsResponse;
import com.example.ritesh.carboncollector.restweb.response.SensorStatusResponse;
import com.example.ritesh.carboncollector.service.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.MockitoAnnotations.initMocks;

public class SensorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SensorReadingService sensorReadingService;

    @Mock
    private SensorService sensorService;

    @Mock
    private SensorStatsService sensorStatsService;

    @Mock
    private AlertService alertService;

    @Mock
    private InitService initService;

    @InjectMocks
    private SensorController sensorController;

    @Before
    public void setUp() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.sensorController).build();
    }

    @After
    public void tearDown() {
    }

    private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    @Test
    public void readings() throws Exception {
        CarbonReadingVo carbonReadingVo = new CarbonReadingVo(4444, LocalDate.now().toString());
        Mockito.doNothing().when(sensorReadingService).captureReading(Mockito.any(CarbonReadingVo.class), Mockito.any(UUID.class));
        mockMvc
                .perform(MockMvcRequestBuilders.post(ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.MEASUREMENTS, UUID.randomUUID())
                        .content(convertObjectToJsonBytes(carbonReadingVo))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted()).andReturn();
        Mockito.verify(sensorReadingService).captureReading(Mockito.any(CarbonReadingVo.class), Mockito.any(UUID.class));
    }

    @Test
    public void readingsWithInvalidRequestBody() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post(ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.MEASUREMENTS, UUID.randomUUID())
                        .content(convertObjectToJsonBytes(new Sensor(UUID.randomUUID(), 1f, 1)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        Mockito.verifyNoInteractions(sensorReadingService);

    }

    @Test
    public void getSensorMetrics() throws Exception {
        SensorMetricsResponse sensorMetricsResponse = new SensorMetricsResponse(4.0f,4);
        Mockito.when(sensorService.getSensorMetrics(Mockito.any(UUID.class))).thenReturn(sensorMetricsResponse);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.METRICS,UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
       // :TODO check the object
        Mockito.verify(sensorService).getSensorMetrics(Mockito.any(UUID.class));
    }

    @Test
    public void getSensorStatus() throws Exception {
        SensorStatusResponse sensorStatusResponse = new SensorStatusResponse(SensorStatus.OK.name());
        Mockito.when(sensorStatsService.getStatus(Mockito.any(UUID.class))).thenReturn(sensorStatusResponse);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(ApiPath.ROOT_WITH_SENSOR_ID,UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        // :TODO check the object
        Mockito.verify(sensorStatsService).getStatus(Mockito.any(UUID.class));
    }

    @Test
    public void getSensorAlerts() throws Exception {
        SensorAlertsResponse sensorAlertsResponse = new SensorAlertsResponse();
        Mockito.when(alertService.getAlerts(Mockito.any(UUID.class))).thenReturn(sensorAlertsResponse);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.ALERTS,UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        // :TODO check the object
        Mockito.verify(alertService).getAlerts(Mockito.any(UUID.class));
    }

    @Test
    public void getSensorAlertsEmptyResponse() throws Exception {
        Mockito.when(alertService.getAlerts(Mockito.any(UUID.class))).thenReturn(null);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.ALERTS,UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();
        // :TODO check the object
        Mockito.verify(alertService).getAlerts(Mockito.any(UUID.class));
    }
}
