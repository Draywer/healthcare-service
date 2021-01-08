package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class MedicalServiceImplTest {

    private MedicalService medicalService;
    private PatientInfoRepository patientInfoRepository;
    private SendAlertService alertService;
    private String patientId;
    private BloodPressure bloodPressureOne;
    private HealthInfo healthInfoOne;
    private PatientInfo patientInfo;
    private String message;
    private ArgumentCaptor<String> argumentCaptor;


    @BeforeAll
    public static void globalSetUp() {
        System.out.println("Тестирование класса MedicalServiceImpl...");
    }

    @BeforeEach
    void setUp() {
        patientId = "123";
        healthInfoOne = new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80));
        patientInfo = new PatientInfo(
                patientId,
                "Иван",
                "Петров",
                LocalDate.of(1980, 11, 26),
                healthInfoOne);
        message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        argumentCaptor = ArgumentCaptor.forClass(String.class);

        patientInfoRepository = mock(PatientInfoFileRepository.class);
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        alertService = spy(SendAlertServiceImpl.class);

        medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
    }


    @Test
    @DisplayName("Проверка вывода сообщения во время проверки давления")
    void test_checkBloodPressure() {
        System.out.println("Проверка вывода сообщения во время проверки давления");
        HealthInfo healthInfoTwo =  new HealthInfo(new BigDecimal("36.65"), new BloodPressure(50, 80));

        medicalService.checkBloodPressure(patientId, healthInfoTwo.getBloodPressure());

        if (!patientInfo.getHealthInfo().getBloodPressure().equals(healthInfoTwo.getBloodPressure())) {
            verify(alertService).send(argumentCaptor.capture());
            assertEquals(message, argumentCaptor.getValue());
        }
    }

    @Test
    @DisplayName("Проверка вывода сообщения во время проверки температуры")
    void test_checkTemperature() {
        System.out.println("Проверка вывода сообщения во время проверки температуры");
        HealthInfo healthInfoTwo =  new HealthInfo(new BigDecimal("37.2"), new BloodPressure(200, 100));

        medicalService.checkTemperature(patientId, healthInfoTwo.getNormalTemperature());
        if(patientInfo.getHealthInfo().getNormalTemperature().add(new BigDecimal("1.5")).compareTo(healthInfoTwo.getNormalTemperature()) > 0) {
            verify(alertService).send(argumentCaptor.capture());
            assertEquals(message, argumentCaptor.getValue());
        }

    }
}